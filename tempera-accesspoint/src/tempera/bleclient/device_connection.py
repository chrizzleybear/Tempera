import asyncio
import logging
from typing import List, Tuple

import bleak.exc
import requests
from bleak import BLEDevice, BleakScanner, BleakClient
from sqlalchemy import select
from sqlalchemy.orm import Session

from tempera.bleclient.etl import filter_uuid
from tempera.database.entities import TemperaStation
from tempera.utils import shared
from tempera.utils.request_utils import make_request

logger = logging.getLogger(f"tempera.{__name__}")


REQUIRED_SERVICES = ["180a", "183f", "181a"]
REQUIRED_CHARACTERISTICS = ["2a25", "2bf2", "2a6e", "2a77", "2a6f", "2bd3"]
SCANNING_TIMEOUT = 5


async def detection_callback(device, advertisement_data) -> None:
    """

    :param device:
    :param advertisement_data:
    :return:
    """
    logger.info(
        f"Device[name:{device.name};address:{device.address};signal_strength(RSSI):{advertisement_data.rssi};"
        f"ad_data:{advertisement_data}]"
    )


async def get_tempera_stations() -> List[BLEDevice] | None:
    """

    :return:
    """
    logger.info("Scanning for BLE devices...")
    scanner = BleakScanner(detection_callback)
    try:
        devices = await scanner.discover(timeout=SCANNING_TIMEOUT)
    except bleak.exc.BleakError:
        raise bleak.exc.BleakError from None

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
        # TODO: send error log to back end
        return None

    return tempera_stations


async def validate_station(
    tempera_station: BLEDevice, client: BleakClient
) -> BLEDevice | None:
    """
    Returns the first valid tempera station. Valid means that its ID corresponds to one stored in the webapp back end.

    :param client:
    :param tempera_station:
    :return:
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
            "Request failed. Couldn't establish a connection to the web server "
            f"at {shared.config['webserver_address']}."
        )
        raise ConnectionError from None

    if not response["access_point_allowed"]:
        logger.warning(
            "This access point is not registered in the web app server. It can't transmit any data."
        )
        raise RuntimeError

    allowed_stations = response["stations_allowed"]

    async with asyncio.TaskGroup() as tg:
        id_ok = tg.create_task(validate_id(client, allowed_stations))
        missing_characteristics = tg.create_task(validate_characteristics(client))

    id_ok, station_id = id_ok.result()
    missing_characteristics = missing_characteristics.result()

    if id_ok and not missing_characteristics:
        await save_station(station_id)
        logger.info(
            f"Station[name: {tempera_station.name}; address: {tempera_station.address}] is valid."
        )
        shared.current_station_id = await get_station_id(client)
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

    :param client:
    :return:
    """
    device_info_service = await filter_uuid(client, "180a")
    serial_number_characteristic = await filter_uuid(device_info_service, "2a25")
    station_id = await client.read_gatt_char(serial_number_characteristic.uuid)
    return station_id.decode()


async def validate_id(client: BleakClient, valid_ids: List[str]) -> Tuple[bool, str]:
    """

    :param client:
    :param valid_ids:
    :return:
    """
    station_id = await get_station_id(client)
    if station_id == "":
        logger.error(f"No station ID found for station {client.address}")
        raise ValueError

    logger.info(f"Checking station ID '{station_id}' against web app server data.")

    return station_id in valid_ids, station_id


async def validate_characteristics(client: BleakClient) -> List[str]:
    """

    :param client:
    :return:
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

    :param station_id:
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


# Keep scanning for stations every 60 seconds until one is found.
# Usually a stop after n attempts would probably be better, but with headless raspberry pi
# this strategy might be preferable
# @retry(wait=wait_fixed(60))
async def discovery_loop() -> BLEDevice:
    """

    :return:
    """
    tempera_stations = await get_tempera_stations()

    if not tempera_stations:
        logger.error("No tempera stations found.")
        raise ValueError

    for station in tempera_stations:
        async with BleakClient(station) as client:
            tempera_station = await validate_station(station, client)

        if tempera_station:
            break

    if not tempera_station:
        logger.error("No tempera station found.")
        raise ValueError

    return tempera_station


async def get_scan_order() -> bool:
    """

    :return:
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
            "Request failed. Couldn't establish a connection to the web server "
            f"at {shared.config['webserver_address']}."
        )
        raise ConnectionError from None

    return response["scan"]
