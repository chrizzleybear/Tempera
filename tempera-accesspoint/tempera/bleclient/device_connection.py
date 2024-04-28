import asyncio
import logging
from typing import List

from bleak import BLEDevice, BleakScanner, BleakClient

from bleclient.device_notification import detection_callback
from utils.config_utils import init_config, init_header
from utils.request_utils import make_request

logger = logging.getLogger(f"tempera.{__name__}")


CONFIG = init_config()
HEADER = init_header(CONFIG)
REQUIRED_SERVICES = ["180a", "183f", "181a"]
REQUIRED_CHARACTERISTICS = ["2a29", "2a25", "2bf2", "2a6e", "2a77", "2a6f", "2bd3"]
SCANNING_TIMEOUT = 5


async def get_tempera_stations() -> List[BLEDevice] | None:
    logger.info("Scanning for BLE devices...")
    scanner = BleakScanner(detection_callback)
    devices = await scanner.discover(timeout=SCANNING_TIMEOUT)
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
        # TODO: send log to back end
        return None

    # TODO: send log to back end
    return tempera_stations


async def validate_stations(tempera_stations: List[BLEDevice]) -> BLEDevice | None:
    """
    Returns the first valid tempera station. Valid means that its ID corresponds to one stored in the webapp back end.

    :param tempera_stations:
    :return:
    """
    logger.info(f"Trying to validate stations: {tempera_stations}")

    try:
        server_address = CONFIG["webserver_address"]
        access_point_id = CONFIG["access_point_id"]
    except KeyError as e:
        logger.critical(f"Failed to read parameter from the config file: {e}")
        raise KeyError

    response = await make_request(
        "get",
        f"{server_address}/rasp/api/valid_devices",
        auth=HEADER,
        params={"device_id": access_point_id},
    )
    if not response["access_point_allowed"]:
        logger.warning(
            "This access point is not registered in the web app server. It can't transmit any data."
        )
        raise RuntimeError

    allowed_stations = response["stations_allowed"]

    for station in tempera_stations:
        # Just return the first valid station (multi-station support not required for this project)
        async with BleakClient(station.address, services=REQUIRED_SERVICES) as client:

            async with asyncio.TaskGroup() as tg:
                id_ok = tg.create_task(validate_id(client, allowed_stations)).result()
                missing_characteristics = tg.create_task(
                    validate_characteristics(client)
                ).result()

        if id_ok and not missing_characteristics:
            logger.info(
                f"Connecting to station (Station[name: {station.name}; address: {station.address}]) "
                f"as it meets all requirements."
            )
            return station
        elif id_ok and missing_characteristics is not None:
            logger.info(
                f"Station (Station[name: {station.name}; address: {station.address}]) meets ID requirement but lacks"
                f" the following characteristics: {missing_characteristics}"
            )
        else:
            logger.info(
                f"Station (Station[name: {station.name}; address: {station.address}]) doesn't meet ID requirement! "
                f"Make sure you have registered this station's ID in the web app server if you want to connect to it."
            )

    logger.warning(
        "No tempera station found satisfies ID, services and characteristics validation."
    )
    raise RuntimeError


async def validate_id(client: BleakClient, valid_ids: List[str]):
    station_id = "Not Found"

    for service in client.services:
        if "180a" in service.uuid:
            for characteristic in service.characteristics:
                if "2a25" in characteristic.uuid:
                    station_id = await client.read_gatt_char(characteristic.uuid)
                    station_id = station_id.decode()

    logger.info(f"Checking station ID '{station_id}' against web app server data.")

    return station_id in valid_ids


async def validate_characteristics(client: BleakClient) -> List[str]:
    missing_characteristics = REQUIRED_CHARACTERISTICS
    for service in client.services:
        for characteristic in service.characteristics:
            for required_characteristic in REQUIRED_CHARACTERISTICS:
                if required_characteristic in characteristic.uuid:
                    missing_characteristics.remove(required_characteristic)

    return missing_characteristics


# Keep scanning for stations every 60 seconds until one is found.
# Usually a stop after n attempts would probably be better, but with headless raspberry pi
# this strategy might be preferable
# @retry(wait=wait_fixed(60))
async def discovery_loop() -> BLEDevice:
    tempera_stations = await get_tempera_stations()
    if not tempera_stations:
        logger.error("No tempera stations found.")
        raise ValueError

    tempera_station = await validate_stations(tempera_stations)

    if not tempera_station:
        logger.error("No tempera station found.")
        raise ValueError

    return tempera_station


async def get_scan_order() -> bool:
    try:
        server_address = CONFIG["webserver_address"]
    except KeyError as e:
        logger.critical(f"Failed to read parameter from the config file: {e}")
        raise KeyError

    response = await make_request(
        "get", f"{server_address}/rasp/api/scan_order", auth=HEADER
    )

    return response["scan"]
