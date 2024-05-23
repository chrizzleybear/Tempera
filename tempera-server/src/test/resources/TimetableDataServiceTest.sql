DELETE FROM internal_record;
DELETE FROM external_record;
DELETE FROM measurement;
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



INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Admin', 'Istrator', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'admin', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PUBLIC');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN');

-- Testdata for ProjectService
INSERT INTO project (id, name, description, manager_username) VALUES
(-1, 'Serious Business', 'This project beuts you aus', 'admin'),
(-2, 'Expansion', 'This project aims to expand our operations globally.', 'admin'),
(-3, 'Innovation', 'This project focuses on fostering innovation within the company.', 'admin'),
(-4, 'Efficiency', 'This project aims to improve efficiency across all departments.', 'admin'),
(-5, 'Sustainability Initiative', 'This project aims to make our operations more environmentally friendly.', 'admin'),
(-6, 'Customer Satisfaction Improvement', 'This project focuses on enhancing customer experience and satisfaction.', 'admin'),
(-7, 'Product Development', 'This project involves developing new products to meet market demands.', 'admin'),
(-8, 'Cost Reduction Initiative', 'This project aims to identify and implement cost-saving measures across the organization.', 'admin'),
(-9, 'Quality Assurance Enhancement', 'This project focuses on improving the quality control processes to ensure product quality and reliability.', 'admin'),
(-10, 'Marketing Campaign Launch', 'This project involves planning and executing a new marketing campaign to attract customers.', 'admin'),
(-11, 'Training and Development Program', 'This project focuses on providing training and development opportunities for employees to enhance their skills and performance.', 'admin'),
(-12, 'Infrastructure Upgrade', 'This project involves upgrading the company''s IT infrastructure to improve efficiency and security.', 'admin');

INSERT INTO userx (enabled, default_project_id, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password)
VALUES (TRUE, -2, 'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'johndoe', 'johndoe@example.com', 'John', 'Doe', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u');
INSERT INTO userx_userx_role (userx_username, roles) VALUES ('johndoe', 'EMPLOYEE');


INSERT INTO groupx (id, group_lead_username, description, name) VALUES (1,'admin', 'this is just for testing', 'Marketing_Group');
INSERT INTO groupx (id, group_lead_username, description, name) VALUES (2,'admin', 'this is also just for testing', 'Research_Group');


-- add some of the created projects to some GroupxProject Objects:
-- add Serious Business, Expansion, Innovation, Efficiency,Sustainability and Customer Satisfaction to testGroup1
INSERT INTO groupx_project_object (group_id, project_id)
VALUES (1, -1), (1, -2), (1, -3), (1, -4), (1,-5), (1, -6);

-- add Product Development, Cost Reduction, Quality Assurance, Marketing Campaign Launch, Training and Development and Infrastructure Upgrade to testGroup2
INSERT INTO groupx_project_object (group_id, project_id)
Values (2, -7), (2, -8), (2, -9), (2, -10), (2, -11), (2, -12);


INSERT INTO groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username)
VALUES (1, -1, 'admin'), (1, -2, 'admin'), (1, -3, 'admin'), (1, -4, 'admin'), (1, -5, 'admin'), (1, -6, 'admin');

INSERT INTO groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username)
VALUES (2, -7, 'johndoe'), (2, -8, 'johndoe'), (2, -9, 'johndoe'), (2, -10, 'johndoe'), (2, -11, 'johndoe'), (2, -12, 'johndoe');

-- INSERT INTO room (room_id) VALUES ('room_1');
-- INSERT INTO room (room_id) VALUES ('room_2');
--
-- INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '123e4567-e89b-12d3-a456-426614174001', 'room_1');
-- INSERT INTO access_point (enabled, id, room_room_id) VALUES (TRUE, '456e4567-e89b-12d3-a456-426614174001', 'room_2');
--
-- INSERT INTO TEMPERA_STATION (ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, '123e4567-e89b-12d3-a456-426614174001','admin', 'tempera_station_1');
-- INSERT INTO tempera_station (enabled, access_point_id, user_username, id) VALUES (TRUE, '456e4567-e89b-12d3-a456-426614174001', 'johndoe', 'tempera_station_2');

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
