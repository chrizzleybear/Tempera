import asyncio
import logging
import sys
from typing import List, Tuple

import bleak.exc
import requests
from bleak import BLEDevice, BleakScanner, BleakClient, AdvertisementData
from sqlalchemy import select
from sqlalchemy.orm import Session
from tenacity import retry, retry_if_exception_type, wait_fixed

from tempera.bleclient.etl import filter_uuid
from tempera.database.entities import TemperaStation
from tempera.exceptions import BluetoothOffException, BluetoothConnectionLostException
from tempera.utils import shared
from tempera.utils.request_utils import make_request

logger = logging.getLogger(f"tempera.{__name__}")


REQUIRED_SERVICES = ["180a", "183f", "181a"]
REQUIRED_CHARACTERISTICS = ["2a25", "2bf2", "2a6e", "2a77", "2a6f", "2bd3"]
SCANNING_TIMEOUT = 5


async def detection_callback(
    device: BLEDevice, advertisement_data: AdvertisementData
) -> None:
    """
    Callback to execute when the :class:`~BleakScanner` detects a bluetooth device.

    :param device: detected device.
    :param advertisement_data: device's advertisement data.
    :return: None
    """
    logger.info(
        f"Device[name:{device.name};address:{device.address};signal_strength(RSSI):{advertisement_data.rssi};"
        f"ad_data:{advertisement_data}]"
    )


async def get_tempera_stations() -> List[BLEDevice] | None:
    """
    Scan for BLE devices and filter out the tempera stations according to the advertisement data.

    :return: a list of tempera stations found.
    """
    logger.info("Scanning for BLE devices...")
    scanner = BleakScanner(detection_callback)
    try:
        devices = await scanner.discover(timeout=SCANNING_TIMEOUT)
    except bleak.exc.BleakError:
        raise BluetoothOffException

    logger.info(f"Found devices: {devices}")

    tempera_stations = []
    for device in devices:
        if "G4T1" in device.name:
            logger.info(
                f"Found tempera station: Device[name:{device.name};address:{device.address}]"
            )
            tempera_stations.append(device)

    if not tempera_stations:
        logger.warning(
            "No devices found with 'G4T1' in their name.\n"
            "Make sure 'G4T1' is part of the tempera station's name you are trying to connect."
        )
        return None

    return tempera_stations


async def validate_station(
    tempera_station: BLEDevice, client: BleakClient
) -> BLEDevice | None:
    """
    Returns the first valid tempera station. Valid means that its ID corresponds to one stored in the webapp back end
    and the station implements the required GATT services and characteristics.

    This function isn't retried with tenacity after a connection loss, so that the access point isn't stuck trying
    to connect to a station that is offline e.g., when you want to exchange one station for another.
    If the connection is lost and the same station should be connected again, this will happen in the discovery
    loop if possible, without further measures.

    **Note:** if the connection to the web server is lost, all stations are considered valid that implement the
    required services and have G4T1 in their name. This leads to two scenarios:

    * The bluetooth connection was already established and then lost. Here, the measurements and time records are read
      and saved until the connection to the web server can be restored and the data is transferred (before the transfer
      validation will occur again).
    * No bluetooth connection was established prior to the web server connection disruption
      (can only happen at startup). In this scenario, data of an unauthorized/deactivated station may be read and
      stored in some unlikely scenarios, but once the connection is established again,
      it won't be sent to the server because the validation will fail so no harm no fault.

    :param tempera_station: the tempera station BLE device.
    :param client: the connection to the tempera station, i.e., the client.
    :return: the tempera station passed as a parameter, if it satisfies all requirements, else None.

    :raises BluetoothConnectionLostException: if the established connection to the tempera station is disrupted.
    """
    logger.info(f"Trying to validate stations: {tempera_station}")
    try:
        response = await make_request(
            "get",
            f"{shared.config['webserver_address']}/rasp/api/valid_devices",
            auth=shared.header,
            params={"access_point_id": shared.config["access_point_id"]},
        )
    except requests.exceptions.ConnectionError:
        logger.error(
            "Can't get valid stations. Couldn't establish a connection to the web server "
            f"at {shared.config['webserver_address']}. Device validation failed."
        )
        return tempera_station

    if not response["access_point_allowed"]:
        logger.critical(
            f"Access point not approved or enabled! "
            f"Ensure that this access point ({shared.config['access_point_id']}) "
            f"is registered and enabled in the web server."
        )
        sys.exit(0)

    allowed_stations = response["stations_allowed"]

    try:
        async with asyncio.TaskGroup() as tg:
            id_ok = tg.create_task(validate_id(client, allowed_stations))
            missing_characteristics = tg.create_task(validate_characteristics(client))
    except* bleak.exc.BleakError:
        logger.error(
            f"Lost connection to {client.address}, going back to device discovery."
        )
        raise BluetoothConnectionLostException

    id_ok, station_id = id_ok.result()
    missing_characteristics = missing_characteristics.result()

    if id_ok and not missing_characteristics:
        await save_station(station_id)
        logger.info(
            f"Station[name: {tempera_station.name}; address: {tempera_station.address}] is valid."
        )
        try:
            shared.current_station_id = await get_station_id(client)
        except bleak.exc.BleakError:
            logger.error(
                f"Lost connection to {client.address}, going back to device discovery."
            )
            raise BluetoothConnectionLostException
        return tempera_station
    elif id_ok and missing_characteristics is not None:
        logger.info(
            f"Station[name: {tempera_station.name}; address: {tempera_station.address}] meets ID requirement but lacks"
            f" the following characteristics: {missing_characteristics}"
        )
        return None
    else:
        logger.info(
            f"Station[name: {tempera_station.name}; address: {tempera_station.address}] doesn't meet ID requirement! "
            f"Make sure you have registered this station's ID in the web app server if you want to connect to it."
        )
        return None


