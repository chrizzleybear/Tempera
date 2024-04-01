import datetime
import struct

from bleak import BleakClient

from utils.db_utils import save_measurement


def battery_notify_handler(_characteristic, data: bytes) -> None:
    save_measurement(
        measurement_id=None,
        sensor_id=0,
        value=struct.unpack("<f", data)[0],
        timestamp=datetime.datetime.now(),
    )


# maps each uuid to its respective handler
uuid_mapper = {"00002a19-0000-1000-8000-00805f9b34fb": battery_notify_handler}


async def start_notify(client: BleakClient, uuid: str) -> None:
    """
    Starts the notification to the server and maps the uuid to the respective handler

    :param client:
    :param uuid:
    :return:
    """
    await client.start_notify(uuid, uuid_mapper[uuid])


def stop_notify(client: BleakClient) -> None:
    """
    Stop all notifiers

    :param client:
    :return:
    """
    for key in uuid_mapper.keys():
        client.stop_notify(key)
