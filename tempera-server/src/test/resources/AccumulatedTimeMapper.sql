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



INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Admin', 'Istrator', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'admin', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PUBLIC');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Susi', 'Kaufgern', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user1', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PRIVATE');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'MANAGER');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Maria', 'Theresa', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'MariaTheresa', 'admin', '2016-01-01 00:00:00', 'OUT_OF_OFFICE', 'HIDDEN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('MariaTheresa', 'MANAGER'), ('MariaTheresa', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Elvis', 'The King', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'elvis', 'elvis', '2016-01-01 00:00:00', 'OUT_OF_OFFICE', 'PUBLIC');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'ADMIN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'EMPLOYEE');

-- some Projects
INSERT INTO project (id, is_active, name, description, manager_username) VALUES
                                                                  (-1, true, 'Serious Business', 'This project beuts you aus', 'MariaTheresa'),
                                                                  (-2, true, 'Expansion', 'This project aims to expand our operations globally.', 'MariaTheresa'),
                                                                  (-3, true, 'Innovation', 'This project focuses on fostering innovation within the company.', 'MariaTheresa'),
                                                                  (-4, true, 'Efficiency', 'This project aims to improve efficiency across all departments.', 'MariaTheresa'),
                                                                  (-5, true, 'Sustainability Initiative', 'This project aims to make our operations more environmentally friendly.', 'MariaTheresa'),
                                                                  (-6, true, 'Customer Satisfaction Improvement', 'This project focuses on enhancing customer experience and satisfaction.', 'MariaTheresa'),
                                                                  (-7, true, 'Product Development', 'This project involves developing new products to meet market demands.', 'user1'),
                                                                  (-8, true, 'Cost Reduction Initiative', 'This project aims to identify and implement cost-saving measures across the organization.', 'user1'),
                                                                  (-9, true, 'Quality Assurance Enhancement', 'This project focuses on improving the quality control processes to ensure product quality and reliability.', 'user1'),
                                                                  (-10, true, 'Marketing Campaign Launch', 'This project involves planning and executing a new marketing campaign to attract customers.', 'user1'),
                                                                  (-11, true, 'Training and Development Program', 'This project focuses on providing training and development opportunities for employees to enhance their skills and performance.', 'user1'),
                                                                  (-12, true, 'Infrastructure Upgrade', 'This project involves upgrading the company''s IT infrastructure to improve efficiency and security.', 'user1');

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
INSERT INTO userx_userx_role (userx_username, roles) VALUES ('brucewayne', 'MANAGER'), ('peterparker', 'GROUPLEAD'), ('tonystark', 'GROUPLEAD');



-- add some Groups to test db
INSERT INTO groupx (id, group_lead_username, description, name, active) VALUES (-1,'peterparker', 'this is just for testing', 'Research Team', true);
INSERT INTO groupx (id, group_lead_username, description, name, active) VALUES (-2,'peterparker', 'this is also just for testing', 'Security Team', true);
INSERT INTO groupx (id, group_lead_username, description, name, active) VALUES (-3,'tonystark', 'this is also just for testing', 'Marketing Team', true);
INSERT INTO groupx (id, group_lead_username, description, name, active) VALUES (-4,'tonystark', 'this is also just for testing', 'Expert Team', true);

-- add some of the created projects to some GroupxProject Objects:
-- add Serious Business, Expansion, Innovation, Efficiency,Sustainability and Customer Satisfaction to testGroup1
INSERT INTO groupx_project_object (group_id, project_id, is_active)
VALUES (-1, -1, true),(-2, -1, true), (-1, -2, true), (-1, -3, true), (-1, -4, true), (-1,-5, true), (-1, -6, true), (-3, -6, true);

-- add Product Development, Cost Reduction, Quality Assurance, Marketing Campaign Launch, Training and Development and Infrastructure Upgrade to testGroup2
INSERT INTO groupx_project_object (group_id, project_id, is_active)
Values (-2, -7, true), (-2, -8, true), (-2, -9, true), (-2, -10, true), (-2, -11, true), (-2, -12, true);

