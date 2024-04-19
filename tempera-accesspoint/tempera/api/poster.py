import logging
import sys
from typing import Literal

import requests
from bleak import BleakClient

from utils.db_utils import get_measurements, delete_measurements

logger = logging.getLogger(f"tempera.{__name__}")


MEASUREMENT_END_POINT = "http://127.0.0.1:8000/api/measurement"


def _map_uuid(client: BleakClient, uuid: str, action: Literal["delete", "fetch"]):
    if "2a6e" in uuid:
        match action:
            case "fetch":
                return {
                    "type": "Temperature",
                    "measurements": get_measurements(client.address, "Temperature"),
                }
            case "delete":
                print("Deleting sent Temperature measurements.")
                delete_measurements(client.address, "Temperature")
    elif "2a77" in uuid:
        match action:
            case "fetch":
                return {
                    "type": "Irradiance",
                    "measurements": get_measurements(client.address, "Irradiance"),
                }
            case "delete":
                print("Deleting sent Irradiance measurements.")
                delete_measurements(client.address, "Irradiance")
    elif "2a6f" in uuid:
        match action:
            case "fetch":
                return {
                    "type": "Humidity",
                    "measurements": get_measurements(client.address, "Humidity"),
                }
            case "delete":
                print("Deleting sent Humidity measurements.")
                delete_measurements(client.address, "Humidity")
    elif "2bd3" in uuid:
        match action:
            case "fetch":
                return {
                    "type": "NMVOC",
                    "measurements": get_measurements(client.address, "NMVOC"),
                }
            case "delete":
                print("Deleting sent NMVOC measurements.")
                delete_measurements(client.address, "NMVOC")
    else:
        raise ValueError(f"Unknown UUID: {uuid} received as input.")


async def post(client: BleakClient, uuid: str):
    body = _map_uuid(client, uuid, "fetch")
    print(body)
    try:
        rc = requests.post(MEASUREMENT_END_POINT, json=body)
    except requests.exceptions.ConnectionError:
        sys.exit("Failed to connect to the api.")
    match rc.status_code:
        case 201:
            print(rc.json())
            _map_uuid(client, uuid, "delete")
        case _:
            print(rc.status_code, rc.json())
