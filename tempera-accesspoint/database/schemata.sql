CREATE TABLE station
(
    station_id INTEGER,
    -- sqlite doesn't have booleans -> 0 = false ; 1 = true
    -- TRUE & FALSE can still be used
    enabled    INTEGER,
    CONSTRAINT station_pk PRIMARY KEY (station_id)
);

CREATE TABLE sensor
(
    sensor_id  INTEGER,
    type       TEXT,
    station_id INTEGER,
    CONSTRAINT station_fk FOREIGN KEY (station_id) REFERENCES station (station_id),
    CONSTRAINT sensor_pk PRIMARY KEY (sensor_id)
);

CREATE TABLE measurement
(
    measurement_id INTEGER,
    sensor_id      INTEGER,
    value          REAL, -- a.k.a. float
    -- sqlite doesn't have dedicated date and/or time types
    -- from docs: TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
    timestamp      TEXT,
    CONSTRAINT sensor_fk FOREIGN KEY (sensor_id) REFERENCES sensor (sensor_id) ON DELETE CASCADE,
    CONSTRAINT measurement_pk PRIMARY KEY (measurement_id)
);

CREATE TABLE timerecord
(
    timerecord_id INTEGER,
    mode          TEXT,
    start_time    TEXT,
    end_time      TEXT,
    CONSTRAINT timerecord_pk PRIMARY KEY (timerecord_id)
);
