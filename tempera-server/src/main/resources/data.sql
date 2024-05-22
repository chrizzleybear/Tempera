INSERT INTO project (id, name, description) VALUES
                                                (-1, 'Serious Business', 'This project beuts you aus'),
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

INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Admin', 'Istrator', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'admin', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PUBLIC');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Susi', 'Kaufgern', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user1', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PRIVATE');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'MANAGER');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Max', 'Mustermann', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user2', 'admin', '2016-01-01 00:00:00', 'OUT_OF_OFFICE', 'HIDDEN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user2', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Elvis', 'The King', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'elvis', 'elvis', '2016-01-01 00:00:00', 'OUT_OF_OFFICE', 'PUBLIC');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'ADMIN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'EMPLOYEE');

-- these users can be used to display as colleagues for john doe
INSERT INTO userx (enabled, default_project_id, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password) VALUES
    (TRUE, -2, 'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'johndoe', 'johndoe@example.com', 'John', 'Doe', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
    (TRUE, null, 'MEETING', 'PUBLIC', '2024-05-10T10:00:00', '2024-05-10T11:45:00', 'bobjones', 'admin', 'bobjones', 'bobjones@example.com', 'Bob', 'Jones', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
    (TRUE, -3, 'OUT_OF_OFFICE', 'HIDDEN', '2024-05-08T15:30:00', '2024-05-08T17:00:00', 'admin', 'admin', 'alicebrown', 'alicebrown@example.com', 'Alice', 'Brown', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
    (true, -2,'DEEPWORK', 'PRIVATE', '2024-05-07T14:00:00', '2024-05-07T16:30:00', 'chriswilliams', 'admin', 'chriswilliams', 'chriswilliams@example.com', 'Chris', 'Williams', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
    (TRUE, -5, 'MEETING', 'PUBLIC', '2024-05-11T10:30:00', '2024-05-11T11:45:00', 'admin', 'admin', 'peterparker', 'peterparker@example.com', 'Peter', 'Parker', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
    (true, null, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-11T13:00:00', '2024-05-11T14:15:00', 'admin', 'admin', 'tonystark', 'tonystark@example.com', 'Tony', 'Stark', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
    (TRUE, -1, 'DEEPWORK', 'HIDDEN', '2024-05-10T15:30:00', '2024-05-10T17:00:00', 'admin', 'admin', 'brucewayne', 'brucewayne@example.com', 'Bruce', 'Wayne', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
    (TRUE, -4, 'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'clarkkent', 'clarkkent@webmail.com', 'Clark', 'Kent', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u');

INSERT INTO userx_userx_role (userx_username, roles) VALUES ('johndoe', 'EMPLOYEE'), ('bobjones', 'EMPLOYEE'), ('alicebrown', 'EMPLOYEE'), ('chriswilliams', 'EMPLOYEE'), ('peterparker', 'EMPLOYEE'), ('tonystark', 'EMPLOYEE'), ('brucewayne', 'EMPLOYEE'), ('clarkkent', 'EMPLOYEE');
INSERT INTO userx_userx_role (userx_username, roles) VALUES ('brucewayne', 'MANAGER');

-- add some Groups to test db
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (-1,'brucewayne', 'this is just for testing', 'testGroup1');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (-2,'brucewayne', 'this is also just for testing', 'testGroup2');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (-3,'brucewayne', 'this is also just for testing', 'outsiderGroup');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (-4,'brucewayne', 'this is also just for testing', 'outsiderGroup2');

-- add some of the created projects to some GroupxProject Objects:
-- add Serious Business, Expansion, Innovation, Efficiency,Sustainability and Customer Satisfaction to testGroup1
INSERT INTO groupx_project_object (group_id, project_id)
VALUES (-1, -1), (-1, -2), (-1, -3), (-1, -4), (-1,-5), (-1, -6);

-- add Product Development, Cost Reduction, Quality Assurance, Marketing Campaign Launch, Training and Development and Infrastructure Upgrade to testGroup2
INSERT INTO groupx_project_object (group_id, project_id)
Values (-2, -7), (-2, -8), (-2, -9), (-2, -10), (-2, -11), (-2, -12);

INSERT INTO groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username)
VALUES (-1, -1, 'admin'), (-1, -2, 'admin'), (-1, -3, 'admin'), (-1, -4, 'admin'), (-1, -5, 'admin'), (-1, -6, 'admin');
INSERT INTO groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username)
VALUES (-2, -7, 'johndoe'), (-2, -8, 'johndoe'), (-2, -9, 'johndoe'), (-2, -10, 'johndoe'), (-2, -11, 'johndoe'), (-2, -12, 'johndoe');


INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'johndoe'), (2, 'johndoe');
INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'alicebrown'), (3, 'alicebrown'), (4, 'alicebrown');
INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'chriswilliams'), (2, 'chriswilliams'), (3, 'chriswilliams'), (4, 'chriswilliams');
INSERT INTO groupx_members (groups_id, members_username) VALUES (1, 'admin'), (2, 'admin'), (3, 'admin'), (4, 'admin');
INSERT INTO groupx_members (groups_id, members_username) VALUES (2, 'bobjones');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'brucewayne'), (4, 'brucewayne');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'peterparker'), (4, 'peterparker');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'tonystark'), (4, 'tonystark');
INSERT INTO groupx_members (groups_id, members_username) VALUES (3, 'clarkkent'), (4, 'clarkkent');


