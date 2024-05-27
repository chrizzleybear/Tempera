import logging
from datetime import datetime, timedelta
from typing import List

import bleak.exc
import pytz
import sqlalchemy.orm
from bleak import BleakClient, BleakGATTCharacteristic
from bleak.backends.service import BleakGATTService
from sqlalchemy import select
from sqlalchemy.orm import Session
from tenacity import retry, retry_if_exception_type, stop_after_attempt, wait_fixed

from tempera.database.entities import Mode, TimeRecord, TemperaStation, Measurement
from tempera.exceptions import BluetoothConnectionLostException
from tempera.utils import shared

logger = logging.getLogger(f"tempera.{__name__}")


def _create_time_record(
    session: sqlalchemy.orm.Session,
    tempera_station: TemperaStation,
    elapsed_time: int,
    work_mode: Mode,
    auto_update: bool,
) -> TimeRecord:
    """
    Utility function to create a time record from the required parameters and save it to the database.

    :param session: the database session for saving to db.
    :param tempera_station: the tempera station where the time record came from.
    :param elapsed_time: the elapsed time.
    :param work_mode: the work mode.
    :param auto_update: whether the time recod was update automatically.
    :return: the saved time record.
    """
    now = datetime.now(tz=pytz.timezone("Europe/Vienna"))
    start_time = now - timedelta(milliseconds=elapsed_time)
    record = TimeRecord(
        tempera_station=tempera_station,
        start=start_time,
        duration=elapsed_time,
        mode=work_mode,
        auto_update=auto_update,
    )
    session.add(record)

    return record


# Elapsed time is the only service which implements `notify` on the Arduino side and thus needs a handler.
async def elapsed_time_handler(
    _characteristic,
    data: bytearray,
):
    """
    Handler for the data sent by the tempera station as notification. Creates, updates and saves time records
    based on the data sent by the tempera station. Uses :func:`_create_time_record` under the hood.

    Handles the following scenarios:

    * The time record exists already (i.e., a time record with the same start time exists in the database) and the
      incoming one is set to auto update. => The existing time record is updated by adding the duration of the
      incoming one.
    * The time record doesn't exist in the database but the incoming one has auto update set to true. => Create a new
      time record with auto update = False and issue a warning.
    * The time record exists already and the incoming one has auto update = False. => Update the existing
      time record and set auto update = False thereby concluding it.
    * The time record doesn't exist and the incoming one has auto update = False => Save the new time record.

    *auto update* are periodic updates sent as notifications. Only time records triggered by button press have
    auto update = False.

    :param _characteristic: characteristic of the notification.
    :param data: binary data containing the information.
    """
    work_mode_map = {
        2: Mode.DEEP_WORK,
        3: Mode.IN_MEETING,
        4: Mode.OUT_OF_OFFICE,
        5: Mode.AVAILABLE,
    }
    record_status_map = {0: True, 7: False}

    logger.debug(f"Received bytes: {data}")
    _flag = data[0]
    elapsed_time = int.from_bytes(data[1:7], byteorder="little")
    _tss = data[7]
    _offset = data[8]
    work_mode = work_mode_map[data[9]]
    auto_update = record_status_map[data[10]]
    logger.debug(
        f"Read -> flag: {_flag}, elapsed_time: {elapsed_time}, tss: {_tss}, offset: {_offset}, work_mode: {work_mode}, "
        f"auto_update: {auto_update}"
    )

    with Session(shared.db_engine) as session:
        tempera_station = session.scalars(
            select(TemperaStation).where(TemperaStation.id == shared.current_station_id)
        ).first()
        current_record = session.scalars(
            select(TimeRecord).order_by(TimeRecord.start.desc()).limit(1)
        ).first()

        if auto_update:
            if current_record:
                # Update current time record
                logger.info(
                    f"Updating record: adding {elapsed_time} ms to the duration of {current_record}"
                )
                current_record.duration += elapsed_time
                current_record.auto_update = True
            elif not current_record:
                record = _create_time_record(
                    session, tempera_station, elapsed_time, work_mode, False
                )
                logger.warning(
                    "Received a time record with auto update == True "
                    "but no corresponding record was found to update. "
                    f"Creating new time record: {record} with auto update == False."
                )
        elif not auto_update:
            if current_record:
                # Close currently open time record before creating a new one
                current_record.auto_update = False
                logger.info(
                    f"Concluding the currently open time record: {current_record}."
                )

            # Create new time record
            record = _create_time_record(
                session, tempera_station, elapsed_time, work_mode, auto_update
            )
            logger.info(f"Creating new time record: {record}")

        session.commit()


