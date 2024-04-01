import sqlite3
from datetime import datetime
from pathlib import Path
from typing import List, Literal, Any, Dict

SensorType = Literal["TEMPERATURE", "AIRQUALITY", "LIGHTINTENSITY", "HUMIDITY"]
WorkMode = Literal["AVAILABLE", "MEETING", "OUT-OF-OFFICE", "DEEPWORK"]


database_path = Path(__file__).parent.parent.parent / "database" / "data.sqlite"


def save_measurement(
    measurement_id: int | None, sensor_id: int, value: float, timestamp: datetime
) -> None:
    """
    If the measurement_id (primary key) is None it will be autogenerated from sqlite;
    it will usually take the value of the next biggest free id integer.

    :param measurement_id:
    :param sensor_id:
    :param value:
    :param timestamp:
    :return:
    """
    insert = "INSERT INTO measurement VALUES (:measurement_id, :sensor_id, :value, :timestamp)"
    timestamp = _adapt_date(timestamp)
    _execute_insert(
        insert,
        measurement_id=measurement_id,
        sensor_id=sensor_id,
        value=value,
        timestamp=timestamp,
    )


def get_measurements(
    sensor_id: int | None = None,
    timestamp: datetime | None = None,
    measurement_id: int | None = None,
    value: float | None = None,
) -> List[Dict[str, Any]]:
    kwargs = {
        "sensor_id": sensor_id,
        "timestamp": timestamp,
        "measurement_id": measurement_id,
        "value": value,
    }

    query = "SELECT * FROM measurement"
    query = _append_to_query(query, **kwargs)

    kwargs["timestamp"] = _adapt_date(timestamp)

    res = _execute_query(query, **kwargs)
    return [
        {
            "measurement_id": tup[0],
            "sensor_id": tup[1],
            "value": tup[2],
            "timestamp": tup[3],
        }
        for tup in res
    ]


def delete_measurements(
    sensor_id: int,
    timestamp: datetime | None = None,
    measurement_id: int | None = None,
    value: float | None = None,
) -> None:
    kwargs = {
        "sensor_id": sensor_id,
        "timestamp": timestamp,
        "measurement_id": measurement_id,
        "value": value,
    }

    query = "DELETE FROM measurement"
    query = _append_to_query(query, **kwargs)

    kwargs["timestamp"] = _adapt_date(timestamp)

    _execute_query(query, **kwargs)


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


def _append_to_query(query: str, **kwargs) -> str:
    if len(set(kwargs.values())) == 1 and None in set(kwargs.values()):
        return query
    for key in kwargs.keys():
        if not kwargs[key]:
            continue
        elif "WHERE" not in query:
            query += f" WHERE {key}=:{key}"
        else:
            query += f" AND {key}=:{key}"

    return query


def _adapt_date(date: datetime) -> str:
    if date:
        # Default separator is 'T' but sqlite saves a whitespace
        # returning false on comparisons of equal date times... kinda dumb if you ask me.
        # Microseconds not included (because they are unnecessary and because sqlite saves
        # .3d and turns to string but creating a datetime to fetch from the db creates the
        # datetime default .6d microseconds string meaning equal dates don't return True)
        # Ergo sqlite & datetime work very poorly and stupidly together.
        return date.isoformat(sep=" ", timespec="seconds")