-- Testdata for TimeRecordService
INSERT INTO external_record (duration, start, time_end, user_username, state)
VALUES
    (30, '2024-05-16 12:00:00', '2024-05-16 12:29:59', 'admin', 'DEEPWORK'),
    (45, '2024-05-16 13:00:00', '2024-05-16 13:44:59', 'admin', 'DEEPWORK'),
    (90, '2024-05-16 14:00:00', '2024-05-16 15:29:59', 'admin', 'MEETING'),
    (120, '2024-05-17 09:00:00', '2024-05-17 10:59:59', 'admin', 'MEETING'),
    (60, '2024-05-17 11:00:00', '2024-05-17 11:59:59', 'admin', 'AVAILABLE'),
    (300, '2024-05-17 15:00:00', '2024-05-17 19:59:59', 'admin', 'OUT_OF_OFFICE'),
    (200, '2024-05-18 10:00:00', '2024-05-18 13:19:59', 'admin', 'AVAILABLE'),
    (100, '2024-05-18 14:00:00', '2024-05-18 15:39:59', 'admin', 'MEETING'),
    (50, '2024-05-18 16:00:00', '2024-05-18 16:49:59', 'admin', 'OUT_OF_OFFICE'),
    (400, '2024-05-19 09:00:00', '2024-05-19 15:39:59', 'admin', 'AVAILABLE'),
    (150, '2024-05-19 14:00:00', null, 'admin', 'DEEPWORK'),

    (3400, '2024-05-10 09:30:00', '2024-05-11 10:29:59', 'johndoe', 'DEEPWORK'),
    (60, '2024-05-11 10:00:00', '2024-05-11 10:59:59', 'johndoe', 'MEETING'),
    (45, '2024-05-11 13:00:00', '2024-05-11 13:44:59', 'johndoe', 'OUT_OF_OFFICE'),
    (90, '2024-05-12 11:00:00', '2024-05-12 12:29:59', 'johndoe', 'AVAILABLE'),
    (120, '2024-05-12 14:00:00', '2024-05-12 15:59:59', 'johndoe', 'MEETING'),
    (120, '2024-05-12 17:00:00', '2024-05-12 18:59:59', 'johndoe', 'OUT_OF_OFFICE'),
    (30, '2024-05-13 08:00:00', '2024-05-13 08:29:59', 'johndoe', 'AVAILABLE'),
    (150, '2024-05-13 11:00:00', '2024-05-13 13:29:59', 'johndoe', 'OUT_OF_OFFICE'),
    (300, '2024-05-14 09:00:00', '2024-05-14 13:59:59', 'johndoe', 'AVAILABLE'),
    (200, '2024-05-14 13:00:00', '2024-05-14 16:19:59', 'johndoe', 'DEEPWORK'),
    (200, '2024-05-14 17:00:00', '2024-05-14 20:19:59', 'johndoe', 'OUT_OF_OFFICE'),
    (100, '2024-05-15 10:00:00', '2024-05-15 11:39:59', 'johndoe', 'DEEPWORK'),
    (50, '2024-05-15 15:00:00', null, 'johndoe', 'MEETING');


