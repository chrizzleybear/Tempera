import datetime
import json
from typing import Tuple

from tempera.db_utils import (
    query_time_record,
    replace_time_record,
    WorkMode,
    flush_time_records,
)


def get_mode_measurement() -> Tuple[WorkMode, datetime.datetime]:
    return "OUT-OF-OFFICE", datetime.datetime.now()


def send_time_record(
    concluded_record: Tuple[int, WorkMode, datetime.datetime, datetime.datetime]
) -> bool:
    record_id, mode, start, end = (
        concluded_record[0],
        concluded_record[1],
        concluded_record[2].isoformat(sep=" "),
        concluded_record[3].isoformat(sep=" "),
    )
    payload = {
        "record_id": record_id,
        "mode": mode,
        "start": start,
        "end": end,
    }
    try:
        # TODO: send data to back end api
        print(json.dumps(payload))
        return True
    except Exception:
        # TODO: exception handling upon failed transmission
        return False


def event_loop():
    # TODO: implement conf.yaml set data transmission interval (maybe with async)
    flush_time_records()

    current_id = 0
    # Initialization doesn't matter, gets immediately overwritten anyway.
    # Doing it just for the variable type to please the linter.
    current_start_time = datetime.datetime.now()

    while True:
        # TODO: get data from arduino
        # mode, time = get_mode_measurement()
        mode = input("input mode >> ")
        time = datetime.datetime.now()

        # This returns an empty list if the mode changes. The id matches but the mode doesn't.
        record = query_time_record(current_id, mode, current_start_time)
        if not record:
            # Don't send the very first time record when it is initialized
            if current_id != 0:
                concluded_record = query_time_record(
                    current_id, start=current_start_time
                )[-1]
                status = send_time_record(concluded_record)
                # delete time records after successful transmission
                if status:
                    flush_time_records()

            # init new entry
            current_id += 1
            current_start_time = time

        replace_time_record(current_id, mode, current_start_time, time)


if __name__ == "__main__":
    event_loop()
