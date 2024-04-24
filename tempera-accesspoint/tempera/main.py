import asyncio
import json
import logging.config
from pathlib import Path

import sqlalchemy
from bleak import BleakClient

from bleclient.device_connection import (
    discovery_loop,
    validate_stations,
    get_scan_order,
)
from bleclient.etl import elapsed_time_handler

with open("logging_conf.json", "r") as config:
    logging.config.dictConfig(json.load(config))

logger = logging.getLogger("tempera")

DATA_COLLECTION_INTERVAL = 5
DATA_SENDING_INTERVAL = 2


async def start_engine() -> sqlalchemy.Engine:
    from sqlalchemy import create_engine

    database = f"sqlite:///{Path(__name__).parent.resolve()}/database/data.sqlite"
    if not Path(database).is_file():
        logger.critical(
            f"{database} is not a valid file path. No database engine can be created from it."
        )
        raise FileNotFoundError
    logger.info(f"Creating database engine from db file: {database}")
    return create_engine(database, echo=True)


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
        tempera_station = tg.create_task(discovery_loop())
        db_engine = tg.create_task(start_engine())

    tempera_station, db_engine = tempera_station.result(), db_engine.result()
    while True:
        async with asyncio.TaskGroup() as tg:
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

            await get_data(client, db_engine)
            await post_data(client)


if __name__ == "__main__":
    asyncio.run(main(), debug=False)
