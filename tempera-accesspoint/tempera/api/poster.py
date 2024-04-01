import json
from typing import NamedTuple

import requests

from utils.db_utils import get_measurements, delete_measurements


def temperature_data():
    data = get_measurements(sensor_id=1)
    return {"temperature data": data}


class UuidInfo(NamedTuple):
    sensor_id: int
    callback: callable
    endpoint: str


def map_uuid(uuid: str) -> UuidInfo:
    if uuid == "00002a6e-0000-1000-8000-00805f9b34fb":
        return UuidInfo(
            sensor_id=1,
            callback=temperature_data(),
            endpoint="https://dz325.wiremockapi.cloud/temperature",
        )


async def post(uuid: str):
    measurement = map_uuid(uuid)
    body = measurement.callback
    print(body)
    rc = requests.post(measurement.endpoint, data=json.dumps(body))
    if rc.status_code == 200:
        print(rc.json())
        delete_measurements(sensor_id=measurement.sensor_id)
    else:
        print(rc.status_code)
