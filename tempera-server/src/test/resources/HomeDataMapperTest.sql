-- add some Groups to test db
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (1,'elvis', 'this is just for testing', 'testGroup1');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (2,'admin', 'this is also just for testing', 'testGroup2');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (3,'admin', 'this is also just for testing', 'outsiderGroup');
-- add some more members to fill the groups

-- these are in the same groups as the user
INSERT INTO userx
(enabled, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password)
VALUES
    (TRUE, 'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'johndoe', 'johndoe@example.com', 'John', 'Doe', 'hashed_password123'),
    (FALSE, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-09T09:00:00', '2024-05-09T10:15:00', 'admin', 'admin', 'janedoe', 'janedoe@example.com', 'Jane', 'Doe', 'hashed_password456'),
    (TRUE, 'MEETING', 'PUBLIC', '2024-05-10T10:00:00', '2024-05-10T11:45:00', 'moderator', 'admin', 'bobjones', 'bobjones@example.com', 'Bob', 'Jones', 'hashed_password789'),
    (TRUE, 'OUT_OF_OFFICE', 'HIDDEN', '2024-05-08T15:30:00', '2024-05-08T17:00:00', 'admin', 'admin', 'alicebrown', 'alicebrown@example.com', 'Alice', 'Brown', 'hashed_password321'),
    (FALSE, 'DEEPWORK', 'PRIVATE', '2024-05-07T14:00:00', '2024-05-07T16:30:00', 'moderator', 'admin', 'chriswilliams', 'chriswilliams@example.com', 'Chris', 'Williams', 'hashed_password654');

-- these are not inside any same group as the user in question
INSERT INTO userx
(enabled, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password)
VALUES
    (TRUE, 'MEETING', 'PUBLIC', '2024-05-11T10:30:00', '2024-05-11T11:45:00', 'admin', 'admin', 'peterparker', 'peterparker@example.com', 'Peter', 'Parker', 'hashed_password987'),
    (FALSE, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-11T13:00:00', '2024-05-11T14:15:00', 'admin', 'admin', 'tonystark', 'tonystark@example.com', 'Tony', 'Stark', 'hashed_password852'),
    (TRUE, 'DEEPWORK', 'HIDDEN', '2024-05-10T15:30:00', '2024-05-10T17:00:00', 'admin', 'admin', 'brucewayne', 'brucewayne@example.com', 'Bruce', 'Wayne', 'hashed_password753');


-- add the members to the groups

INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'johndoe'), (1, 'janedoe'), (1, 'bobjones');
INSERT INTO groupx_members (groups_id, members_username) VALUES (2, 'alicebrown'), (2, 'chriswilliams'), (2, 'johndoe');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'peterparker'), (3, 'tonystark');

INSERT INTO room (room_id) VALUES ('room_10');
INSERT INTO room (room_id) VALUES ('room_11');
INSERT INTO room (room_id) VALUES ('room_12');

INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'room_10');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'room_11');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'room_12');

INSERT INTO tempera_station
(enabled, access_point_id, user_username, id)
VALUES
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'johndoe', 'TEMP123'),
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'janedoe', 'TEMP124'),
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'bobjones', 'TEMP125'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'alicebrown', 'TEMP126'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'chriswilliams', 'TEMP127'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'peterparker', 'TEMP128'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'tonystark', 'TEMP129'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'brucewayne', 'TEMP130');

INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('TEMPERATURE', -10, 'TEMP123', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP124', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP125', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP126', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP127', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP128', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP129', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP130', 'CELSIUS');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('IRRADIANCE', -11, 'TEMP123', 'LUX'),
('IRRADIANCE', -11, 'TEMP124', 'LUX'),
('IRRADIANCE', -11, 'TEMP125', 'LUX'),
('IRRADIANCE', -11, 'TEMP126', 'LUX'),
('IRRADIANCE', -11, 'TEMP127', 'LUX'),
('IRRADIANCE', -11, 'TEMP128', 'LUX'),
('IRRADIANCE', -11, 'TEMP129', 'LUX'),
('IRRADIANCE', -11, 'TEMP130', 'LUX');

INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('HUMIDITY', -12, 'TEMP123', 'PERCENT'),
('HUMIDITY', -12, 'TEMP124', 'PERCENT'),
('HUMIDITY', -12, 'TEMP125', 'PERCENT'),
('HUMIDITY', -12, 'TEMP126', 'PERCENT'),
('HUMIDITY', -12, 'TEMP127', 'PERCENT'),
('HUMIDITY', -12, 'TEMP128', 'PERCENT'),
('HUMIDITY', -12, 'TEMP129', 'PERCENT'),
('HUMIDITY', -12, 'TEMP130', 'PERCENT');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('NMVOC', -13, 'TEMP123', 'OHM'),
('NMVOC', -13, 'TEMP124', 'OHM'),
('NMVOC', -13, 'TEMP125', 'OHM'),
('NMVOC', -13, 'TEMP126', 'OHM'),
('NMVOC', -13, 'TEMP127', 'OHM'),
('NMVOC', -13, 'TEMP128', 'OHM'),
('NMVOC', -13, 'TEMP129', 'OHM'),
('NMVOC', -13, 'TEMP130', 'OHM');

-- fill in measurements for all the temperature sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES (20.0, -10, '2016-01-01 00:00:00', 'TEMP123'),
(20.0, -10, '2016-01-01 00:00:00', 'TEMP124'),
(25.9, -10, '2016-01-01 00:00:00', 'TEMP125'),
(22.0, -10, '2016-01-01 00:00:00', 'TEMP126'),
(24.0, -10, '2016-01-01 00:00:00', 'TEMP127'),
(30.0, -10, '2016-01-01 00:00:00', 'TEMP128'),
(17.0, -10, '2016-01-01 00:00:00', 'TEMP129'),
(24.1, -10, '2016-01-01 00:00:00', 'TEMP130');

-- fill in measurements for all the irradiance sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES (1000.0, -11, '2016-01-01 00:00:00', 'TEMP123'),
(1000.0, -11, '2016-01-01 00:00:00', 'TEMP124'),
(1100.0, -11, '2016-01-01 00:00:00', 'TEMP125'),
(1200.0, -11, '2016-01-01 00:00:00', 'TEMP126'),
(1240.0, -11, '2016-01:01 00:00:00', 'TEMP127'),
(1900.0, -11, '2016-01-01 00:00:00', 'TEMP128'),
(9000.0, -11, '2016-01-01 00:00:00', 'TEMP129'),
(8900.0, -11, '2016-01-01 00:00:00', 'TEMP130');

-- fill in measurements for all the humidity sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES (50.0, -12, '2016-01-01 00:00:00', 'TEMP123'),
(50.0, -12, '2016-01:01 00:00:00', 'TEMP124'),
(55.0, -12, '2016-01-01 00:00:00', 'TEMP125'),
(60.0, -12, '2016-01-01 00:00:00', 'TEMP126'),
(65.0, -12, '2016-01-01 00:00:00', 'TEMP127'),
(70.0, -12, '2016-01-01 00:00:00', 'TEMP128'),
(75.0, -12, '2016-01-01 00:00:00', 'TEMP129'),
(80.0, -12, '2016-01-01 00:00:00', 'TEMP130');

-- fill in measurements for all the nmvoc sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES (100.0, -13, '2016-01-01 00:00:00', 'TEMP123'),
(100.0, -13, '2016-01:01 00:00:00', 'TEMP124'),
(110.0, -13, '2016-01-01 00:00:00', 'TEMP125'),
(120.0, -13, '2016-01:01 00:00:00', 'TEMP126'),
(130.0, -13, '2016-01-01 00:00:00', 'TEMP127'),
(140.0, -13, '2016-01:01 00:00:00', 'TEMP128'),
(150.0, -13, '2016-01:01 00:00:00', 'TEMP129'),
(160.0, -13, '2016-01:01 00:00:00', 'TEMP130');