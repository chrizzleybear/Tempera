DELETE FROM internal_record;
DELETE FROM external_record;
DELETE FROM measurement;
DELETE FROM sensor;
DELETE FROM tempera_station;
DELETE FROM access_point;
DELETE FROM room;
DELETE FROM groupx_members;
DELETE FROM groupx;
DELETE FROM project_contributors;
DELETE FROM userx_userx_role;
DELETE FROM userx;
DELETE FROM project;

--Todo: add this data to the db for real frontend testing

-- projects
INSERT INTO project (id, name, description) VALUES
    (-1, 'Ausbeutung', 'This project beuts you aus'),
    (-2, 'Expansion', 'This project aims to expand our operations globally.'),
    (-3, 'Innovation', 'This project focuses on fostering innovation within the company.'),
    (-4, 'Efficiency', 'This project aims to improve efficiency across all departments.'),
    (-5, 'Sustainability Initiative', 'This project aims to make our operations more environmentally friendly.'),
    (-6, 'Customer Satisfaction Improvement', 'This project focuses on enhancing customer experience and satisfaction.'),
    (-7, 'Product Development', 'This project involves developing new products to meet market demands.'),
    (-8, 'Cost Reduction Initiative', 'This project aims to identify and implement cost-saving measures across the organization.'),
    (-9, 'Quality Assurance Enhancement', 'This project focuses on improving the quality control processes to ensure product quality and reliability.'),
    (-10, 'Marketing Campaign Launch', 'This project involves planning and executing a new marketing campaign to attract customers.'),
    (-11, 'Training and Development Program', 'This project focuses on providing training and development opportunities for employees to enhance their skills and performance.'),
    (-12, 'Infrastructure Upgrade', 'This project involves upgrading the company''s IT infrastructure to improve efficiency and security.');