INSERT INTO groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username)
VALUES (-1, -1, 'admin'), (-1, -2, 'admin'), (-1, -3, 'admin'), (-1, -4, 'admin'), (-1, -5, 'admin'), (-1, -6, 'admin'), (-1, -6, 'bobjones'), (-1, -6, 'chriswilliams'), (-1, -6, 'peterparker'), (-1, -6, 'tonystark'), (-1, -6, 'brucewayne'), (-1, -6, 'clarkkent');
INSERT INTO groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username)
VALUES (-2, -7, 'johndoe'), (-2, -8, 'johndoe'), (-2, -9, 'johndoe'), (-2, -10, 'johndoe'), (-2, -11, 'johndoe'), (-2, -12, 'johndoe');


INSERT INTO groupx_members (groups_id, members_username) VALUES (-1, 'johndoe'), (-2, 'johndoe');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-1, 'alicebrown'), (-3, 'alicebrown'), (-4, 'alicebrown');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-1, 'chriswilliams'), (-2, 'chriswilliams'), (-3, 'chriswilliams'), (-4, 'chriswilliams');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-1, 'admin'), (-2, 'admin'), (-3, 'admin'), (-4, 'admin');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-2, 'bobjones');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-3, 'brucewayne'), (-4, 'brucewayne');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-3, 'peterparker'), (-4, 'peterparker');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-3, 'tonystark'), (-4, 'tonystark');
INSERT INTO groupx_members (groups_id, members_username) VALUES (-3, 'clarkkent'), (-4, 'clarkkent');


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
    (-3, -1, -2, '2024-05-16 13:00:00', '2024-05-16 13:59:59', '2024-05-16 13:00:00', 'admin'),
    (-4, null, null, '2024-05-16 14:00:00', '2024-05-17 08:59:59', '2024-05-16 14:00:00', 'admin'),
    (-5, -1, -3, '2024-05-17 09:00:00', '2024-05-17 10:59:59', '2024-05-17 09:00:00', 'admin'),
    (-6, null, null, '2024-05-17 11:00:00', '2024-05-17 14:59:59', '2024-05-17 11:00:00', 'admin'),
    (-7, -1, -1, '2024-05-17 15:00:00', '2024-05-18 09:59:59', '2024-05-17 15:00:00', 'admin'),
    (-8, null, null, '2024-05-18 10:00:00', '2024-05-18 13:59:59', '2024-05-18 10:00:00', 'admin'),
    (-9, -1, -5, '2024-05-18 14:00:00', '2024-05-18 15:59:59', '2024-05-18 14:00:00', 'admin'),
    (-10, null, null, '2024-05-18 16:00:00', '2024-05-19 08:59:59', '2024-05-18 16:00:00', 'admin'),
    (-11, -3, -6, '2024-05-19 09:00:00', '2024-05-19 13:59:59', '2024-05-19 09:00:00', 'admin'),
    (-12, -3, -6, '2024-05-19 14:00:00', null, '2024-05-19 14:00:00', 'admin'),

    (-2, -1, -1, '2024-05-10 09:30:00', '2024-05-11 09:29:59', '2024-05-10 09:30:00', 'johndoe'),
    (-13, null, null, '2024-05-11 10:00:00', '2024-05-11 12:59:59', '2024-05-11 10:00:00', 'johndoe'),
    (-14, -2, -7, '2024-05-11 13:00:00', '2024-05-12 10:59:59', '2024-05-11 13:00:00', 'johndoe'),
    (-15, null, null, '2024-05-12 11:00:00', '2024-05-12 13:59:59', '2024-05-12 11:00:00', 'johndoe'),
    (-16, -2, -8, '2024-05-12 14:00:00', '2024-05-12 16:59:59', '2024-05-12 14:00:00', 'johndoe'),
    (-17, null, null, '2024-05-13 08:00:00', '2024-05-13 10:59:59', '2024-05-13 08:00:00', 'johndoe'),
    (-18, -2, -9, '2024-05-13 11:00:00', '2024-05-14 08:59:59', '2024-05-13 11:00:00', 'johndoe'),
    (-19, null, null, '2024-05-14 09:00:00', '2024-05-14 12:59:59', '2024-05-14 09:00:00', 'johndoe'),
    (-20, -2, -10, '2024-05-14 13:00:00', '2024-05-14 16:59:59', '2024-05-14 13:00:00', 'johndoe'),
    (-21, null, null, '2024-05-15 10:00:00', '2024-05-15 14:59:59', '2024-05-15 10:00:00', 'johndoe'),
    (-22, -2, -11, '2024-05-15 15:00:00', null, '2024-05-15 15:00:00', 'johndoe');