async def get_station_id(client: BleakClient) -> str:
    """
    Read the serial number (=id) of the connected tempera station.

    :param client: the connection to the tempera station.
    :return: the id of the tempera station.
    """
    device_info_service = await filter_uuid(client, "180a")
    serial_number_characteristic = await filter_uuid(device_info_service, "2a25")
    station_id = await client.read_gatt_char(serial_number_characteristic.uuid)
    return station_id.decode()


async def validate_id(client: BleakClient, valid_ids: List[str]) -> Tuple[bool, str]:
    """

    :param client: the connection to the tempera station.
    :param valid_ids: a list of valid ids to check the tempera station id against.
    :return: Tuple of (true, station id) if the id is valid, else (False, station id)
    """
    station_id = await get_station_id(client)
    if station_id == "":
        logger.info(f"No station ID found for station {client.address}")
        return False, station_id

    logger.info(f"Checking station ID '{station_id}' against web app server data.")

    return station_id in valid_ids, station_id


async def validate_characteristics(client: BleakClient) -> List[str]:
    """
    Check if all required characteristics are implemented by a station.

    :param client: the connection to the tempera station.
    :return: a list of missing characteristics. This is an empty list if all are implemented.
    """
    missing_characteristics = REQUIRED_CHARACTERISTICS
    for service in client.services:
        for characteristic in service.characteristics:
            for required_characteristic in REQUIRED_CHARACTERISTICS:
                if required_characteristic in characteristic.uuid:
                    missing_characteristics.remove(required_characteristic)

    return missing_characteristics


async def save_station(station_id: str) -> None:
    """
    Save the tempera station id if it isn't already in the database.

    :param station_id: the id of the station to save.
    :returns: None
    """
    with Session(shared.db_engine) as session:
        station = session.scalars(
            select(TemperaStation).where(TemperaStation.id == station_id)
        ).first()

        if station:
            logger.info(f"Station {station} already known. Skipping save to database.")
        else:
            logger.info(f"Saving {station} as it meets all requirements.")
            station = TemperaStation(id=station_id)

            session.add(station)
            session.commit()


@retry(retry=retry_if_exception_type(RuntimeWarning), wait=wait_fixed(30))
async def discovery_loop() -> BLEDevice:
    """
    Piece together multiple functions to create a discovery loop and return the first valid tmepera station found.

    #. Search for tempera stations with :func:`~get_tempera_stations`
    #. Validate the tempera stations if found with :func:`~validate_station`

    :return: the first valid tempera station found.

    :raises RuntimeWarning: if no tempera station is found and starts looking for devices again.
    """
    tempera_stations = await get_tempera_stations()

    if not tempera_stations:
        logger.warning("No tempera stations found. Going back to device discovery.")
        raise RuntimeWarning

    for station in tempera_stations:
        async with BleakClient(station) as client:
            try:
                tempera_station = await validate_station(station, client)
            except bleak.exc.BleakError:
                logger.info(f"Can't connect to {client.address}. Skipping validation.")

        if tempera_station:
            break

    if not tempera_station:
        logger.warning(
            "No valid tempera station found. Going back to device discovery."
        )
        raise RuntimeWarning

    return tempera_station


async def get_scan_order() -> bool:
    """
    Get the scan order from the web server.

    :return: True if the server requests a scan else False.
    """
    try:
        response = await make_request(
            "get",
            f"{shared.config['webserver_address']}/rasp/api/scan_order",
            auth=shared.header,
            params={"access_point_id": shared.config["access_point_id"]},
        )
    except requests.exceptions.ConnectionError:
        logger.error(
            "Couldn't get scan order. Couldn't establish a connection to the web server "
            f"at {shared.config['webserver_address']}."
        )
        return False

    return response["scan"]
