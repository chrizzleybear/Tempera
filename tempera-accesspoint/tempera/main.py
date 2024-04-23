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
        if "180a" in service.uuid or "1801" in service.uuid:
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
    tempera_station = await discovery_loop()

    while True:
        with asyncio.TaskGroup() as tg:
            # Check that the access point and tempera station are still approved by the web app server.
            valid_station = tg.create_task(validate_stations([tempera_station]))

            # If the scan order is issued by the web app server, an error is thrown.
            # This will cause tenacity to execute the main function from the top again,
            # starting with device discovery. This is sort of a 'goto'.
            scan_order = tg.create_task(get_scan_order())

            tg.sleep(DATA_COLLECTION_INTERVAL)

        tempera_station = valid_station.result()
        if scan_order.result():
            logger.info("Scan order received. Returning to device discovery.")
            raise RuntimeError

        async with BleakClient(tempera_station.address) as client:
            logger.debug(f"Connected to device {tempera_station.address}.")

            await get_data(client)
            await post_data(client)


if __name__ == "__main__":
    asyncio.run(main(), debug=False)
