DELETE FROM audit_log;
DELETE FROM internal_record;
DELETE FROM external_record;
DELETE FROM measurement;
DELETE FROM alert;
DELETE FROM sensor;
DELETE FROM tempera_station;
DELETE FROM access_point;
DELETE FROM room_thresholds;
DELETE FROM room;
DELETE FROM threshold;
DELETE FROM groupx_members;
DELETE FROM groupx_project_object_contributors;
DELETE FROM groupx_project_object;
DELETE FROM groupx;
DELETE FROM userx_userx_role;
DELETE FROM project;
DELETE FROM userx;


INSERT INTO userx (enabled, default_project_id, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password)
VALUES
    (True, null, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-09T09:00:00', '2024-05-09T10:15:00', 'admin', 'admin', 'admin', 'admini@example.com', 'Jane', 'Doe', 'hashed_password456');




INSERT INTO project (id, is_active, name, description, manager_username) VALUES
                                                                  (-1, true, 'Serious Business', 'This project beuts you aus', 'admin'),
                                                                  (-2, true, 'Expansion', 'This project aims to expand our operations globally.', 'admin'),
                                                                  (-3, true, 'Innovation', 'This project focuses on fostering innovation within the company.', 'admin'),
                                                                  (-4, true, 'Efficiency', 'This project aims to improve efficiency across all departments.', 'admin'),
                                                                  (-5, true, 'Sustainability Initiative', 'This project aims to make our operations more environmentally friendly.', 'admin'),
                                                                  (-6, true, 'Customer Satisfaction Improvement', 'This project focuses on enhancing customer experience and satisfaction.', 'admin'),
                                                                  (-7, true, 'Product Development', 'This project involves developing new products to meet market demands.', 'admin'),
                                                                  (-8, true, 'Cost Reduction Initiative', 'This project aims to identify and implement cost-saving measures across the organization.', 'admin'),
                                                                  (-9, true, 'Quality Assurance Enhancement', 'This project focuses on improving the quality control processes to ensure product quality and reliability.', 'admin'),
                                                                  (-10, true, 'Marketing Campaign Launch', 'This project involves planning and executing a new marketing campaign to attract customers.', 'admin'),
                                                                  (-11, true, 'Training and Development Program', 'This project focuses on providing training and development opportunities for employees to enhance their skills and performance.', 'admin'),
                                                                  (-12, true, 'Infrastructure Upgrade', 'This project involves upgrading the company''s IT infrastructure to improve efficiency and security.', 'admin');


-- these are in the same groups as the user (6 people)
INSERT INTO userx (enabled, default_project_id, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password)
VALUES
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


-- add some Groups to test db
INSERT INTO groupx (active, id, group_lead_username, description, name) VALUES (true, -1,'manager', 'this is just for testing', 'testGroup1');
INSERT INTO groupx (active, id, group_lead_username, description, name) VALUES (true, -2,'manager', 'this is also just for testing', 'testGroup2');
INSERT INTO groupx (active, id, group_lead_username, description, name) VALUES (true, -3,'manager', 'this is also just for testing', 'outsiderGroup');
INSERT INTO groupx (active, id, group_lead_username, description, name) VALUES (true, -4,'manager', 'this is also just for testing', 'outsiderGroup2');
-- add some more members to fill the groups

-- add some of the created projects to some GroupxProject Objects:
-- add Serious Business, Expansion, Innovation, Efficiency,Sustainability and Customer Satisfaction to testGroup1
INSERT INTO groupx_project_object (group_id, project_id, is_active)
VALUES (-1, -1, true), (-1, -2, true), (-1, -3, true), (-1, -4, true), (-1,-5, true), (-1, -6, true);

-- add Product Development, Cost Reduction, Quality Assurance, Marketing Campaign Launch, Training and Development and Infrastructure Upgrade to testGroup2
INSERT INTO groupx_project_object (group_id, project_id, is_active)
Values (-2, -7, true), (-2, -8, true), (-2, -9, true), (-2, -10, true), (-2, -11, true), (-2, -12, true);

INSERT INTO groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username)
VALUES (-1, -1, 'admin'), (-1, -2, 'admin'), (-1, -3, 'admin'), (-1, -4, 'admin'), (-1, -5, 'admin'), (-1, -6, 'admin');
INSERT INTO groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username)
VALUES (-2, -7, 'johndoe'), (-2, -8, 'johndoe'), (-2, -9, 'johndoe'), (-2, -10, 'johndoe'), (-2, -11, 'johndoe'), (-2, -12, 'johndoe');
-- add the members to the groups
-- all in all 10 people
-- 9 of them have active temperastations
-- one of them is john doe himself
-- so 8 people should be displayed as colleagues
INSERT INTO groupx_members (groups_id, members_username) VALUES (-1, 'johndoe'), (-2, 'johndoe');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-1, 'alicebrown'), (-3, 'alicebrown'), (-4, 'alicebrown');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-1, 'chriswilliams'), (-2, 'chriswilliams'), (-3, 'chriswilliams'), (-4, 'chriswilliams');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-1, 'admin'), (-2, 'admin'), (-3, 'admin'), (-4, 'admin');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-2, 'bobjones');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-3, 'brucewayne'), (-4, 'brucewayne');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-3, 'peterparker'), (-4, 'peterparker');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-3, 'tonystark'), (-4, 'tonystark');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-3, 'clarkkent'), (-4, 'clarkkent');


