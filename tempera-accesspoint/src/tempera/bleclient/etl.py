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

        if not current_record:
            # Create the new time record
            record = _create_time_record(
                session, tempera_station, elapsed_time, work_mode, auto_update
            )
            logger.info(f"Creating time record: {record}.")
        else:
            if not auto_update:
                # Conclude the last time record
                current_record.auto_update = False
                logger.info(f"Concluding the previous time record: {current_record}.")

                # Create the new time record
                # auto_update is False forces the conclusion of the old measurement and the creation of a new one.
                # The new one should not be deleted after being sent, as the next auto update must add on to it.
                # Thus, auto update is set to True when it is created, even if it was not an automatic update.
                # (auto update is both determining the creation of a new time record, and whether existing ones
                # are deleted after being sent. From a software design perspective, it would be better to separate
                # the functionality into 2 variables to split the responsibility. It would make things a little
                # easier to understand but here a corner is being cut a little for convenience.)
                record = _create_time_record(
                    session, tempera_station, elapsed_time, work_mode, True
                )
                logger.info(f"Creating time record: {record}")

            elif auto_update:
                current_record.duration += elapsed_time
                # set auto_update to True, that way the initial auto_update = False is overwritten with the automatic
                # update
                current_record.auto_update = True
                logger.info(
                    f"Updating record: adding {elapsed_time} ms to the duration of {current_record}"
                )

        session.commit()


async def measurements_handler(
    client: BleakClient,
    characteristics: List[BleakGATTCharacteristic],
):
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
    if isinstance(provider, BleakClient):
        return list(filter(lambda service: uuid in service.uuid, provider.services))[0]
    elif isinstance(provider, BleakGATTService):
        return list(
            filter(
                lambda characteristic: uuid in characteristic.uuid,
                provider.characteristics,
            )
        )[0]