INSERT INTO internal_record (id, group_id, project_id, start, time_end, ext_rec_start, user_name)
VALUES
    (-1, null, null, '2024-05-16 12:00:00', '2024-05-16 12:59:59', '2024-05-16 12:00:00', 'admin'),
    (-3, 1, -2, '2024-05-16 13:00:00', '2024-05-16 13:59:59', '2024-05-16 13:00:00', 'admin'),
    (-4, null, null, '2024-05-16 14:00:00', '2024-05-17 08:59:59', '2024-05-16 14:00:00', 'admin'),
    (-5, 1, -3, '2024-05-17 09:00:00', '2024-05-17 10:59:59', '2024-05-17 09:00:00', 'admin'),
    (-6, null, null, '2024-05-17 11:00:00', '2024-05-17 14:59:59', '2024-05-17 11:00:00', 'admin'),
    (-7, 1, -4, '2024-05-17 15:00:00', '2024-05-18 09:59:59', '2024-05-17 15:00:00', 'admin'),
    (-8, null, null, '2024-05-18 10:00:00', '2024-05-18 13:59:59', '2024-05-18 10:00:00', 'admin'),
    (-9, 1, -5, '2024-05-18 14:00:00', '2024-05-18 15:59:59', '2024-05-18 14:00:00', 'admin'),
    (-10, null, null, '2024-05-18 16:00:00', '2024-05-19 08:59:59', '2024-05-18 16:00:00', 'admin'),
    (-11, 1, -6, '2024-05-19 09:00:00', '2024-05-19 13:59:59', '2024-05-19 09:00:00', 'admin'),
    (-12, null, null, '2024-05-19 14:00:00', null, '2024-05-19 14:00:00', 'admin'),

    (-2, 1, -1, '2024-05-10 09:30:00', '2024-05-11 09:29:59', '2024-05-10 09:30:00', 'johndoe'),
    (-13, null, null, '2024-05-11 10:00:00', '2024-05-11 12:59:59', '2024-05-11 10:00:00', 'johndoe'),
    (-14, 2, -7, '2024-05-11 13:00:00', '2024-05-12 10:59:59', '2024-05-11 13:00:00', 'johndoe'),
    (-15, null, null, '2024-05-12 11:00:00', '2024-05-12 13:59:59', '2024-05-12 11:00:00', 'johndoe'),
    (-16, 2, -8, '2024-05-12 14:00:00', '2024-05-12 16:59:59', '2024-05-12 14:00:00', 'johndoe'),
    (-17, null, null, '2024-05-13 08:00:00', '2024-05-13 10:59:59', '2024-05-13 08:00:00', 'johndoe'),
    (-18, 2, -9, '2024-05-13 11:00:00', '2024-05-14 08:59:59', '2024-05-13 11:00:00', 'johndoe'),
    (-19, null, null, '2024-05-14 09:00:00', '2024-05-14 12:59:59', '2024-05-14 09:00:00', 'johndoe'),
    (-20, 2, -10, '2024-05-14 13:00:00', '2024-05-14 16:59:59', '2024-05-14 13:00:00', 'johndoe'),
    (-21, null, null, '2024-05-15 10:00:00', '2024-05-15 14:59:59', '2024-05-15 10:00:00', 'johndoe'),
    (-22, 2, -11, '2024-05-15 15:00:00', null, '2024-05-15 15:00:00', 'johndoe');


-------------------------------------------MEASUREMENT CHAIN----------------------------------------
INSERT INTO room (room_id) VALUES ('room_1');
INSERT INTO room (room_id) VALUES ('room_2');
INSERT INTO room(room_id) VALUES ('room_3');
INSERT INTO room (room_id) VALUES ('room_10');
INSERT INTO room (room_id) VALUES ('room_11');
INSERT INTO room (room_id) VALUES ('room_12');

INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '123e4567-e89b-12d3-a456-426614174001', 'room_1');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (FALSE, '456e4567-e89b-12d3-a456-426614174001', 'room_2');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '789e4567-e89b-12d3-a456-426614174001', 'room_3');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'room_10');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'room_11');
INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'room_12');

