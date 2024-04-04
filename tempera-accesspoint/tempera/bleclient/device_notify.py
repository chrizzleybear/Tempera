import datetime
import struct

from bleak import BleakClient

from utils.db_utils import save_measurement


def detection_callback(device, advertisement_data):
    if "G4T1" in device.name:
        print(
            f"{device.name} with address {device.address}. Signal strenght (RSSI) {advertisement_data.rssi}. Ad data: {advertisement_data}"
        )


def battery_notify_handler(_characteristic, data: bytes) -> None:
    print(f"saving: {_characteristic}")
    save_measurement(
        measurement_id=None,
        sensor_id=0,
        value=struct.unpack("<f", data)[0],
        timestamp=datetime.datetime.now(),
    )


def temperature_notify_handler(_characteristic, data: bytes) -> None:
    print(f"saving: {_characteristic}")
    save_measurement(
        measurement_id=None,
        sensor_id=1,
        value=struct.unpack("<f", data)[0],
        timestamp=datetime.datetime.now(),
    )


# maps each uuid to its respective handler
uuid_mapper = {
    # "00002a19-0000-1000-8000-00805f9b34fb": battery_notify_handler,
    "00002a6e-0000-1000-8000-00805f9b34fb": temperature_notify_handler
}


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
