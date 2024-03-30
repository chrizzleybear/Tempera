import datetime
import sqlite3
from pathlib import Path
from typing import List, Literal, Tuple, Any

SensorType = (Literal["TEMPERATURE", "AIRQUALITY", "LIGHTINTENSITY", "HUMIDITY"],)


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
    measurement_id: int, sensor_id: int, value: float, timestamp: datetime.datetime
) -> None:
    insert = "INSERT INTO measurement VALUES (:measurement_id, :sensor_id, :value, :timestamp)"
    timestamp = timestamp.isoformat()
    _execute_insert(
        insert,
        measurement_id=measurement_id,
        sensor_id=sensor_id,
        value=value,
        timestamp=timestamp,
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
    measurement_id: int, sensor_id: int, value: float, timestamp: datetime.datetime
) -> List[Tuple[int, int, float, datetime.datetime]]:
    query = (
        "SELECT * "
        "FROM measurement "
        "WHERE measurement_id=:measurement_id AND sensor_id=:sensor_id AND value=:value AND timestamp=:timestamp"
    )
    res = _execute_query(
        query,
        measurement_id=measurement_id,
        sensor_id=sensor_id,
        value=value,
        timestamp=timestamp,
    )
    return [
        (mes_id, sens_id, val, datetime.datetime.fromisoformat(timestmp))
        for mes_id, sens_id, val, timestmp in res
    ]


def _execute_query(query: str, **kwargs) -> List[Any]:
    con = sqlite3.connect(database_path)
    cur = con.cursor()

    res = cur.execute(query, kwargs)

    res = res.fetchall()
    if not res:
        raise ValueError(f"No entry found for query: {query} and args: {kwargs}")

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
