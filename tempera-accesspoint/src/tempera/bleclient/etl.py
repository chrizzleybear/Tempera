import logging
from datetime import datetime, timezone, timedelta
from typing import List

import sqlalchemy.orm
from bleak import BleakClient, BleakGATTCharacteristic
from bleak.backends.service import BleakGATTService
from sqlalchemy import select
from sqlalchemy.orm import Session

from tempera.database.entities import Mode, TimeRecord, TemperaStation, Measurement
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

    :param session:
    :param tempera_station:
    :param elapsed_time:
    :param work_mode:
    :param auto_update:
    :return:
    """
    now = datetime.now(tz=timezone.utc)
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

    :param _characteristic:
    :param data:
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

    # The auto_update flag tells if the update is triggered manually (button press) or if it is just a periodic
    # update send via notify. Only when auto_update=False a button press has taken place and a new record must be
    # created.
    # Records are always handled retroactively, meaning that when a button is pressed the previous record is
    # concluded and a new one started. The data of the previous one is sent to the access point.

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
                # Time record with auto update == True but without corresponding open record in the database.
                # Note: only the very last record saved can even come in to question for an update, all others
                #       must already have been concluded.
                # Create a new time record as if auto update == False
                # (this corrects the mistake to throw fewer erros in the web server)
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


async def measurements_handler(
    client: BleakClient,
    characteristics: List[BleakGATTCharacteristic],
):
    """

    :param client:
    :param characteristics:
    """
    temperature, irradiance, humidity, nmvoc = None, None, None, None
    for characteristic in characteristics:
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
            raise RuntimeError

    if not temperature:
        logger.warning("Received no value for measurement: temperature.")
        raise ValueError
    elif not irradiance:
        logger.warning("Received no value for measurement: irradiance.")
        raise ValueError
    elif not humidity:
        logger.warning("Received no value for measurement: humidity.")
        raise ValueError
    elif not nmvoc:
        logger.warning("Received no value for measurement: nmvoc.")
        raise ValueError

    with Session(shared.db_engine) as session:
        tempera_station = session.scalars(
            select(TemperaStation).where(TemperaStation.id == shared.current_station_id)
        ).first()

        measurement = Measurement(
            tempera_station=tempera_station,
            timestamp=datetime.now(timezone.utc),
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

    :param provider:
    :param uuid:
    :return:
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