INSERT INTO TEMPERA_STATION (ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, '123e4567-e89b-12d3-a456-426614174001','admin', 'tempera_station_1');
INSERT INTO TEMPERA_STATION (ENABLED, access_point_id, USER_USERNAME, ID) VALUES (FALSE,'123e4567-e89b-12d3-a456-426614174001', 'user2', 'tempera_station_disabled_2');
INSERT INTO TEMPERA_STATION (ENABLED, access_point_id, USER_USERNAME, ID) VALUES (FALSE, '123e4567-e89b-12d3-a456-426614174001', 'user1', 'tempera_station_disabled');
INSERT INTO TEMPERA_STATION (enabled, access_point_id, user_username, id) VALUES (FALSE, '123e4567-e89b-12d3-a456-426614174001', 'elvis', 'tempera_station_disabled_elvis');
INSERT INTO tempera_station
(enabled, access_point_id, user_username, id)
VALUES
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'johndoe', 'TEMP123'),
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'bobjones', 'TEMP125'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'alicebrown', 'TEMP126'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'chriswilliams', 'TEMP127'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'peterparker', 'TEMP128'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'tonystark', 'TEMP129'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'brucewayne', 'TEMP130'),
    (FALSE, '333e4567-e89b-12d3-a456-426614174001', 'clarkkent', 'TEMP131');

INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES
                                                                  ('TEMPERATURE', -1, 'tempera_station_1', 'CELSIUS'),
                                                                  ('TEMPERATURE', -10, 'TEMP123', 'CELSIUS'),
                                                                  ('TEMPERATURE', -10, 'TEMP125', 'CELSIUS'),
                                                                  ('TEMPERATURE', -10, 'TEMP126', 'CELSIUS'),
                                                                  ('TEMPERATURE', -10, 'TEMP127', 'CELSIUS'),
                                                                  ('TEMPERATURE', -10, 'TEMP128', 'CELSIUS'),
                                                                  ('TEMPERATURE', -10, 'TEMP129', 'CELSIUS'),
                                                                  ('TEMPERATURE', -10, 'TEMP130', 'CELSIUS'),
                                                                  ('TEMPERATURE', -10, 'TEMP131', 'CELSIUS');

INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES
                                                                  ('IRRADIANCE', -2, 'tempera_station_1', 'LUX'),
                                                                  ('IRRADIANCE', -11, 'TEMP123', 'LUX'),
                                                                  ('IRRADIANCE', -11, 'TEMP125', 'LUX'),
                                                                  ('IRRADIANCE', -11, 'TEMP126', 'LUX'),
                                                                  ('IRRADIANCE', -11, 'TEMP127', 'LUX'),
                                                                  ('IRRADIANCE', -11, 'TEMP128', 'LUX'),
                                                                  ('IRRADIANCE', -11, 'TEMP129', 'LUX'),
                                                                  ('IRRADIANCE', -11, 'TEMP130', 'LUX'),
                                                                  ('IRRADIANCE', -11, 'TEMP131', 'LUX');

INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES
                                                                  ('HUMIDITY', -3, 'tempera_station_1', 'PERCENT'),
                                                                  ('HUMIDITY', -12, 'TEMP123', 'PERCENT'),
                                                                  ('HUMIDITY', -12, 'TEMP125', 'PERCENT'),
                                                                  ('HUMIDITY', -12, 'TEMP126', 'PERCENT'),
                                                                  ('HUMIDITY', -12, 'TEMP127', 'PERCENT'),
                                                                  ('HUMIDITY', -12, 'TEMP128', 'PERCENT'),
                                                                  ('HUMIDITY', -12, 'TEMP129', 'PERCENT'),
                                                                  ('HUMIDITY', -12, 'TEMP130', 'PERCENT'),
                                                                  ('HUMIDITY', -12, 'TEMP131', 'PERCENT');

INSERT INTO SENSOR (SENSOR_TYPE, SENSOR_ID, TEMPERA_ID, UNIT) VALUES
                                                                  ('NMVOC', -4, 'tempera_station_1', 'OHM'),
                                                                  ('NMVOC', -13, 'TEMP123', 'OHM'),
                                                                  ('NMVOC', -13, 'TEMP125', 'OHM'),
                                                                  ('NMVOC', -13, 'TEMP126', 'OHM'),
                                                                  ('NMVOC', -13, 'TEMP127', 'OHM'),
                                                                  ('NMVOC', -13, 'TEMP128', 'OHM'),
                                                                  ('NMVOC', -13, 'TEMP129', 'OHM'),
                                                                  ('NMVOC', -13, 'TEMP130', 'OHM'),
                                                                  ('NMVOC', -13, 'TEMP131', 'OHM');

