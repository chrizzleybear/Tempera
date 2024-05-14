import logging
from asyncio import TaskGroup
from typing import Dict, Any, Sequence, Tuple, Literal

import requests
from sqlalchemy import select
from sqlalchemy.orm import Session

from tempera.database.entities import TemperaStation, Measurement, TimeRecord
from tempera.utils import shared, make_request

logger = logging.getLogger(f"tempera.{__name__}")


# Explanation of the func(*, kwarg) notation i.e., how to force kwarg only input.
# https://www.youtube.com/watch?v=R8-oAqCgHag

DataType = Literal["TimeRecord", "Measurement"]


def _get_from_database(
    *, kind: DataType
) -> Tuple[Sequence[TimeRecord | Measurement], TemperaStation]:
    """

    :param kind:
    :return:
    """
    with Session(shared.db_engine) as session:
        tempera_station = session.scalars(
            select(TemperaStation).where(TemperaStation.id == shared.current_station_id)
        ).first()
        if not tempera_station:
            logger.critical(
                f"Currently connected station {shared.current_station_id} not found in the database. "
                "The station should have been saved upon first connection. Check the logs for possible errors."
            )
            raise ValueError

        result = None
        match kind:
            case "TimeRecord":
                result = session.scalars(
                    select(TimeRecord)
                    .where(TimeRecord.tempera_station_id == shared.current_station_id)
                    .order_by(TimeRecord.start.asc())
                ).all()
            case "Measurement":
                result = session.scalars(
                    select(Measurement)
                    .where(Measurement.tempera_station_id == shared.current_station_id)
                    .order_by(Measurement.timestamp.asc())
                ).all()
        if not result:
            logger.warning(
                f"No {kind}(s) found for station {shared.current_station_id}."
            )

    return result, tempera_station


def _build_payload(
    tempera_station: TemperaStation,
    data: Measurement | TimeRecord,
    *,
    kind: DataType,
) -> Dict[str, Any]:
    """

    :param tempera_station:
    :param data:
    :param kind:
    :return:
    """
    match kind:
        case "Measurement":
            return {
                "access_point_id": shared.config["access_point_id"],
                "tempera_station_id": tempera_station.id,
                # datetime is not serializable -> to string
                # web server expects 'T' separated date & time, not ' ' separated
                "timestamp": f"{data.timestamp}".replace(" ", "T"),
                "temperature": data.temperature,
                "irradiance": data.irradiance,
                "humidity": data.humidity,
                "nmvoc": data.nmvoc,
            }
        case "TimeRecord":
            return {
                "access_point_id": shared.config["access_point_id"],
                "tempera_station_id": tempera_station.id,
                "start": f"{data.start}".replace(" ", "T"),
                # ms / 1_000 = seconds (cast back to int because division turns the value automatically to
                # float)
                "duration": int(data.duration / 1_000),
                "mode": data.mode,
                "auto_update": data.auto_update,
            }


async def send_data(*, kind: DataType):
    """

    :param kind:
    :return:
    """
    match kind:
        case "Measurement":
            endpoint = "measurement"
        case "TimeRecord":
            endpoint = "time_record"
        case _:
            logger.critical(f"Can't handle data of the {kind} type.")
            raise ValueError

    data, tempera_station = _get_from_database(kind=kind)

    if not data:
        logger.warning(f"No measurements found for station {tempera_station.id}")

    payloads = [_build_payload(tempera_station, item, kind=kind) for item in data]

    logger.info(f"Sending {len(payloads)} {kind}(s).")
    try:
        async with TaskGroup() as tg:
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
    except* requests.exceptions.ConnectionError:
        logger.error(
            f"Failed to send data to the {endpoint} API endpoint. "
            "Couldn't establish a connection to the web server "
            f"at {shared.config['webserver_address']}."
        )
        raise ConnectionError from None

    _safe_delete_data(data, kind=kind)


def _safe_delete_data(
    data: Sequence[Measurement | TimeRecord], *, kind: DataType
) -> None:
    """

    :param data:
    :param kind:
    :return:
    """
    # Don't delete the most recent time record.
    # This is the only time record that isn't concluded (i.e., doesn't have an end).
    # Note: it doesn't matter whether auto update is True or False for this measurement.
    if kind == "TimeRecord":
        data = data[:-1]

    with Session(shared.db_engine) as session:
        [session.delete(item) for item in data]
        session.commit()


async def send_measurements_and_time_records():
    """
    Wrapper around :func:`~send_data` to send measurements and time records with one function call.

    :return:
    """
    await send_data(kind="Measurement")
    await send_data(kind="TimeRecord")
