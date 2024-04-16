import logging
import struct
from datetime import datetime

from bleak import BleakClient

from utils.db_utils import save_measurement, SensorType

logger = logging.getLogger(f"tempera.{__name__}")


def detection_callback(device, advertisement_data):
    print(
        f"Device[name:{device.name};address:{device.address};signal_strenght(RSSI):{advertisement_data.rssi};"
        f"ad_data:{advertisement_data}]"
    )


def temperature_notify_handler(_characteristic, data: bytes) -> None:
    print(f"Temperature[val:{float(data)}]")
    # print(f"Temperature[val:{struct.unpack('<f',data)[0]}]")
    # save_measurement(
    #     measurement_id=None,
    #     sensor_id=1,
    #     value=struct.unpack("<f", data)[0],
    #     timestamp=datetime.datetime.now(),
    # )


def irradiance_notify_handler(_characteristic, data: bytes) -> None:
    print(f"Irradiance[val:{struct.unpack('<f',data)[0]}]")
    # save_measurement(
    #     measurement_id=None,
    #     sensor_id=2,
    #     value=struct.unpack("<f", data)[0],
    #     timestamp=datetime.datetime.now(),
    # )


def humidity_notify_handler(_characteristic, data: bytes) -> None:
    print(f"Humidity[val:{struct.unpack('<f',data)[0]}]")
    # save_measurement(
    #     measurement_id=None,
    #     sensor_id=3,
    #     value=struct.unpack("<f", data)[0],
    #     timestamp=datetime.datetime.now(),
    # )


def nmvoc_notify_handler(_characteristic, data: bytes) -> None:
    print(f"NMVOC[val:{struct.unpack('<f',data)[0]}]")
    # save_measurement(
    #     measurement_id=None,
    #     sensor_id=4,
    #     value=struct.unpack("<f", data)[0],
    #     timestamp=datetime.datetime.now(),
    # )


async def debug_handler(
    client: BleakClient, uuid: str, sensor_type: SensorType
) -> None:
    val = float(await client.read_gatt_char(uuid))
    print(f"Saving\t{sensor_type}[val:{val}]")
    save_measurement(
        station_address=client.address,
        sensor_type=sensor_type,
        value=val,
        timestamp=datetime.now(),
    )


def measurement_handler(_characteristic, data: bytes, sensor_id: int) -> None:
    pass


maps = {2: "DW", 3: "Meeting", 4: "OoO", 5: "Present"}
_m = {0: "Old", 7: "New"}


def timerecord_handler(_characteristic, data: bytes):
    print("\n")
    print(
        struct.unpack("<I", data[1:5])[0] / 1_000,
        # maps[data[9]],
        # _m[data[10]],
    )
    print("\n")


async def notify(client: BleakClient, uuid: str) -> None:
    """
    Starts the notification to the server and maps the uuid to the respective handler

    :param client:
    :param uuid:
    :return:
    """
    debug = False
    match debug:
        case True:
            if "2a6e" in uuid:
                await debug_handler(client, uuid, "Temperature")
            elif "2a77" in uuid:
                await debug_handler(client, uuid, "Irradiance")
            elif "2a6f" in uuid:
                await debug_handler(client, uuid, "Humidity")
            # elif "2bd3" in uuid:
            #     await debug_handler(client, uuid, "NMVOC")
            else:
                raise ValueError(f"Unknown UUID: {uuid} received as input.")
        case False:
            if "2a6e" in uuid:
                await client.start_notify(uuid, temperature_notify_handler)
            elif "2a77" in uuid:
                await client.start_notify(uuid, irradiance_notify_handler)
            elif "2a6f" in uuid:
                await client.start_notify(uuid, humidity_notify_handler)
            elif "2bd3" in uuid:
                await client.start_notify(uuid, nmvoc_notify_handler)
            elif "2bf2" in uuid:
                await client.start_notify(uuid, timerecord_handler)
            # else:
            #     # raise ValueError(f"Unknown UUID: {uuid} received as input.")
            #     print(f"Unknown UUID: {uuid} received as input.")
