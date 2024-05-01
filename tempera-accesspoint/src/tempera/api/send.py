import asyncio
import logging
from typing import Dict, Any, Sequence, Tuple, Literal

import requests
import sqlalchemy.orm
from sqlalchemy import select
from sqlalchemy.orm import Session

from tempera.database.entities import TemperaStation, Measurement, TimeRecord
from tempera.utils import shared, make_request

logger = logging.getLogger(f"tempera.{__name__}")


async def send_measurements():
    with Session(shared.db_engine) as session:
        measurements, tempera_station = get_from_database(session, "Measurement")

        if not measurements:
            logger.warning(f"No measurements found for station {tempera_station.id}")

        payload = build_measurements_payload(measurements, tempera_station)

        try:
            logger.info(f"Sending {len(measurements)} measurement(s).")
            await make_request(
                "post",
                f"{shared.config['webserver_address']}/rasp/api/measurements",
                auth=shared.header,
                json=payload,
            )
        except requests.ConnectionError:
            logger.error(
                "Request failed. Couldn't establish a connection to the web server."
            )
            raise ConnectionError

        delete_measurements(session, measurements)


def get_tempera_station(session: sqlalchemy.orm.Session) -> TemperaStation:
    tempera_station = session.scalars(
        select(TemperaStation).where(TemperaStation.id == shared.current_station_id)
    ).first()
    if not tempera_station:
        logger.critical(
            f"Currently connected station {shared.current_station_id} not found in the database. "
            "The station should have been saved upon first connection. Check the logs for possible errors."
        )
        raise ValueError

    return tempera_station


def build_measurements_payload(
    measurements: Sequence[Measurement],
    tempera_station: TemperaStation,
) -> Dict[str, Any]:
    return {
        "access_point_id": shared.config["access_point_id"],
        "measurements": [
            {
                "tempera_station_id": tempera_station.id,
                "timestamp": f"{measurement.timestamp}",  # datetime is not serializable -> to string
                "temperature": measurement.temperature,
                "irradiance": measurement.irradiance,
                "humidity": measurement.humidity,
                "nmvoc": measurement.nmvoc,
            }
            for measurement in measurements
        ],
    }


def delete_measurements(session, measurements: Sequence[Measurement]) -> None:
    [session.delete(measurement) for measurement in measurements]
    session.commit()


async def send_time_records() -> None:
    with Session(shared.db_engine) as session:
        time_records, tempera_station = get_from_database(session, "TimeRecord")

        payload = build_time_records_payload(time_records, tempera_station)

        try:
            logger.info(f"Sending time {len(time_records)} record(s).")
            await make_request(
                "post",
                f"{shared.config['webserver_address']}/rasp/api/time_records",
                auth=shared.header,
                json=payload,
            )
        except requests.ConnectionError:
            logger.error(
                "Request failed. Couldn't establish a connection to the web server."
            )
            raise ConnectionError

        delete_time_records(session, time_records)


def get_from_database(
    session: sqlalchemy.orm.Session, kind: Literal["TimeRecord", "Measurement"]
) -> Tuple[Sequence[TimeRecord | Measurement], TemperaStation]:
    tempera_station = get_tempera_station(session)

    result = None
    if kind == "TimeRecord":
        result = session.scalars(
            select(TimeRecord)
            .where(TimeRecord.tempera_station_id == shared.current_station_id)
            .order_by(TimeRecord.start.desc())
        ).all()
    elif kind == "Measurement":
        result = session.scalars(
            select(Measurement)
            .where(Measurement.tempera_station_id == shared.current_station_id)
            .order_by(Measurement.timestamp.desc())
        ).all()

    return result, tempera_station


# TODO: implement api schema and build payload function
def build_time_records_payload(
    time_records: Sequence[TimeRecord], tempera_station: TemperaStation
) -> Dict[str, Any]:
    return {
        "access_point_id": shared.config["access_point_id"],
        "time_records": [
            {
                "tempera_station_id": tempera_station.id,
                "start": f"{time_record.start}",
                # ms / 1_000 = seconds (cast back to int because division turns the value automatically to float)
                "duration": int(time_record.duration / 1_000),
                "mode": time_record.mode,
                "auto_update": time_record.auto_update,
            }
            for time_record in time_records
        ],
    }


def delete_time_records(
    session: sqlalchemy.orm.Session, time_records: Sequence[TimeRecord]
) -> None:
    [session.delete(time_record) for time_record in time_records]
    session.commit()


async def send_measurements_and_time_records():
    async with asyncio.TaskGroup() as tg:
        _ = tg.create_task(send_measurements())
        _ = tg.create_task(send_time_records())
