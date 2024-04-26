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
    get_station_id,
)
from utils.shared import init_globals
from bleclient.etl import elapsed_time_handler, filter_uuid, measurements_handler

with open("logging_conf.json", "r") as config:
    logging.config.dictConfig(json.load(config))

logger = logging.getLogger("tempera")

DATA_COLLECTION_INTERVAL = 5


async def get_notifications(client: BleakClient, db_engine: sqlalchemy.Engine) -> None:
    elapsed_time_service = await filter_uuid(client, "183f")
    elapsed_time_uuid = await filter_uuid(elapsed_time_service, "2bf2")

    station_id = await get_station_id(client)
    await client.start_notify(
        elapsed_time_uuid,
        elapsed_time_handler,
        engine=db_engine,
        station_id=station_id,
    )
    logger.info("Subscribed to time record notifications.")


async def get_measurements(client: BleakClient, db_engine: sqlalchemy.Engine) -> None:
    measurement_service = await filter_uuid(client, "181a")
    characteristics = ["2a6e", "2a77", "2a6f", "2bd3"]
    logger.info(
        f"Getting measurements for the following characteristics: {characteristics}."
    )
    uuids = []
    for uuid in characteristics:
        uuids.append(await filter_uuid(measurement_service, uuid))

    station_id = await get_station_id(client)
    await measurements_handler(client, uuids, db_engine, station_id)


# TODO: add retry
async def post_data(client: BleakClient) -> None:
    pass


# @retry()
async def main():
    async with asyncio.TaskGroup() as tg:
        _ = tg.create_task(init_globals())
        tempera_station = tg.create_task(discovery_loop())

    tempera_station = tempera_station.result()
    async with BleakClient(tempera_station) as client:
        logger.debug(f"Connected to device {tempera_station.address}.")

        await get_notifications(client, db_engine)

        while True:
            async with asyncio.TaskGroup() as tg:
                # Check that the access point and tempera station are still approved by the web app server.
                valid_station = tg.create_task(
                    validate_stations(tempera_station, client, db_engine)
                )

                # If the scan order is issued by the web app server, an error is thrown.
                # This will cause tenacity to execute the main function from the top again,
                # starting with device discovery. This is sort of a 'goto'.
                scan_order = tg.create_task(get_scan_order())

                measurements = tg.create_task(get_measurements(client, db_engine))

                _ = tg.create_task(asyncio.sleep(DATA_COLLECTION_INTERVAL))

            valid_station = valid_station.result()
            if valid_station != tempera_station:
                logger.warning(f"{tempera_station} is no longer a valid station.")
                raise RuntimeError
            if scan_order.result():
                logger.info("Scan order received. Returning to device discovery.")
                raise RuntimeError

            measurements.result()
            await post_data(client)


if __name__ == "__main__":
    asyncio.run(main(), debug=False)