INSERT INTO room (room_id) VALUES ('room_1');
INSERT INTO room (room_id) VALUES ('room_2');
INSERT INTO room (room_id) VALUES ('room_3');



INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -17, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -18, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -19, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -20, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -21, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -22, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -23, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -24, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -25, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -26, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -27, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -28, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -29, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -30, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -31, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -32, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -33, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -34, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -35, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -36, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -37, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -38, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -39, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -40, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -41, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -42, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -43, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -44, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -45, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -46, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -47, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -48, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -49, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -50, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -51, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -52, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -53, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -54, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -55, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -56, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -57, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -58, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -59, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -60, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -61, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -62, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -63, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10, -64, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');



INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-17, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-18, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-19, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-20, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-21, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-22, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-23, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-24, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-25, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-26, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-27, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-28, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-29, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-30, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-31, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-32, 'room_1');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-33, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-34, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-35, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-36, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-37, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-38, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-39, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-40, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-41, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-42, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-43, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-44, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-45, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-46, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-47, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-48, 'room_2');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-49, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-50, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-51, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-52, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-53, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-54, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-55, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-56, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-57, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-58, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-59, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-60, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-61, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-62, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-63, 'room_3');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-64, 'room_3');


INSERT INTO access_point (is_healthy, enabled, id, room_room_id) VALUES (TRUE, TRUE, '111e4567-e89b-12d3-a456-426614174001', 'room_1');
INSERT INTO access_point (is_healthy, enabled, id, room_room_id) VALUES (TRUE, TRUE, '222e4567-e89b-12d3-a456-426614174001', 'room_2');
INSERT INTO access_point (is_healthy, enabled, id, room_room_id) VALUES (TRUE, TRUE, '333e4567-e89b-12d3-a456-426614174001', 'room_3');

INSERT INTO tempera_station
(is_healthy, enabled, access_point_id, user_username, id)
VALUES
    (TRUE, TRUE, '111e4567-e89b-12d3-a456-426614174001', 'johndoe', 'TEMP123'),
    (TRUE, TRUE, '111e4567-e89b-12d3-a456-426614174001', 'admin', 'TEMP124'),
    (TRUE, TRUE, '111e4567-e89b-12d3-a456-426614174001', 'bobjones', 'TEMP125'),
    (TRUE, TRUE, '222e4567-e89b-12d3-a456-426614174001', 'alicebrown', 'TEMP126'),
    (TRUE, TRUE, '222e4567-e89b-12d3-a456-426614174001', 'chriswilliams', 'TEMP127'),
    (TRUE, TRUE, '222e4567-e89b-12d3-a456-426614174001', 'peterparker', 'TEMP128'),
    (TRUE, TRUE, '333e4567-e89b-12d3-a456-426614174001', 'tonystark', 'TEMP129'),
    (TRUE, TRUE, '333e4567-e89b-12d3-a456-426614174001', 'brucewayne', 'TEMP130'),
    (TRUE, FALSE, '333e4567-e89b-12d3-a456-426614174001', 'clarkkent', 'TEMP131'),
    (TRUE, TRUE, '333e4567-e89b-12d3-a456-426614174001', 'manager', 'TEMP132');

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

INSERT INTO internal_record (id, group_id, project_id, start, time_end, ext_rec_start, user_name) VALUES
(-1, null, -1, '2024-05-10 09:30:00', null, '2024-05-10 09:30:00', 'johndoe');

-- possibly add older data
