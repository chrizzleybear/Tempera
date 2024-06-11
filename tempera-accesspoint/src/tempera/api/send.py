import logging
import sys
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
    Gets all measurements or time records associated to the currently connected tempera station in an
    ascending time-ordered fashion.

    :param kind: what kind of data (measurement or time record) to fetch from the database.
    :return: an ascending time-ordered list of measurements or time records and the associated tempera station.
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
            sys.exit(0)

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
) -> Dict[str, Any]:
    """
    Builds the payload to send to the web server out of a measurement or time record.

    :param tempera_station: The tempera station from whence the measurement/time record comes from.
    :param data: the measurement/time record to send
    :return: the payload as a dictionary to be sent as a json kwarg in a HTTP request.
    """
    if isinstance(data, Measurement):
        return {
            "access_point_id": shared.config["access_point_id"],
            "tempera_station_id": tempera_station.id,
            # datetime is not serializable -> to string
            # web server expects 'T' separated date & time, not ' ' separated
            # web server needs precision no higher than tenths of a second
            "timestamp": data.timestamp.strftime("%Y-%m-%dT%H:%M:%S.%f")[:-5],
            "temperature": data.temperature,
            "irradiance": data.irradiance,
            "humidity": data.humidity,
            "nmvoc": data.nmvoc,
        }
    elif isinstance(data, TimeRecord):
        return {
            "access_point_id": shared.config["access_point_id"],
            "tempera_station_id": tempera_station.id,
            "start": data.start.strftime("%Y-%m-%dT%H:%M:%S.%f")[:-5],
            # ms / 1_000 = seconds (cast back to int because division turns the value automatically to
            # float)
            "duration": int(data.duration / 1_000),
            "mode": data.mode,
            "auto_update": data.auto_update,
        }


async def send_data(*, kind: DataType) -> None:
    """
    Retrieves all measurements or time records from database and asynchronously sends them to the web server.

    :param kind: type of data to send (measurement or time record).
    :return: None
    :raises requests.exceptions.ConnectionError: if the web server can't be reached
    """
    match kind:
        case "Measurement":
            endpoint = "measurement"
        case "TimeRecord":
            endpoint = "time_record"
        case _:
            logger.critical(f"Can't handle data of the {kind} type.")
            sys.exit(0)

    data, tempera_station = _get_from_database(kind=kind)

    payloads = [_build_payload(tempera_station, item) for item in data]

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
        raise requests.exceptions.ConnectionError

    _safe_delete_data(data)


def _safe_delete_data(data: Sequence[Measurement | TimeRecord]) -> None:
    """
    Delete all items of the sequence if they are of type measurement. If the items are of type time record,
    the last one is always kept, as it is the only one that isn't concluded yet, meaning that it could be
    updated any time with an auto update.

    :param data: sequence of measurements or time records to send
    :return: None
    """
    if len(data) > 0 and isinstance(data[0], TimeRecord):
        data = data[:-1]

    with Session(shared.db_engine) as session:
        [session.delete(item) for item in data]
        session.commit()


async def send_measurements_and_time_records() -> None:
    """
    Wrapper around :func:`~send_data` to send measurements and time records with one function call.

    :return: None
    """
    for kind in ["Measurement", "TimeRecord"]:
        try:
            await send_data(kind=kind)
        except requests.exceptions.ConnectionError:
            pass
