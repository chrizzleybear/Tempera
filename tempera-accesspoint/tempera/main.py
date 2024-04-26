import asyncio
import json
import logging.config

import sqlalchemy
from bleak import BleakClient

import utils.shared as shared
from bleclient.device_connection import (
    discovery_loop,
    validate_stations,
    get_scan_order,
)
from bleclient.etl import elapsed_time_handler
from utils.shared import init_globals

with open("logging_conf.json", "r") as config:
    logging.config.dictConfig(json.load(config))

logger = logging.getLogger("tempera")

DATA_COLLECTION_INTERVAL = 5


# TODO: add retry
async def get_data(client: BleakClient, db_engine: sqlalchemy.Engine) -> None:
    elapsed_time_service = list(
        filter(lambda service: "183f" in service.uuid, client.services)
    )[0]
    elapsed_time_uuid = list(
        filter(
            lambda characteristic: "2bf2" in characteristic.uuid,
            elapsed_time_service.characteristics,
        )
    )[0]

    await client.start_notify(
        elapsed_time_uuid,
        elapsed_time_handler,
        engine=db_engine,
        station_id=client.address,
    )


# TODO: add retry
async def post_data(client: BleakClient) -> None:
    pass


# @retry()
async def main():
    async with asyncio.TaskGroup() as tg:
        _ = tg.create_task(init_globals())
        tempera_station = tg.create_task(discovery_loop())

    tempera_station = tempera_station.result()
    while True:
        async with asyncio.TaskGroup() as tg:
            # Check that the access point and tempera station are still approved by the web app server.
            valid_station = tg.create_task(validate_stations([tempera_station]))

            # If the scan order is issued by the web app server, an error is thrown.
            # This will cause tenacity to execute the main function from the top again,
            # starting with device discovery. This is sort of a 'goto'.
            scan_order = tg.create_task(get_scan_order())

            tg.create_task(asyncio.sleep(DATA_COLLECTION_INTERVAL))

        tempera_station = valid_station.result()
        if scan_order.result():
            logger.info("Scan order received. Returning to device discovery.")
            raise RuntimeError

        async with BleakClient(tempera_station.address) as client:
            logger.debug(f"Connected to device {tempera_station.address}.")

            await get_data(client, shared.db_engine)
            await post_data(client)


if __name__ == "__main__":
    asyncio.run(main(), debug=False)
