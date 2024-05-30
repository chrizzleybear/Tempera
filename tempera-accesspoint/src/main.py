import asyncio
import logging.config
import sys

import bleak
import requests.exceptions
from bleak import BleakClient
from tenacity import retry, wait_fixed, retry_if_exception_type

from logging_conf import config
from tempera import bleclient, utils, api
from tempera.exceptions import BluetoothOffException, BluetoothConnectionLostException
from tempera.utils import make_request, shared

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


async def send_data() -> None:
    await api.send_measurements_and_time_records()


@retry(
    retry=retry_if_exception_type(requests.exceptions.ConnectionError),
    wait=wait_fixed(10),
)
async def send_connection_status(status: bool) -> None:
    """

    :param status:
    :return:
    """
    logger.info(f"Sending tempera station connection status={status} to web server.")
    try:
        await make_request(
            "put",
            f"{shared.config['webserver_address']}/rasp/api/tempera_status",
            auth=shared.header,
            params={
                "access_point_id": shared.config["access_point_id"],
                "station_id": shared.current_station_id,
            },
            json={
                "connection_status": status,
            },
        )
    except requests.exceptions.ConnectionError:
        logger.error(
            f"Failed to send tempera station connection status to web server at {shared.config['webserver_address']}"
        )
    except AttributeError:
        logger.error(
            "Can't connect to the web server. "
            "Since this station was never approved since the start of the program, the connection to it is refused. "
            "Returning to device discovery."
        )
        raise AttributeError


# Make the main function restart if the established bluetooth connection to a station is lost
# or a runtime error is encountered (these are raised throughout the package for minor errors
# which aren't critical for the functionality of the access point.)
@retry(
    retry=retry_if_exception_type(BluetoothConnectionLostException)
    | retry_if_exception_type(bleak.exc.BleakDBusError)
    | retry_if_exception_type(RuntimeError),
    wait=wait_fixed(10),
)
async def main():
    try:
        async with asyncio.TaskGroup() as tg:
            _ = tg.create_task(utils.init_globals())
            tempera_station = tg.create_task(bleclient.discovery_loop())
    # Use except* to catch a group (list) of the same exceptions. Must be used when catching exceptions from
    # the async task manager as multiple exceptions can be thrown because of the many concurrent tasks.
    # A simple catch wouldn't allow them all to be handled.
    except* BluetoothOffException:
        logger.critical("Bluetooth is off. Turn on Bluetooth and try again :)")
        sys.exit(0)
    except* RuntimeError:
        logger.info("Returning to device discovery.")
        raise

    tempera_station = tempera_station.result()
    async with BleakClient(tempera_station) as client:
        logger.info(f"Connected to device {tempera_station.address}.")
        logger.info(
            "Time records are received as notifications from the tempera station. "
            "Sensor measurements are polled and sent to the web server every "
            f"{utils.shared.config['sending_interval']} seconds, as configured."
        )

        try:
            async with asyncio.TaskGroup() as tg:
                _send_status = tg.create_task(send_connection_status(status=True))
                _get_notifications = tg.create_task(get_notifications(client))
        except* AttributeError:
            raise RuntimeError

        while True:
            try:
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
            except* BluetoothConnectionLostException:
                await send_connection_status(status=False)
                logger.info("Falling back to device discovery.")
                raise BluetoothConnectionLostException from None
            except* bleak.exc.BleakDBusError:
                await send_connection_status(status=False)
                logger.info("Falling back to device discovery.")
                raise


if __name__ == "__main__":
    try:
        asyncio.run(main(), debug=False)
    except bleak.exc.BleakDBusError:
        logger.error("Connection failed. Retrying")
        asyncio.run(main(), debug=False)
