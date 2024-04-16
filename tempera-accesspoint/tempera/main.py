import asyncio
import logging.config
import sys
from typing import List

from bleak import BleakClient, BleakScanner, BLEDevice

from api.poster import post
from bleclient.device_notify import (
    detection_callback,
    notify,
)
from utils.db_utils import delete_measurements

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("tempera")


data_collection_interval = 5
data_sending_interval = 2


async def get_tempera_stations() -> List[BLEDevice]:
    logger.info("Scanning for BLE devices...")
    scanner = BleakScanner(detection_callback)
    devices = await scanner.discover(timeout=5)

    tempera_stations = []
    for device in devices:
        logger.info(f"Device[name:{device.name};address:{device.address}]")
        if "G4T1" in device.name:
            tempera_stations.append(device)

    logger.info(f"Tempera stations found {tempera_stations}")
    if not tempera_stations:
        sys.exit(
            "No devices found with 'G4T1' in their name.\n"
            "Make sure 'G4T1' is part of the tempera station's name you are trying to connect."
        )

    return tempera_stations


async def get_data(client: BleakClient) -> None:
    for service in client.services:
        logger.info(
            f"Service[desc:{service.description};handle:{service.handle};uuid:{service.uuid}]"
        )
        for characteristic in service.characteristics:
            logger.info(
                f"Characteristics[desc:{characteristic.description};uuid:{characteristic.uuid}]"
            )

            await notify(client, characteristic.uuid)


async def post_data(client: BleakClient) -> None:
    for service in client.services:
        for characteristic in service.characteristics:
            await post(client, characteristic.uuid)


async def main():
    delete_measurements(all=True)

    tempera_stations = await get_tempera_stations()

    # Project requirements postulate only one station be connected at one time to reduce complexity
    tempera_station = tempera_stations[0]
    while True:
        async with BleakClient(tempera_station.address, services=["181a"]) as client:
            print(f"Connected to device {tempera_station.address}.")

            await get_data(client)
            await post_data(client)

        await asyncio.sleep(data_collection_interval)
        await asyncio.sleep(
            data_sending_interval - data_collection_interval
            if data_sending_interval > data_collection_interval
            else 0
        )


if __name__ == "__main__":
    asyncio.run(main())
