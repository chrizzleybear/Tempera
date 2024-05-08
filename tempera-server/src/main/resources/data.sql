INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Admin', 'Istrator', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'admin', 'admin', '2016-01-01 00:00:00');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Susi', 'Kaufgern', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user1', 'admin', '2016-01-01 00:00:00');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'MANAGER');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Max', 'Mustermann', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user2', 'admin', '2016-01-01 00:00:00');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user2', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Elvis', 'The King', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'elvis', 'elvis', '2016-01-01 00:00:00');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'ADMIN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'EMPLOYEE');
INSERT INTO TEMPERA_STATION (ENABLED, USER_USERNAME, ID) VALUES (TRUE, 'admin', 'tempera_station_1');
INSERT INTO TEMPERA_STATION (ENABLED, USER_USERNAME, ID) VALUES (FALSE, 'user2', 'tempera_station_disabled_2');
INSERT INTO TEMPERA_STATION (ENABLED, USER_USERNAME, ID) VALUES (FALSE, 'user1', 'tempera_station_disabled');
INSERT INTO TEMPERA_STATION (enabled, user_username, id) VALUES (FALSE, 'elvis', 'tempera_station_disabled_elvis');
INSERT INTO room (room_id) VALUES ('room_1');
INSERT INTO room (room_id) VALUES ('room_2');
INSERT INTO room(room_id) VALUES ('room_3');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '123e4567-e89b-12d3-a456-426614174001', 'room_1');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (FALSE, '456e4567-e89b-12d3-a456-426614174001', 'room_2');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '789e4567-e89b-12d3-a456-426614174001', 'room_3');
INSERT INTO access_point_tempera_stations (access_point_id, tempera_stations_id)  VALUES ('123e4567-e89b-12d3-a456-426614174001', 'tempera_station_1');
INSERT INTO access_point_tempera_stations (access_point_id, tempera_stations_id)  VALUES ('123e4567-e89b-12d3-a456-426614174001', 'tempera_station_disabled');
INSERT INTO access_point_tempera_stations (access_point_id, tempera_stations_id)  VALUES ('456e4567-e89b-12d3-a456-426614174001', 'tempera_station_disabled_2');
INSERT Into access_point_tempera_stations (access_point_id, tempera_stations_id)  VALUES ('789e4567-e89b-12d3-a456-426614174001', 'tempera_station_disabled_elvis');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_STATION_ID, UNIT) VALUES ('TEMPERATURE', -1, 'tempera_station_1', 'CELSIUS');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_STATION_ID, UNIT) VALUES ('IRRADIANCE', -2, 'tempera_station_1', 'LUX');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_STATION_ID, UNIT) VALUES ('HUMIDITY', -3, 'tempera_station_1', 'PERCENT');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_STATION_ID, UNIT) VALUES ('NMVOC', -4, 'tempera_station_1', 'OHM');
INSERT INTO measurement (measurement_value, id, sensor_sensor_id, timestamp, sensor_tempera_station_id)  VALUES (20.0, -1, -1, '2016-01-01 00:00:00', 'tempera_station_1');
-- Testdata for TimeRecordService
INSERT INTO external_record (duration, start, time_end, user_username, state) VALUES (30, '2016-01-01 00:00:00', null, 'admin', 'DEEPWORK');
INSERT INTO internal_record (groupx_id, id, project_id, start, time_end) VALUES (null, -1, null, '2016-01-01 00:00:00', null);
INSERT INTO external_record_internal_records (internal_records_id, external_record_start, external_record_user_username) VALUES (-1, '2016-01-01 00:00:00', 'admin');



