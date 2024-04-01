import json

import requests

from utils.db_utils import get_measurements, delete_measurements


def battery_data():
    data = get_measurements(sensor_id=0)
    return {"battery_levels": data}


uuid_mapper = {"00002a19-0000-1000-8000-00805f9b34fb": battery_data()}
rest_mapper = {
    "00002a19-0000-1000-8000-00805f9b34fb": "http://127.0.0.1:8000/temperature/"
}


async def post(uuid: str):
    body = uuid_mapper[uuid]
    rc = requests.post(rest_mapper[uuid], data=json.dumps(body))
    if rc.status_code == 200:
        print("Success!")
        delete_measurements(sensor_id=0)
    else:
        print(rc.status_code)
