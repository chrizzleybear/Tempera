import asyncio
import json
import logging.config

from bleak import BleakClient

from api.poster import post
from bleclient.device_connection import (
    discovery_loop,
    validate_stations,
    get_scan_order,
)
from bleclient.device_notification import (
    notify,
)

with open("logging_conf.json", "r") as config:
    logging.config.dictConfig(json.load(config))

logger = logging.getLogger("tempera")


DATA_COLLECTION_INTERVAL = 5
DATA_SENDING_INTERVAL = 2


# TODO: add retry
async def get_data(client: BleakClient) -> None:
    for service in client.services:
        if service.uuid == "180a":
            continue
        logger.info(
            f"Service[desc:{service.description};handle:{service.handle};uuid:{service.uuid}]"
        )
        for characteristic in service.characteristics:
            logger.info(
                f"Characteristics[desc:{characteristic.description};uuid:{characteristic.uuid}]"
            )

            await notify(client, characteristic.uuid)


# TODO: add retry
async def post_data(client: BleakClient) -> None:
    for service in client.services:
        for characteristic in service.characteristics:
            await post(client, characteristic.uuid)


# @retry()
async def main():
    tempera_station = await discovery_loop(check_characteristics=True)

    while True:
        # Check that the access point and tempera station are still approved by the web app server.
        await validate_stations([tempera_station], check_characteristics=False)

        async with BleakClient(tempera_station.address) as client:
            logger.debug(f"Connected to device {tempera_station.address}.")

            await get_data(client)
            await post_data(client)

        await asyncio.sleep(DATA_COLLECTION_INTERVAL)
        await asyncio.sleep(
            DATA_SENDING_INTERVAL - DATA_COLLECTION_INTERVAL
            if DATA_SENDING_INTERVAL > DATA_COLLECTION_INTERVAL
            else 0
        )

        # If the scan order is issued by the web app server, an error is thrown.
        # This will cause tenacity to execute the main function from the top again,
        # starting with device discovery. This is sort of a 'goto'.
        if get_scan_order():
            raise RuntimeError("Scan order received. Returning to device discovery.")


if __name__ == "__main__":
    asyncio.run(main())
