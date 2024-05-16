import asyncio
import logging.config
import sys

import bleak.exc
from bleak import BleakClient

from logging_conf import config
from tempera import bleclient, utils, api

logging.config.dictConfig(config)
logger = logging.getLogger("tempera")


async def get_notifications(client: BleakClient) -> None:
    elapsed_time_service = await bleclient.filter_uuid(client, "183f")
    elapsed_time_uuid = await bleclient.filter_uuid(elapsed_time_service, "2bf2")

    await client.start_notify(
        elapsed_time_uuid,
        bleclient.elapsed_time_handler,
    )
    logger.info("Subscribed to time record notifications.")


async def get_measurements(client: BleakClient) -> None:
    measurement_service = await bleclient.filter_uuid(client, "181a")
    characteristics = ["2a6e", "2a77", "2a6f", "2bd3"]
    logger.info(
        f"Getting measurements for the following characteristics: {characteristics}."
    )
    uuids = []
    for uuid in characteristics:
        uuids.append(await bleclient.filter_uuid(measurement_service, uuid))

    await bleclient.measurements_handler(client, uuids)


# TODO: add retry
async def send_data() -> None:
    await api.send_measurements_and_time_records()


# @retry()
async def main():
    try:
        async with asyncio.TaskGroup() as tg:
            _ = tg.create_task(utils.init_globals())
            tempera_station = tg.create_task(bleclient.discovery_loop())
    except* bleak.exc.BleakError:
        logger.critical("Bluetooth is off. Turn on Bluetooth and try again :)")
        sys.exit(0)

    tempera_station = tempera_station.result()
    async with BleakClient(tempera_station) as client:
        logger.info(f"Connected to device {tempera_station.address}.")
        logger.info(
            "Time records are received as notifications from the tempera station. "
            "Sensor measurements are polled and sent to the web server every "
            f"{utils.shared.config['sending_interval']} seconds, as configured."
        )

        await get_notifications(client)

        while True:
            async with asyncio.TaskGroup() as tg:
                # Check that the access point and tempera station are still approved by the web app server.
                valid_station = tg.create_task(
                    bleclient.validate_station(tempera_station, client)
                )

                # If the scan order is issued by the web app server, an error is thrown.
                # This will cause tenacity to execute the main function from the top again,
                # starting with device discovery. This is sort of a 'goto'.
                scan_order = tg.create_task(bleclient.get_scan_order())

                _measurements = tg.create_task(get_measurements(client))

                # Sending interval is the timeout of data sending from the raspberry to the web server.
                # Here it is also used as the data collection interval for simplicity and convenience.
                _ = tg.create_task(
                    asyncio.sleep(utils.shared.config["sending_interval"])
                )

            valid_station = valid_station.result()
            if valid_station != tempera_station:
                logger.warning(f"{tempera_station} is no longer a valid station.")
                raise RuntimeError
            if scan_order.result():
                logger.info("Scan order received. Returning to device discovery.")
                raise RuntimeError

            await send_data()


if __name__ == "__main__":
    asyncio.run(main(), debug=False)