-- these are in the same groups as the user (6 people)
INSERT INTO userx
(enabled, default_project_id, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password)
VALUES
    (True, null, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-09T09:00:00', '2024-05-09T10:15:00', 'admin', 'admin', 'admin', 'admini@example.com', 'Jane', 'Doe', 'hashed_password456'),
    (TRUE, -1, 'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'manager', 'manager@mail.com', 'Bob', 'Marley', 'hashed_password123'),
    (TRUE, -2, 'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'johndoe', 'johndoe@example.com', 'John', 'Doe', 'hashed_password123'),
    (TRUE, null, 'MEETING', 'PUBLIC', '2024-05-10T10:00:00', '2024-05-10T11:45:00', 'bobjones', 'admin', 'bobjones', 'bobjones@example.com', 'Bob', 'Jones', 'hashed_password789'),
    (TRUE, -3, 'OUT_OF_OFFICE', 'HIDDEN', '2024-05-08T15:30:00', '2024-05-08T17:00:00', 'admin', 'admin', 'alicebrown', 'alicebrown@example.com', 'Alice', 'Brown', 'hashed_password321'),
    (true, -2,'DEEPWORK', 'PRIVATE', '2024-05-07T14:00:00', '2024-05-07T16:30:00', 'chriswilliams', 'admin', 'chriswilliams', 'chriswilliams@example.com', 'Chris', 'Williams', 'hashed_password654'),
-- these are not inside any same group as the user in question (3 people)
    (TRUE, -5, 'MEETING', 'PUBLIC', '2024-05-11T10:30:00', '2024-05-11T11:45:00', 'admin', 'admin', 'peterparker', 'peterparker@example.com', 'Peter', 'Parker', 'hashed_password987'),
    (true, null, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-11T13:00:00', '2024-05-11T14:15:00', 'admin', 'admin', 'tonystark', 'tonystark@example.com', 'Tony', 'Stark', 'hashed_password852'),
    (TRUE, -1, 'DEEPWORK', 'HIDDEN', '2024-05-10T15:30:00', '2024-05-10T17:00:00', 'admin', 'admin', 'brucewayne', 'brucewayne@example.com', 'Bruce', 'Wayne', 'hashed_password753'),
-- this one is in the same group but his temperastation is not enabled
    (TRUE, -4, 'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'clarkkent', 'clarkkent@webmail.com', 'Clark', 'Kent', 'hashed_password123');

INSERT INTO project_contributors (project_id, username) VALUES
                                                                         (-1, 'admin'), (-1, 'manager'), (-1, 'johndoe'), (-1, 'alicebrown'), (-1, 'brucewayne'), (-1, 'clarkkent'),
                                                                         (-2, 'admin'), (-2, 'manager'), (-2, 'johndoe'), (-2, 'alicebrown'), (-2, 'brucewayne'), (-2, 'clarkkent'),
                                                                         (-3, 'admin'), (-3, 'manager'), (-3, 'johndoe'), (-3, 'alicebrown'), (-3, 'brucewayne'), (-3, 'clarkkent'),
                                                                         (-4, 'admin'), (-4, 'manager'), (-4, 'johndoe'), (-4, 'alicebrown'), (-4, 'brucewayne'), (-4, 'clarkkent'),
                                                                         (-5, 'admin'), (-5, 'manager'), (-5, 'johndoe'), (-5, 'alicebrown'), (-5, 'brucewayne'), (-5, 'clarkkent'),
                                                                         (-6, 'admin'), (-6, 'manager'), (-6, 'johndoe'), (-6, 'alicebrown'), (-6, 'brucewayne'), (-6, 'clarkkent'),
                                                                         (-7, 'admin'), (-7, 'manager'), (-7, 'johndoe'), (-7, 'alicebrown'), (-7, 'brucewayne'), (-7, 'clarkkent'),
                                                                         (-8, 'admin'), (-8, 'manager'), (-8, 'johndoe'), (-8, 'alicebrown'), (-8, 'brucewayne'), (-8, 'clarkkent'),
                                                                         (-9, 'admin'), (-9, 'manager'), (-9, 'johndoe'), (-9, 'alicebrown'), (-9, 'brucewayne'), (-9, 'clarkkent'),
                                                                         (-10, 'admin'), (-10, 'manager'), (-10, 'johndoe'), (-10, 'alicebrown'), (-10, 'brucewayne'), (-10, 'clarkkent'),
                                                                         (-11, 'admin'), (-11, 'manager'), (-11, 'johndoe'), (-11, 'alicebrown'), (-11, 'brucewayne'), (-11, 'clarkkent'),
                                                                         (-12, 'admin'), (-12, 'manager'), (-12, 'johndoe'), (-12, 'alicebrown'), (-12, 'brucewayne'), (-12, 'clarkkent');
-- add some Groups to test db
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (1,'manager', 'this is just for testing', 'testGroup1');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (2,'manager', 'this is also just for testing', 'testGroup2');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (3,'manager', 'this is also just for testing', 'outsiderGroup');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (4,'manager', 'this is also just for testing', 'outsiderGroup2');
-- add some more members to fill the groups


-- add the members to the groups
-- all in all 10 people
-- 9 of them have active temperastations
-- one of them is john doe himself
-- so 8 people should be displayed as colleagues
INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'johndoe'), (2, 'johndoe');
INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'alicebrown'), (3, 'alicebrown'), (4, 'alicebrown');
INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'chriswilliams'), (2, 'chriswilliams'), (3, 'chriswilliams'), (4, 'chriswilliams');
INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'admin'), (2, 'admin'), (3, 'admin'), (4, 'admin');
INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'manager'), (2, 'manager'), (3, 'manager'), (4, 'manager');
INSERT INTO groupx_members (groups_id, members_username) VALUES (2, 'bobjones');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'brucewayne'), (4, 'brucewayne');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'peterparker'), (4, 'peterparker');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'tonystark'), (4, 'tonystark');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'clarkkent'), (4, 'clarkkent');



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
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'admin', 'TEMP124'),
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'bobjones', 'TEMP125'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'alicebrown', 'TEMP126'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'chriswilliams', 'TEMP127'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'peterparker', 'TEMP128'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'tonystark', 'TEMP129'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'brucewayne', 'TEMP130'),
    (FALSE, '333e4567-e89b-12d3-a456-426614174001', 'clarkkent', 'TEMP131'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'manager', 'TEMP132');

-- realized this is not necessary for all the colleagues for the tests but we can use it later
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('TEMPERATURE', -10, 'TEMP123', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP124', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP125', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP126', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP127', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP128', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP129', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP130', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP131', 'CELSIUS'),
('TEMPERATURE', -10, 'TEMP132', 'CELSIUS');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('IRRADIANCE', -11, 'TEMP123', 'LUX'),
('IRRADIANCE', -11, 'TEMP124', 'LUX'),
('IRRADIANCE', -11, 'TEMP125', 'LUX'),
('IRRADIANCE', -11, 'TEMP126', 'LUX'),
('IRRADIANCE', -11, 'TEMP127', 'LUX'),
('IRRADIANCE', -11, 'TEMP128', 'LUX'),
('IRRADIANCE', -11, 'TEMP129', 'LUX'),
('IRRADIANCE', -11, 'TEMP130', 'LUX'),
('IRRADIANCE', -11, 'TEMP131', 'LUX'),
('IRRADIANCE', -11, 'TEMP132', 'LUX');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('HUMIDITY', -12, 'TEMP123', 'PERCENT'),
('HUMIDITY', -12, 'TEMP124', 'PERCENT'),
('HUMIDITY', -12, 'TEMP125', 'PERCENT'),
('HUMIDITY', -12, 'TEMP126', 'PERCENT'),
('HUMIDITY', -12, 'TEMP127', 'PERCENT'),
('HUMIDITY', -12, 'TEMP128', 'PERCENT'),
('HUMIDITY', -12, 'TEMP129', 'PERCENT'),
('HUMIDITY', -12, 'TEMP130', 'PERCENT'),
('HUMIDITY', -12, 'TEMP131', 'PERCENT'),
('HUMIDITY', -12, 'TEMP132', 'PERCENT');
INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES ('NMVOC', -13, 'TEMP123', 'OHM'),
('NMVOC', -13, 'TEMP124', 'OHM'),
('NMVOC', -13, 'TEMP125', 'OHM'),
('NMVOC', -13, 'TEMP126', 'OHM'),
('NMVOC', -13, 'TEMP127', 'OHM'),
('NMVOC', -13, 'TEMP128', 'OHM'),
('NMVOC', -13, 'TEMP129', 'OHM'),
('NMVOC', -13, 'TEMP130', 'OHM'),
('NMVOC', -13, 'TEMP131', 'OHM'),
('NMVOC', -13, 'TEMP132', 'OHM');

