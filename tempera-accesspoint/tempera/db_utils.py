import sqlite3
from datetime import datetime
from pathlib import Path
from typing import List, Literal, Tuple, Any, Dict

SensorType = Literal["TEMPERATURE", "AIRQUALITY", "LIGHTINTENSITY", "HUMIDITY"]
WorkMode = Literal["AVAILABLE", "MEETING", "OUT-OF-OFFICE", "DEEPWORK"]


database_path = Path(__file__).parent.parent / "database" / "data.sqlite"


def insert_station(station_id: int, enabled: bool) -> None:
    insert = "INSERT INTO station VALUES (:station_id, :enabled)"
    enabled = 0 if not enabled else 1
    _execute_insert(insert, station_id=station_id, enabled=enabled)


def insert_sensor(
    sensor_id: int,
    sensor_type: SensorType,
    station_id: int,
) -> None:
    insert = "INSERT INTO sensor VALUES (:sensor_id, :sensor_type, :station_id)"
    _execute_insert(
        insert, sensor_id=sensor_id, sensor_type=sensor_type, station_id=station_id
    )


def insert_measurement(
    measurement_id: int, sensor_id: int, value: float, timestamp: datetime
) -> None:
    insert = "INSERT INTO measurement VALUES (:measurement_id, :sensor_id, :value, :timestamp)"
    timestamp = _adapt_date(timestamp)
    _execute_insert(
        insert,
        measurement_id=measurement_id,
        sensor_id=sensor_id,
        value=value,
        timestamp=timestamp,
    )


def insert_time_record(
    time_record_id: int, mode: WorkMode, start: datetime, end: datetime
):
    insert = "INSERT INTO timerecord VALUES (:time_record_id, :mode, :start, :end)"
    start, end = _adapt_date(start), _adapt_date(end)
    _execute_insert(
        insert, time_record_id=time_record_id, mode=mode, start=start, end=end
    )


def query_station(station_id: int, enabled: bool) -> List[Tuple[int, bool]]:
    query = "SELECT * FROM station WHERE station_id=:station_id AND enabled=:enabled"
    res = _execute_query(query, station_id=station_id, enabled=enabled)
    return [(id, True if enabled == 1 else 0) for id, enabled in res]


def query_sensor(
    sensor_id: int, sensor_type: SensorType, station_id: int
) -> List[Tuple[int, SensorType, int]]:
    query = "SELECT * FROM sensor WHERE sensor_id=:sensor_id AND type=:sensor_type AND station_id=:station_id"
    return _execute_query(
        query, sensor_id=sensor_id, sensor_type=sensor_type, station_id=station_id
    )


def query_measurement(
    measurement_id: int, sensor_id: int, value: float, timestamp: datetime
) -> List[Tuple[int, int, float, datetime]]:
    query = (
        "SELECT * "
        "FROM measurement "
        "WHERE measurement_id=:measurement_id AND sensor_id=:sensor_id AND value=:value AND timestamp=:timestamp"
    )
    timestamp = _adapt_date(timestamp)
    res = _execute_query(
        query,
        measurement_id=measurement_id,
        sensor_id=sensor_id,
        value=value,
        timestamp=timestamp,
    )
    return [
        (mes_id, sens_id, val, datetime.fromisoformat(timestmp))
        for mes_id, sens_id, val, timestmp in res
    ]


def query_time_record(
    time_record_id: int,
    mode: WorkMode = None,
    start: datetime = None,
    end: datetime = None,
) -> List[Tuple[int, WorkMode, datetime, datetime]]:
    start, end = _adapt_date(start), _adapt_date(end)
    params = {"time_record_id": time_record_id}
    query = "SELECT * FROM timerecord WHERE timerecord_id=:time_record_id"
    query, params = _append_to_query(query, " AND mode=:mode", mode, params, "mode")
    query, params = _append_to_query(
        query, " AND start_time=:start", start, params, "start"
    )
    query, params = _append_to_query(query, " AND end_time=:end", end, params, "end")
    res = _execute_query(query, **params)
    return [
        (
            time_record_id,
            mode,
            datetime.fromisoformat(start),
            datetime.fromisoformat(end),
        )
        for time_record_id, mode, start, end in res
    ]


def replace_time_record(tr_id: int, mode: str, start: datetime, end: datetime) -> None:
    con = sqlite3.connect(database_path)
    cur = con.cursor()

    delete_kwargs = {"time_record_id": tr_id}
    delete = "DELETE FROM timerecord WHERE timerecord_id=:time_record_id"
    cur.execute(delete, delete_kwargs)

    insert_kwargs = {
        "time_record_id": tr_id,
        "mode": mode,
        "start": start,
        "end": end,
    }
    insert = "INSERT INTO timerecord VALUES (:time_record_id, :mode, :start, :end)"
    cur.execute(insert, insert_kwargs)

    try:
        con.commit()
        con.close()
    except sqlite3.Error:
        con.rollback()
        con.close()
        raise sqlite3.Error("Failed to replace time record.")


def flush_time_records():
    con = sqlite3.connect(database_path)
    cur = con.cursor()
    cur.execute("DELETE FROM timerecord WHERE TRUE")
    try:
        con.commit()
        con.close()
    except sqlite3.Error:
        con.rollback()
        con.close()
        raise sqlite3.DatabaseError("Failed to flush the database.")


def _execute_query(query: str, **kwargs) -> List[Any]:
    con = sqlite3.connect(database_path)
    cur = con.cursor()

    res = cur.execute(query, kwargs)

    res = res.fetchall()

    return res


def _execute_insert(insert: str, **kwargs) -> None:
    con = sqlite3.connect(database_path)
    cur = con.cursor()

    cur.execute(insert, kwargs)
    try:
        con.commit()
        con.close()
    except sqlite3.DatabaseError:
        con.rollback()
        con.close()
        raise sqlite3.DatabaseError(
            f"No entry found for query: {insert} and args: {kwargs}"
        )


def _append_to_query(
    query: str, condition: str, var: Any, params: Dict[str, Any], name: str
) -> Tuple[str, Dict[str, Any]]:
    if var:
        params[name] = var
        return query + condition, params
    else:
        return query, params


def _adapt_date(date: datetime) -> str:
    if date:
        # default separator is 'T' but sqlite saves a whitespace
        # returning false on comparisons of equal date times... bummer if you ask me.
        return date.isoformat(sep=" ")
