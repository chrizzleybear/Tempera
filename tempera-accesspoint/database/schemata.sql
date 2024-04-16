CREATE TABLE station
(
    address     TEXT,
    name        TEXT,
    description TEXT,
    enabled     INT,
    CONSTRAINT station_pk PRIMARY KEY (address)
);

CREATE TABLE sensor
(
    type            TEXT,
    station_address TEXT,
    CONSTRAINT sensors_station_fk FOREIGN KEY (station_address) REFERENCES station (address),
    CONSTRAINT sensor_pk PRIMARY KEY (type, station_address)
);

CREATE TABLE measurement
(
    station_address TEXT,
    sensor_type     TEXT,
    value           REAL,
    timestamp       TEXT,
    CONSTRAINT measurements_station_fk FOREIGN KEY (station_address) REFERENCES station (address) ON DELETE CASCADE,
    CONSTRAINT measurements_sensor_fk FOREIGN KEY (sensor_type) REFERENCES sensor (type) ON DELETE CASCADE,
    CONSTRAINT measurement_pk PRIMARY KEY (station_address, sensor_type, timestamp)
);

CREATE TABLE time_record
(
    station_address TEXT,
    mode            TEXT,
    start_time      TEXT,
    end_time        TEXT,
    CONSTRAINT time_records_station_fk FOREIGN KEY (station_address) REFERENCES station (address) ON DELETE CASCADE,
    CONSTRAINT time_record_pk PRIMARY KEY (station_address, start_time)
);
