import asyncio
import logging
from typing import Dict, Any, Sequence, Tuple, Literal

import sqlalchemy.orm
from sqlalchemy import select
from sqlalchemy.orm import Session

from tempera.database.entities import TemperaStation, Measurement, TimeRecord
from tempera.utils import shared, make_request

logger = logging.getLogger(f"tempera.{__name__}")


# Explanation of the func(*, kwarg) notation i.e., how to force kwarg only input.
# https://www.youtube.com/watch?v=R8-oAqCgHag

DataType = Literal["TimeRecord", "Measurement"]


def _get_tempera_station(session: sqlalchemy.orm.Session) -> TemperaStation:
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


def _get_from_database(
    session: sqlalchemy.orm.Session, *, kind: DataType
) -> Tuple[Sequence[TimeRecord | Measurement], TemperaStation]:
    tempera_station = _get_tempera_station(session)

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


def _build_payload(
    tempera_station: TemperaStation,
    data: Measurement | TimeRecord,
    *,
    kind: DataType,
) -> Dict[str, Any]:
    match kind:
        case "Measurement":
            return {
                "access_point_id": shared.config["access_point_id"],
                "tempera_station_id": tempera_station.id,
                "timestamp": f"{data.timestamp}",  # datetime is not serializable -> to string
                "temperature": data.temperature,
                "irradiance": data.irradiance,
                "humidity": data.humidity,
                "nmvoc": data.nmvoc,
            }
        case "TimeRecord":
            return {
                "access_point_id": shared.config["access_point_id"],
                "tempera_station_id": tempera_station.id,
                "start": f"{data.start}",
                # ms / 1_000 = seconds (cast back to int because division turns the value automatically to
                # float)
                "duration": int(data.duration / 1_000),
                "mode": data.mode,
                "auto_update": data.auto_update,
            }


async def send_data(*, kind: DataType):
    if kind not in ["Measurement", "TimeRecord"]:
        logger.critical(f"Can't handle data of the {kind} type.")
        raise ValueError

    with Session(shared.db_engine) as session:
        data, tempera_station = _get_from_database(session, kind=kind)

        if not data:
            logger.warning(f"No measurements found for station {tempera_station.id}")

        payloads = [_build_payload(tempera_station, item, kind=kind) for item in data]

        match kind:
            case "Measurement":
                endpoint = "measurements"
            case "TimeRecord":
                endpoint = "time_records"

        async with asyncio.TaskGroup() as tg:
            logger.info(f"Sending {len(data)} {kind}(s).")
            _ = [
                tg.create_task(
                    make_request(
                        "post",
                        f"{shared.config['webserver_address']}/rasp/api/{endpoint}",
                        auth=shared.header,
                        json=payload,
                    )
                )
                for payload in payloads
            ]

        _safe_delete_data(session, data, kind=kind)


def _safe_delete_data(
    session, data: Sequence[Measurement | TimeRecord], *, kind: DataType
) -> None:
    # Remove the time_record with auto_update == True from the list of those to be deleted
    # so that it keeps being updated at every etl cycle.
    # ETL should ensure that only the very last time_record can have auto_update == True
    if kind == "TimeRecord":
        data = list(filter(lambda time_record: not time_record.auto_update, data))

    [session.delete(item) for item in data]
    session.commit()


async def send_measurements_and_time_records():
    async with asyncio.TaskGroup() as tg:
        _ = tg.create_task(send_data(kind="Measurement"))
        _ = tg.create_task(send_data(kind="TimeRecord"))
