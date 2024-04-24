import logging
from datetime import datetime, timezone, timedelta

import sqlalchemy
from sqlalchemy import select
from sqlalchemy.orm import Session

from database.entities import Mode, TimeRecord, TemperaStation

logger = logging.getLogger(f"tempera.{__name__}")


# Elapsed time is the only service which implements `notify` on the Arduino side and thus needs a handler.
async def elapsed_time_handler(
    _characteristic,
    data: bytearray,
    engine: sqlalchemy.Engine = None,
    station_id: str = None,
):
    work_mode_map = {
        2: Mode.DEEP_WORK,
        3: Mode.IN_MEETING,
        4: Mode.OUT_OF_OFFICE,
        5: Mode.AVAILABLE,
    }
    record_status_map = {0: True, 7: False}

    if not engine:
        logger.critical(
            "No database engine provided. No database operations will be possible."
        )
        raise RuntimeError
    elif not station_id:
        logger.error(
            "No station ID provided. Impossible to tell from which station the time records are coming from."
        )
        raise RuntimeError

    # TODO: check that we don't need unused flags
    logger.debug(f"Received bytes: {data}")
    flag = data[0]
    elapsed_time = int.from_bytes(data[1:7], byteorder="little")
    tss = data[7]
    offset = data[8]
    work_mode = work_mode_map[data[9]]
    auto_update = record_status_map[data[10]]

    # The auto_update flag tells if the update is triggered manually (button press) or if it is just a periodic
    # update send via notify. Only when auto_update=False a button press has taken place and a new record must be
    # created.
    # Records are always handled retroactively, meaning that when a button is pressed the previous record is
    # concluded and a new one started. The data of the previous one is sent to the access point.

    with Session(engine) as session:
        tempera_station = session.scalars(
            select(TemperaStation).where(TemperaStation.id == station_id)
        ).one()
        current_record = session.scalars(
            select(TimeRecord).order_by(TimeRecord.start.desc()).limit(1)
        ).one()

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
