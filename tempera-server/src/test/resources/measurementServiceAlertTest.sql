DELETE FROM internal_record;
DELETE FROM external_record;
DELETE FROM measurement;
DELETE FROM alert;
DELETE FROM sensor;
DELETE FROM tempera_station;
DELETE FROM access_point;
DELETE FROM room;
DELETE FROM groupx_members;
DELETE FROM groupx_project_object_contributors;
DELETE FROM groupx_project_object;
DELETE FROM groupx;
DELETE FROM userx_userx_role;
DELETE FROM userx WHERE default_project_id Is Not NULL;
DELETE FROM project;
DELETE FROM userx;
DELETE FROM threshold;
DELETE FROM alert;
INSERT INTO userx
(enabled, default_project_id, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password)
VALUES
    (True, null, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-09T09:00:00', '2024-05-09T10:15:00', 'johndoe', 'johndoe', 'johndoe', 'admini@example.com', 'John', 'Doe', 'hashed_password456');

INSERT INTO room (room_id) VALUES ('room_10');

INSERT INTO access_point (is_healthy, enabled, id, room_room_id) VALUES (TRUE, TRUE, '111e4567-e89b-12d3-a456-426614174001', 'room_10');

INSERT INTO tempera_station
(is_healthy, enabled, access_point_id, user_username, id)
VALUES
    (TRUE, TRUE, '111e4567-e89b-12d3-a456-426614174001', 'johndoe', 'TEMP123');

-- realized this is not necessary for all the colleagues for the tests but we can use it later
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('TEMPERATURE', -10, 'TEMP123', 'CELSIUS');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('IRRADIANCE', -11, 'TEMP123', 'LUX');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('HUMIDITY', -12, 'TEMP123', 'PERCENT');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('NMVOC', -13, 'TEMP123', 'OHM');

-- fill in measurements for all the temperature sensors (also not necessary for HomeDataMapperTest but can be used later)
-- user of interest is johndoe (TEMP123)
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
    (100.0, -10, '2024-05-10T08:30:00', 'TEMP123');

-- fill in measurements for all the irradiance sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
    (100.0, -11, '2024-05-10T08:30:00', 'TEMP123');

-- fill in measurements for all the humidity sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
    (100.0, -12, '2024-05-10T08:30:00', 'TEMP123');

-- fill in measurements for all the nmvoc sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
    (100.0, -13, '2024-05-10T08:30:00', 'TEMP123');

INSERT INTO threshold (id, default_threshold, sensor_type, threshold_type, threshold_value, modification_id, tip_id) VALUES
   (-100, True, 'TEMPERATURE', 'UPPERBOUND_INFO', 50.0, null, null),                   -- no violation (since UPPERBOUND_WARNING is overruling info
    (-101, True, 'IRRADIANCE', 'UPPERBOUND_INFO', 50.0, null, null),                      -- violation irradiance upperbound_info
    (-102, True, 'HUMIDITY', 'UPPERBOUND_INFO', 200.0, null, null),
    (-103, True, 'NMVOC', 'UPPERBOUND_INFO', 200.0, null, null),
    (-104, True, 'TEMPERATURE', 'LOWERBOUND_INFO', 0.0, null, null),
    (-105, True, 'IRRADIANCE', 'LOWERBOUND_INFO', 0.0, null, null),
    (-106, True, 'HUMIDITY', 'LOWERBOUND_INFO', 0.0, null, null),
    (-107, True, 'NMVOC', 'LOWERBOUND_INFO', 200.0, null, null),                          -- no violation  since LOWERBOUND_WARNING is overruling info
    (-108, True, 'TEMPERATURE', 'UPPERBOUND_WARNING', 0.0, null, null),            -- violation temp upperbound_warning
    (-109, True, 'IRRADIANCE', 'UPPERBOUND_WARNING', 2000.0, null, null),
    (-110, True, 'HUMIDITY', 'UPPERBOUND_WARNING', 1000.0, null, null),
    (-111, True, 'NMVOC', 'UPPERBOUND_WARNING', 200.0, null, null),
    (-112, True, 'TEMPERATURE', 'LOWERBOUND_WARNING', -10.0, null, null),
    (-113, True, 'IRRADIANCE', 'LOWERBOUND_WARNING', -10.0, null, null),
    (-114, True, 'HUMIDITY', 'LOWERBOUND_WARNING', -10.0, null, null),
    (115, True, 'NMVOC', 'LOWERBOUND_WARNING', 1000.0, null, null);                 -- violation nmvoc lowerbound_warning


    INSERT INTO room_thresholds (room_room_id, thresholds_id) VALUES
        ('room_10', -100),
        ('room_10', -101),
        ('room_10', -102),
        ('room_10', -103),
        ('room_10', -104),
        ('room_10', -105),
        ('room_10', -106),
        ('room_10', -107),
        ('room_10', -108),
        ('room_10', -109),
        ('room_10', -110),
        ('room_10', -111),
        ('room_10', -112),
        ('room_10', -113),
        ('room_10', -114),
        ('room_10', 115);
