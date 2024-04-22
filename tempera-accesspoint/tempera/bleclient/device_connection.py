import json
import logging
from pathlib import Path
from typing import List

import requests
import yaml
from bleak import BLEDevice, BleakScanner, BleakClient
from fastapi import requests
from requests import Response
from tenacity import retry, wait_fixed

from bleclient.device_notification import detection_callback

logger = logging.getLogger(f"tempera.{__name__}")


API_ENDPOINT = "http://127.0.0.1:8000"
REQUIRED_SERVICES = ["180a", "183f", "181a"]
REQUIRED_CHARACTERISTICS = ["2a29", "2a25", "2bf2", "2a6e", "2a77", "2a6f", "2bd3"]
SCANNING_TIMEOUT = 5


async def get_tempera_stations() -> List[BLEDevice]:
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
        return []

    # TODO: send log to back end
    return tempera_stations


async def validate_stations(
    tempera_stations: List[BLEDevice], check_characteristics: bool = False
) -> BLEDevice | None:
    """
    Returns the first valid tempera station. Valid means that its ID corresponds to one stored in the webapp back end.

    :param check_characteristics:
    :param tempera_stations:
    :return:
    """
    logger.info(f"Trying to validate stations: {tempera_stations}")

    config_file = Path(__name__).resolve().parent.parent / "conf.yaml"
    if not config_file.is_file():
        logger.critical(
            f"{config_file.absolute()} not found. Couldn't get web app server IP, aborting validation."
        )
        raise FileNotFoundError
    logger.debug(f"Reading config file ({config_file.absolute()})")

    with open(config_file, "r") as config_file:
        config = yaml.safe_load(config_file)

    response = requests.get(
        f"{API_ENDPOINT}/rasp/api/{config['accesspoint_id']}",
        hooks={"response": validate_access_point},
    )

    response = json.loads(response.json())
    allowed_stations = response["stations_allowed"]

    is_valid = True
    for station in tempera_stations:
        # TODO: is the address or device serial number being set in the webapp?
        # Just return the first valid station (multi-station support not required for this project)
        if station.address in allowed_stations:
            if check_characteristics:
                async with BleakClient(
                    station.address, services=REQUIRED_SERVICES
                ) as client:

                    is_valid = _validate_station(station, client)

                if is_valid:
                    logger.info(
                        f"Station (Station[name: {station.name}; address: {station.address}]) meets all requirements: "
                    )

                    return station
            else:
                return station

    return None


def _validate_station(station: BLEDevice, client: BleakClient) -> bool:
    missing_characteristics = REQUIRED_CHARACTERISTICS
    for service in client.services:
        for required_characteristic in REQUIRED_CHARACTERISTICS:
            if required_characteristic in service.uuid:
                missing_characteristics.remove(required_characteristic)

    if missing_characteristics:
        logger.warning(
            f"Otherwise valid station ({station}) is missing "
            f"the following required characteristic '{missing_characteristics}'"
        )
        return False

    return True


# Keep scanning for stations every 60 seconds until one is found.
# Usually a stop after n attempts would probably be better, but with headless raspberry pi
# this strategy might be preferable
@retry(wait=wait_fixed(60))
async def discovery_loop(check_characteristics=False) -> BLEDevice:
    tempera_stations = await get_tempera_stations()
    if not tempera_stations:
        raise ValueError("No tempera stations found.")

    tempera_station = await validate_stations(
        tempera_stations, check_characteristics=check_characteristics
    )

    if not tempera_station:
        raise ValueError("No valid tempera station found.")

    return tempera_station


def validate_access_point(response: Response) -> None:
    code = response.status_code
    response = response.json()

    if code != 200:
        logger.debug(f"{response.status_code}, {response}")
        raise RuntimeError("An error occurred in calling the API")
    elif response["access_point_allowed"] != "True":
        logger.debug(f"{response.status_code}, {response}")
        raise RuntimeError("This access point is not registered in the web app server.")


def get_scan_order() -> bool:
    response = requests.get(f"{API_ENDPOINT}/rasp/api/scan_order")
    return response.json()["scan"]