@retry(
    retry=retry_if_exception_type(bleak.exc.BleakDBusError),
    wait=wait_fixed(10),
    stop=stop_after_attempt(10),
)
async def measurements_handler(
    client: BleakClient,
    characteristics: List[BleakGATTCharacteristic],
):
    """
    Read the measurement service characteristics from the device and create/save the resulting measurement to db.

    :param client: the connection to the tempera station.
    :param characteristics: list of measurement characteristics to read from the tempera station.
    :return: None
    :raises bleak.exc.BleakError: if connection issues occur when reading the characteristics from the tempera station.
    :raises bleak.exc.BleakDBusError: see BleakError
    """
    temperature, irradiance, humidity, nmvoc = None, None, None, None
    for characteristic in characteristics:
        try:
            if "2a6e" in characteristic.uuid:
                temperature = (
                    int.from_bytes(
                        await client.read_gatt_char(characteristic), byteorder="little"
                    )
                    / 100
                )
            elif "2a77" in characteristic.uuid:
                irradiance = (
                    int.from_bytes(
                        await client.read_gatt_char(characteristic), byteorder="little"
                    )
                    / 10
                )
            elif "2a6f" in characteristic.uuid:
                humidity = (
                    int.from_bytes(
                        await client.read_gatt_char(characteristic), byteorder="little"
                    )
                    / 100
                )
            elif "2bd3" in characteristic.uuid:
                nmvoc = float(
                    int.from_bytes(
                        await client.read_gatt_char(characteristic), byteorder="little"
                    )
                    * 100
                )
            else:
                logger.warning(
                    f"UUID {characteristic} doesn't match any supported "
                    "characteristic in the environmental sensing service."
                )
        except bleak.exc.BleakDBusError:
            logger.error("Failed to read measurement.")
            raise
        except bleak.exc.BleakError:
            logger.error("Connection to tempera station lost.")
            raise BluetoothConnectionLostException

    with Session(shared.db_engine) as session:
        tempera_station = session.scalars(
            select(TemperaStation).where(TemperaStation.id == shared.current_station_id)
        ).first()

        measurement = Measurement(
            tempera_station=tempera_station,
            timestamp=datetime.now(tz=pytz.timezone("Europe/Vienna")),
            temperature=temperature,
            irradiance=irradiance,
            humidity=humidity,
            nmvoc=nmvoc,
        )
        logger.info(f"Saving {measurement}.")

        session.add(measurement)
        session.commit()


async def filter_uuid(
    provider: BleakClient | BleakGATTService, uuid: str
) -> BleakGATTService | BleakGATTCharacteristic:
    """
    Find and return the complete UUID in a list of services or characteristics, given a substring of it.

    :param provider: the client (when searching services), else the service (when searching for characteristics).
    :param uuid: the UUID substring to find.
    :return: the service or characteristic that has the passed uuid as substring.
    """
    if isinstance(provider, BleakClient):
        return list(filter(lambda service: uuid in service.uuid, provider.services))[0]
    elif isinstance(provider, BleakGATTService):
        return list(
            filter(
                lambda characteristic: uuid in characteristic.uuid,
                provider.characteristics,
            )
        )[0]