-- fill in measurements for all the temperature sensors (also not necessary for HomeDataMapperTest but can be used later)
-- user of interest is johndoe (TEMP123)
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
                                                                                                 (20.0, -1, '2024-05-15 09:00:00', 'tempera_station_1'),
                                                                                                 (20.0, -10, '2024-05-10T08:30:00', 'TEMP123'),
                                                                                                 (25.9, -10, '2024-05-11T10:15:00', 'TEMP125'),
                                                                                                 (22.0, -10, '2024-05-11T11:30:00', 'TEMP126'),
                                                                                                 (24.0, -10, '2024-05-12T12:00:00', 'TEMP127'),
                                                                                                 (30.0, -10, '2024-05-12T13:15:00', 'TEMP128'),
                                                                                                 (17.0, -10, '2024-05-10T14:30:00', 'TEMP129'),
                                                                                                 (24.1, -10, '2024-05-11T15:45:00', 'TEMP130'),
                                                                                                 (24.1, -10, '2024-05-11T15:45:00', 'TEMP131');

-- fill in measurements for all the irradiance sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
                                                                                                 (999.8, -2, '2024-05-15 09:02:00', 'tempera_station_1'),
                                                                                                 (1000.0, -11, '2024-05-10T08:30:00', 'TEMP123'),
                                                                                                 (1100.0, -11, '2024-05-11T10:15:00', 'TEMP125'),
                                                                                                 (1200.0, -11, '2024-05-11T11:30:00', 'TEMP126'),
                                                                                                 (1240.0, -11, '2024-05-12T12:00:00', 'TEMP127'),
                                                                                                 (1900.0, -11, '2024-05-12T13:15:00', 'TEMP128'),
                                                                                                 (9000.0, -11, '2024-05-10T14:30:00', 'TEMP129'),
                                                                                                 (8900.0, -11, '2024-05-11T15:45:00', 'TEMP130'),
                                                                                                 (8900.0, -11, '2024-05-11T15:45:00', 'TEMP131');

-- fill in measurements for all the humidity sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
                                                                                                 (40.8, -3, '2024-05-15 09:02:00', 'tempera_station_1'),
                                                                                                 (50.0, -12, '2024-05-10T08:30:00', 'TEMP123'),
                                                                                                 (55.0, -12, '2024-05-11T10:15:00', 'TEMP125'),
                                                                                                 (60.0, -12, '2024-05-11T11:30:00', 'TEMP126'),
                                                                                                 (65.0, -12, '2024-05-12T12:00:00', 'TEMP127'),
                                                                                                 (70.0, -12, '2024-05-12T13:15:00', 'TEMP128'),
                                                                                                 (75.0, -12, '2024-05-10T14:30:00', 'TEMP129'),
                                                                                                 (80.0, -12, '2024-05-11T15:45:00', 'TEMP130'),
                                                                                                 (80.0, -12, '2024-05-11T15:45:00', 'TEMP131');
-- fill in measurements for all the nmvoc sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)  VALUES
                                                                                                 (430.222, -4, '2024-05-15 09:02:00', 'tempera_station_1'),
                                                                                                 (100.0, -13, '2024-05-10T08:30:00', 'TEMP123'),
                                                                                                 (110.0, -13, '2024-05-11T10:15:00', 'TEMP125'),
                                                                                                 (120.0, -13, '2024-05-11T11:30:00', 'TEMP126'),
                                                                                                 (124.0, -13, '2024-05-12T12:00:00', 'TEMP127'),
                                                                                                 (190.0, -13, '2024-05-12T13:15:00', 'TEMP128'),
                                                                                                 (900.0, -13, '2024-05-10T14:30:00', 'TEMP129'),
                                                                                                 (890.0, -13, '2024-05-11T15:45:00', 'TEMP130'),
                                                                                                 (890.0, -13, '2024-05-11T15:45:00', 'TEMP131');