-- fill in measurements for all the temperature sensors (also not necessary for HomeDataMapperTest but can be used later)
-- user of interest is johndoe (TEMP123)
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
(20.0, -10, '2024-05-10T08:30:00', 'TEMP123'),
(20.0, -10, '2024-05-10T09:45:00', 'TEMP124'),
(25.9, -10, '2024-05-11T10:15:00', 'TEMP125'),
(22.0, -10, '2024-05-11T11:30:00', 'TEMP126'),
(24.0, -10, '2024-05-12T12:00:00', 'TEMP127'),
(30.0, -10, '2024-05-12T13:15:00', 'TEMP128'),
(24.1, -10, '2024-05-11T15:45:00', 'TEMP130'),
(24.1, -10, '2024-05-11T15:45:00', 'TEMP131'),
(24.1, -10, '2024-05-11T15:45:00', 'TEMP132');

-- fill in measurements for all the irradiance sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
(1000.0, -11, '2024-05-10T08:30:00', 'TEMP123'),
(1000.0, -11, '2024-05-10T09:45:00', 'TEMP124'),
(1100.0, -11, '2024-05-11T10:15:00', 'TEMP125'),
(1200.0, -11, '2024-05-11T11:30:00', 'TEMP126'),
(1240.0, -11, '2024-05-12T12:00:00', 'TEMP127'),
(1900.0, -11, '2024-05-12T13:15:00', 'TEMP128'),
(8900.0, -11, '2024-05-11T15:45:00', 'TEMP130'),
(8900.0, -11, '2024-05-11T15:45:00', 'TEMP131'),
(8900.0, -11, '2024-05-11T15:45:00', 'TEMP132');

-- fill in measurements for all the humidity sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
(50.0, -12, '2024-05-10T08:30:00', 'TEMP123'),
(50.0, -12, '2024-05-10T09:45:00', 'TEMP124'),
(55.0, -12, '2024-05-11T10:15:00', 'TEMP125'),
(60.0, -12, '2024-05-11T11:30:00', 'TEMP126'),
(65.0, -12, '2024-05-12T12:00:00', 'TEMP127'),
(70.0, -12, '2024-05-12T13:15:00', 'TEMP128'),
(80.0, -12, '2024-05-11T15:45:00', 'TEMP130'),
(80.0, -12, '2024-05-11T15:45:00', 'TEMP131'),
(80.0, -12, '2024-05-11T15:45:00', 'TEMP132');

-- fill in measurements for all the nmvoc sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
(100.0, -13, '2024-05-10T08:30:00', 'TEMP123'),
(100.0, -13, '2024-05-10T09:45:00', 'TEMP124'),
(110.0, -13, '2024-05-11T10:15:00', 'TEMP125'),
(120.0, -13, '2024-05-11T11:30:00', 'TEMP126'),
(124.0, -13, '2024-05-12T12:00:00', 'TEMP127'),
(190.0, -13, '2024-05-12T13:15:00', 'TEMP128'),
(890.0, -13, '2024-05-11T15:45:00', 'TEMP130'),
(890.0, -13, '2024-05-11T15:45:00', 'TEMP131'),
(890.0, -13, '2024-05-11T15:45:00', 'TEMP132');

-- Timerecord (needed for timestamp)
INSERT INTO external_record (duration, start, time_end, user_username, state) VALUES
(3400, '2024-05-10 09:30:00', null, 'johndoe', 'DEEPWORK');

INSERT INTO internal_record (id, groupx_id, project_id, start, time_end, ext_rec_start, user_name) VALUES
(-1, null, -1, '2024-05-10 09:30:00', null, '2024-05-10 09:30:00', 'johndoe');

-- possibly add older data
