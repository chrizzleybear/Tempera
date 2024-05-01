import logging
from datetime import datetime, timezone, timedelta
from typing import List

from bleak import BleakClient, BleakGATTCharacteristic
from bleak.backends.service import BleakGATTService
from sqlalchemy import select
from sqlalchemy.orm import Session

from tempera.database.entities import Mode, TimeRecord, TemperaStation, Measurement
from tempera.utils import shared

logger = logging.getLogger(f"tempera.{__name__}")


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

    # TODO: check that we don't need unused flags
    logger.debug(f"Received bytes: {data}")
    flag = data[0]
    elapsed_time = int.from_bytes(data[1:7], byteorder="little")
    tss = data[7]
    offset = data[8]
    work_mode = work_mode_map[data[9]]
    auto_update = record_status_map[data[10]]
    logger.debug(
        f"Read -> flag: {flag}, elapsed_time: {elapsed_time}, tss: {tss}, offset: {offset}, work_mode: {work_mode}, "
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

        if not current_record or not auto_update:
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
            logger.info(f"Creating time record: {record}")
        elif auto_update:
            current_record.duration += elapsed_time
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
            temperature = float(await client.read_gatt_char(characteristic))
        elif "2a77" in characteristic.uuid:
            irradiance = float(await client.read_gatt_char(characteristic))
        elif "2a6f" in characteristic.uuid:
            humidity = float(await client.read_gatt_char(characteristic))
        elif "2bd3" in characteristic.uuid:
            nmvoc = float(await client.read_gatt_char(characteristic))
        else:
            logger.warning(
                f"UUID {characteristic} doesn't match any supported "
                "characteristic in the environmental sensing service."
            )
            raise RuntimeError

    # TODO: think if a value error is appropriate or if it is better to send the measurement even if one filed is None.
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
