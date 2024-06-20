
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Admin', 'Istrator', 'admin.istrator@web.de',  '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'admin', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PUBLIC');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'EMPLOYEE');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'MANAGER');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'GROUPLEAD');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Susi', 'Kaufgern', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'susikaufgern', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PRIVATE');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('susikaufgern', 'MANAGER');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('susikaufgern', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Maria', 'Theresa', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'mariatheresa', 'admin', '2016-01-01 00:00:00', 'OUT_OF_OFFICE', 'HIDDEN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('mariatheresa', 'MANAGER'), ('mariatheresa', 'EMPLOYEE');
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES(TRUE, 'Elvis', 'The King', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'elvis', 'elvis', '2016-01-01 00:00:00', 'OUT_OF_OFFICE', 'PUBLIC');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'ADMIN');
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'EMPLOYEE');

-- some Projects
INSERT INTO project (id, is_active, name, description, manager_username) VALUES
                                                                  (-1, true, 'Serious Business', 'This project is all about serious business. You know.', 'mariatheresa'),
                                                                  (-2, true, 'Expansion', 'This project aims to expand our operations globally.', 'mariatheresa'),
                                                                  (-3,true, 'Innovation', 'This project focuses on fostering innovation within the company.', 'mariatheresa'),
                                                                  (-4,true, 'Efficiency', 'This project aims to improve efficiency across all departments.', 'mariatheresa'),
                                                                  (-5,true, 'Sustainability Initiative', 'This project aims to make our operations more environmentally friendly.', 'mariatheresa'),
                                                                  (-6,true, 'Customer Satisfaction Improvement', 'This project focuses on enhancing customer experience and satisfaction.', 'mariatheresa'),
                                                                  (-7,true, 'Product Development', 'This project involves developing new products to meet market demands.', 'mariatheresa'),
                                                                  (-8,true, 'Cost Reduction Initiative', 'This project aims to identify and implement cost-saving measures across the organization.', 'mariatheresa'),
                                                                  (-9, true,'Quality Assurance Enhancement', 'This project focuses on improving the quality control processes to ensure product quality and reliability.', 'mariatheresa'),
                                                                  (-10,true, 'Marketing Campaign Launch', 'This project involves planning and executing a new marketing campaign to attract customers.', 'mariatheresa'),
                                                                  (-11,true, 'Training and Development Program', 'This project focuses on providing training and development opportunities for employees to enhance their skills and performance.', 'mariatheresa'),
                                                                  (-12,true, 'Infrastructure Upgrade', 'This project involves upgrading the company''s IT infrastructure to improve efficiency and security.', 'mariatheresa');

-- these users can be used to display as colleagues for john doe
INSERT INTO userx (enabled, default_group_id, default_project_id, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password) VALUES
                                                                                                                                                                                                                       (TRUE, null, null,'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'johndoe', 'johndoe@example.com', 'John', 'Doe', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
                                                                                                                                                                                                                       (TRUE, null,null,  'MEETING', 'PUBLIC', '2024-05-10T10:00:00', '2024-05-10T11:45:00', 'bobjones', 'admin', 'bobjones', 'bobjones@example.com', 'Bob', 'Jones', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
                                                                                                                                                                                                                       (TRUE, null, null,  'OUT_OF_OFFICE', 'HIDDEN', '2024-05-08T15:30:00', '2024-05-08T17:00:00', 'admin', 'admin', 'alicebrown', 'alicebrown@example.com', 'Alice', 'Brown', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
                                                                                                                                                                                                                       (true, null, null, 'DEEPWORK', 'PRIVATE', '2024-05-07T14:00:00', '2024-05-07T16:30:00', 'chriswilliams', 'admin', 'chriswilliams', 'chriswilliams@example.com', 'Chris', 'Williams', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
                                                                                                                                                                                                                       (TRUE, null, null, 'MEETING', 'PUBLIC', '2024-05-11T10:30:00', '2024-05-11T11:45:00', 'admin', 'admin', 'peterparker', 'peterparker@example.com', 'Peter', 'Parker', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
                                                                                                                                                                                                                       (true, null, null, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-11T13:00:00', '2024-05-11T14:15:00', 'admin', 'admin', 'tonystark', 'tonystark@example.com', 'Tony', 'Stark', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
                                                                                                                                                                                                                       (TRUE, null, null, 'DEEPWORK', 'HIDDEN', '2024-05-10T15:30:00', '2024-05-10T17:00:00', 'admin', 'admin', 'brucewayne', 'brucewayne@example.com', 'Bruce', 'Wayne', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u'),
                                                                                                                                                                                                                       (TRUE, null, null, 'DEEPWORK', 'PUBLIC', '2024-05-10T12:00:00', '2024-05-10T14:30:00', 'admin', 'admin', 'clarkkent', 'clarkkent@webmail.com', 'Clark', 'Kent', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u');

INSERT INTO userx_userx_role (userx_username, roles) VALUES ('johndoe', 'EMPLOYEE'), ('bobjones', 'EMPLOYEE'), ('alicebrown', 'EMPLOYEE'), ('chriswilliams', 'EMPLOYEE'), ('peterparker', 'EMPLOYEE'), ('tonystark', 'EMPLOYEE'), ('brucewayne', 'EMPLOYEE'), ('clarkkent', 'EMPLOYEE');
INSERT INTO userx_userx_role (userx_username, roles) VALUES ('brucewayne', 'MANAGER'), ('peterparker', 'GROUPLEAD'), ('tonystark', 'GROUPLEAD');



-- add some Groups to test db
INSERT INTO public.groupx (active, id, group_lead_username, description, name) VALUES (true, -3, 'tonystark', 'this is also just for testing', 'Marketing Team');
INSERT INTO public.groupx (active, id, group_lead_username, description, name) VALUES (true, -1, 'peterparker', 'Dedicated to hard research in the field of Money.', 'Research Team');
INSERT INTO public.groupx (active, id, group_lead_username, description, name) VALUES (true, -4, 'tonystark', 'Consisting of the finest of experts, these experts sure know stuff.', 'Expert Team');
INSERT INTO public.groupx (active, id, group_lead_username, description, name) VALUES (true, -2, 'admin', 'this is also just for testing', 'Security Team');


-- add some of the created projects to some GroupxProject Objects:
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -1, -1);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -1, -2);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -1, -3);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -1, -4);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -1, -5);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -1, -6);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -2, -7);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -2, -1);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -2, -8);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -2, -9);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -2, -10);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -1, -7);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -3, -1);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -4, -2);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -4, -4);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (false, -2, -11);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -3, -3);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -4, -5);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -4, -9);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -3, -12);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -2, -12);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -3, -11);
INSERT INTO public.groupx_project_object (is_active, group_id, project_id) VALUES (true, -3, -10);



INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -1, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -1, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -2, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -3, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -4, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -5, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -6, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -6, 'bobjones');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -6, 'chriswilliams');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -6, 'peterparker');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -6, 'tonystark');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -6, 'brucewayne');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -6, 'clarkkent');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -7, 'johndoe');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -8, 'johndoe');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -9, 'johndoe');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -10, 'johndoe');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-1, -7, 'johndoe');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -7, 'chriswilliams');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -7, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -7, 'bobjones');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -7, 'clarkkent');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -1, 'bobjones');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -1, 'clarkkent');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -1, 'alicebrown');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -8, 'clarkkent');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -8, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -8, 'chriswilliams');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -10, 'admin');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -10, 'clarkkent');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -10, 'bobjones');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-2, -10, 'chriswilliams');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-3, -1, 'mariatheresa');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-3, -3, 'mariatheresa');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-3, -11, 'mariatheresa');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -4, 'mariatheresa');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -4, 'clarkkent');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -4, 'brucewayne');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -5, 'mariatheresa');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -5, 'clarkkent');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -5, 'brucewayne');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -9, 'mariatheresa');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -9, 'chriswilliams');
INSERT INTO public.groupx_project_object_contributors (groupx_projects_group_id, groupx_projects_project_id, contributors_username) VALUES (-4, -9, 'peterparker');



INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-1, 'johndoe');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-1, 'alicebrown');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-1, 'chriswilliams');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-1, 'admin');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-2, 'johndoe');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-2, 'chriswilliams');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-2, 'admin');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-2, 'bobjones');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-2, 'clarkkent');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-2, 'alicebrown');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-3, 'alicebrown');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-3, 'chriswilliams');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-3, 'admin');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-3, 'brucewayne');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-3, 'peterparker');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-3, 'tonystark');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-3, 'clarkkent');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-3, 'mariatheresa');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-4, 'alicebrown');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-4, 'chriswilliams');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-4, 'admin');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-4, 'brucewayne');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-4, 'peterparker');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-4, 'tonystark');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-4, 'clarkkent');
INSERT INTO public.groupx_members (groups_id, members_username) VALUES (-4, 'mariatheresa');




-- Testdata for TimeRecordService
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (30, '2024-05-16 12:00:00.000000', '2024-05-16 13:00:00.000000', 'admin', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (45, '2024-05-16 13:00:00.000000', '2024-05-16 15:00:00.000000', 'admin', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (90, '2024-05-16 15:00:00.000000', '2024-05-17 08:00:00.000000', 'admin', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (120, '2024-05-17 08:00:00.000000', '2024-05-17 11:00:00.000000', 'admin', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (60, '2024-05-17 11:00:00.000000', '2024-05-17 15:00:00.000000', 'admin', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (300, '2024-05-17 15:00:00.000000', '2024-05-18 09:00:00.000000', 'admin', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (200, '2024-05-18 10:00:00.000000', '2024-05-18 14:00:00.000000', 'admin', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (100, '2024-05-18 14:00:00.000000', '2024-05-18 16:00:00.000000', 'admin', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (50, '2024-05-18 16:00:00.000000', '2024-05-19 09:00:00.000000', 'admin', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (400, '2024-05-19 09:00:00.000000', '2024-05-19 14:00:00.000000', 'admin', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (3400, '2024-05-11 09:30:00.000000', '2024-05-11 10:00:00.000000', 'johndoe', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (60, '2024-05-11 10:00:00.000000', '2024-05-11 13:00:00.000000', 'johndoe', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (45, '2024-05-11 13:00:00.000000', '2024-05-11 18:00:00.000000', 'johndoe', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (45, '2024-05-11 18:00:00.000000', '2024-05-12 11:00:00.000000', 'johndoe', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (90, '2024-05-12 11:00:00.000000', '2024-05-12 14:00:00.000000', 'johndoe', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (120, '2024-05-12 14:00:00.000000', '2024-05-12 17:00:00.000000', 'johndoe', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (120, '2024-05-12 17:00:00.000000', '2024-05-12 08:00:00.000000', 'johndoe', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (30, '2024-05-13 08:00:00.000000', '2024-05-13 11:00:00.000000', 'johndoe', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (30, '2024-05-13 11:00:00.000000', '2024-05-13 17:00:00.000000', 'johndoe', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (150, '2024-05-13 17:00:00.000000', '2024-05-14 09:00:00.000000', 'johndoe', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (300, '2024-05-14 09:00:00.000000', '2024-05-14 13:00:00.000000', 'johndoe', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (200, '2024-05-14 13:00:00.000000', '2024-05-14 17:00:00.000000', 'johndoe', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (200, '2024-05-14 17:00:00.000000', '2024-05-15 10:00:00.000000', 'johndoe', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (100, '2024-05-15 10:00:00.000000', '2024-05-15 15:00:00.000000', 'johndoe', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (2578589, '2024-05-19 14:00:00.000000', '2024-06-18 10:16:29.990000', 'admin', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (13439, '2024-06-18 10:16:30.000000', '2024-06-18 14:00:29.990000', 'admin', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (7199, '2024-06-18 14:00:30.000000', '2024-06-18 16:00:29.990000', 'admin', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (3599, '2024-06-18 16:00:30.000000', '2024-06-18 17:00:29.990000', 'admin', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (53999, '2024-06-18 17:00:30.000000', '2024-06-19 08:00:29.990000', 'admin', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (7199, '2024-06-19 08:00:30.000000', '2024-06-19 10:00:29.990000', 'admin', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (7199, '2024-06-19 10:00:30.000000', '2024-06-19 12:00:29.990000', 'admin', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (7199, '2024-06-19 12:00:30.000000', '2024-06-19 14:00:29.990000', 'admin', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (17999, '2024-06-19 14:00:30.000000', '2024-06-19 19:00:29.990000', 'admin', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (40439, '2024-06-19 19:00:30.000000', '2024-06-20 06:14:29.990000', 'admin', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (3599, '2024-06-20 06:14:30.000000', '2024-06-20 07:14:29.990000', 'admin', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (17999, '2024-06-20 07:14:30.000000', '2024-06-20 12:14:29.990000', 'admin', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (14399, '2024-06-20 12:14:30.000000', '2024-06-20 16:14:29.990000', 'admin', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (7199, '2024-06-20 16:14:30.000000', '2024-06-20 18:14:29.990000', 'admin', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (45959, '2024-06-20 18:14:30.000000', '2024-06-21 07:00:29.990000', 'admin', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (1, '2024-06-21 07:00:30.000000', null, 'admin', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (403229, '2024-05-15 15:00:00.000000', '2024-05-20 07:00:29.990000', 'johndoe', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (431999, '2024-05-20 07:00:30.000000', '2024-05-25 07:00:29.990000', 'johndoe', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (345599, '2024-05-25 07:00:30.000000', '2024-05-29 07:00:29.990000', 'johndoe', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (777599, '2024-05-29 07:00:30.000000', '2024-06-07 07:00:29.990000', 'johndoe', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (691199, '2024-06-07 07:00:30.000000', '2024-06-15 07:00:29.990000', 'johndoe', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (431999, '2024-06-15 07:00:30.000000', '2024-06-20 07:00:29.990000', 'johndoe', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (86399, '2024-06-20 07:00:30.000000', '2024-06-21 07:00:29.990000', 'johndoe', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (1, '2024-06-21 07:00:30.000000', null, 'johndoe', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (691199, '2024-05-21 07:00:30.000000', '2024-05-24 07:00:29.990000', 'bobjones', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (691199, '2024-05-24 07:00:30.000000', '2024-05-26 07:00:29.990000', 'bobjones', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (691199, '2024-05-26 07:00:30.000000', '2024-05-29 07:00:29.990000', 'bobjones', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (1814399, '2024-05-29 07:00:30.000000', '2024-06-19 07:00:29.990000', 'bobjones', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (1, '2024-06-19 07:00:30.000000', null, 'bobjones', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (172799, '2024-06-01 07:00:30.000000', '2024-06-03 07:00:29.990000', 'mariatheresa', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (259199, '2024-06-03 07:00:30.000000', '2024-06-06 07:00:29.990000', 'mariatheresa', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (86399, '2024-06-06 07:00:30.000000', '2024-06-07 07:00:29.990000', 'mariatheresa', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (172799, '2024-06-07 07:00:30.000000', '2024-06-09 07:00:29.990000', 'mariatheresa', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (259199, '2024-06-09 07:00:30.000000', '2024-06-12 07:00:29.990000', 'mariatheresa', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (345599, '2024-06-12 07:00:30.000000', '2024-06-16 07:00:29.990000', 'mariatheresa', 'DEEPWORK');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (86399, '2024-06-16 07:00:30.000000', '2024-06-17 07:00:29.990000', 'mariatheresa', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (86399, '2024-06-17 07:00:30.000000', '2024-06-18 07:00:29.990000', 'mariatheresa', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (86399, '2024-06-18 07:00:30.000000', '2024-06-19 07:00:29.990000', 'mariatheresa', 'AVAILABLE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (86399, '2024-06-19 07:00:30.000000', '2024-06-20 07:00:29.990000', 'mariatheresa', 'MEETING');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (86399, '2024-06-20 07:00:30.000000', '2024-06-21 07:00:29.990000', 'mariatheresa', 'OUT_OF_OFFICE');
INSERT INTO public.external_record (duration, start, time_end, user_username, state) VALUES (1, '2024-06-21 07:00:30.000000', null, 'mariatheresa', 'AVAILABLE');


INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-16 12:00:00.000000', null, -1, null, '2024-05-16 12:00:00.000000', '2024-05-16 13:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-16 13:00:00.000000', -1, -3, -2, '2024-05-16 13:00:00.000000', '2024-05-16 15:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-16 15:00:00.000000', null, -4, null, '2024-05-16 15:00:00.000000', '2024-05-17 08:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-17 08:00:00.000000', -1, -5, -3, '2024-05-17 08:00:00.000000', '2024-05-17 11:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-17 11:00:00.000000', null, -6, null, '2024-05-17 11:00:00.000000', '2024-05-17 15:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-18 14:00:00.000000', -1, -9, -5, '2024-05-18 14:00:00.000000', '2024-05-18 16:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-18 16:00:00.000000', null, -10, null, '2024-05-18 16:00:00.000000', '2024-05-19 09:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-19 09:00:00.000000', -1, -11, -6, '2024-05-19 09:00:00.000000', '2024-05-19 14:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-11 09:30:00.000000', -1, -2, -1, '2024-05-11 09:30:00.000000', '2024-05-11 10:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-11 10:00:00.000000', null, -13, null, '2024-05-11 10:00:00.000000', '2024-05-11 12:59:59.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-11 13:00:00.000000', -2, -14, -7, '2024-05-11 13:00:00.000000', '2024-05-11 17:59:59.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-11 18:00:00.000000', -2, -23, -7, '2024-05-11 18:00:00.000000', '2024-05-12 11:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-12 11:00:00.000000', null, -15, null, '2024-05-12 11:00:00.000000', '2024-05-12 13:59:59.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-12 14:00:00.000000', -2, -16, -8, '2024-05-12 14:00:00.000000', '2024-05-12 16:59:59.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-12 17:00:00.000000', null, -17, null, '2024-05-12 17:00:00.000000', '2024-05-13 10:59:59.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-13 08:00:00.000000', -2, -18, -9, '2024-05-13 08:00:00.000000', '2024-05-13 11:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-13 11:00:00.000000', -2, -24, -9, '2024-05-13 11:00:00.000000', '2024-05-13 17:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-13 17:00:00.000000', -2, -25, -9, '2024-05-13 17:00:00.000000', '2024-05-14 09:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-14 09:00:00.000000', null, -19, null, '2024-05-14 09:00:00.000000', '2024-05-14 13:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-14 13:00:00.000000', -2, -20, -10, '2024-05-14 13:00:00.000000', '2024-05-14 17:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-14 17:00:00.000000', -2, -26, -10, '2024-05-14 17:00:00.000000', '2024-05-15 10:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-18 10:00:00.000000', -1, -8, -4, '2024-05-18 10:00:00.000000', '2024-05-18 14:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-17 15:00:00.000000', null, -99, null, '2024-05-17 16:00:00.000000', '2024-05-18 09:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-17 15:00:00.000000', null, -7, null, '2024-05-17 15:00:00.000000', '2024-05-17 16:00:00.000000', 'admin', null);

INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-19 14:00:00.000000', -1, -127, -6, '2024-05-22 14:00:00.000000', '2024-05-25 14:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-19 14:00:00.000000', -1, -128, -2, '2024-05-25 14:00:00.000000', '2024-05-29 14:00:00.000000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-19 08:00:30.000000', null, -106, null, '2024-06-19 08:00:30.000000', '2024-06-19 10:00:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-19 14:00:00.000000', -1, -129, -1, '2024-05-29 14:00:00.000000', '2024-06-18 10:16:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-19 19:00:30.000000', null, -110, null, '2024-06-19 19:00:30.000000', '2024-06-20 06:14:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-20 07:00:30.000000', -2, -117, -7, '2024-05-20 07:00:30.000000', '2024-05-23 07:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-20 07:00:30.000000', -2, -130, -9, '2024-05-23 07:00:00.000000', '2024-05-24 07:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-20 06:14:30.000000', -1, -111, -1, '2024-06-20 06:14:30.000000', '2024-06-20 07:14:29.990000', 'admin', 'Now is the time to shine :)');
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-19 14:00:30.000000', -1, -109, -3, '2024-06-19 14:00:30.000000', '2024-06-19 19:00:29.990000', 'admin', 'Look at this description');
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-19 12:00:30.000000', -1, -180, -4, '2024-06-19 12:00:30.000000', '2024-06-19 14:00:29.990000', 'admin', 'We worked really hard on this!');
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-19 10:00:30.000000', -1, -177, -3, '2024-06-19 10:00:30.000000', '2024-06-19 12:00:29.990000', 'admin', 'Some Description blabla');
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-20 07:14:30.000000', null, -112, null, '2024-06-20 07:14:30.000000', '2024-06-20 12:14:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-20 16:14:30.000000', null, -114, null, '2024-06-20 16:14:30.000000', '2024-06-20 18:14:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-07 07:00:30.000000', -2, -120, -8, '2024-06-07 07:00:30.000000', '2024-06-15 07:00:29.990000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-20 12:14:30.000000', -1, -113, -4, '2024-06-20 12:14:30.000000', '2024-06-20 16:14:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-20 18:14:30.000000', -1, -115, -6, '2024-06-20 18:14:30.000000', '2024-06-21 07:00:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-21 07:00:30.000000', -1, -116, -3, '2024-06-21 07:00:30.000000', null, 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-15 15:00:00.000000', -2, -22, -11, '2024-05-15 15:00:00.000000', '2024-05-20 07:00:29.990000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-25 07:00:30.000000', null, -118, null, '2024-05-25 07:00:30.000000', '2024-05-29 07:00:29.990000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-15 07:00:30.000000', null, -121, null, '2024-06-15 07:00:30.000000', '2024-06-20 07:00:29.990000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-19 07:00:30.000000', null, -126, null, '2024-06-19 07:00:30.000000', null, 'bobjones', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-18 17:00:30.000000', -1, -153, -4, '2024-06-18 17:00:30.000000', '2024-06-19 08:00:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-18 16:00:30.000000', -1, -142, -3, '2024-06-18 16:00:30.000000', '2024-06-18 17:00:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-18 14:00:30.000000', -1, -131, -6, '2024-06-18 14:00:30.000000', '2024-06-18 16:00:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-18 10:16:30.000000', -1, -147, -5, '2024-06-18 10:16:30.000000', '2024-06-18 14:00:29.990000', 'admin', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-19 14:00:00.000000', -1, -12, -3, '2024-05-19 14:00:00.000000', '2024-05-22 14:00:00.000000', 'admin', null);

INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-29 07:00:30.000000', -2, -219, -7, '2024-05-29 07:00:30.000000', '2024-06-04 07:00:00.000000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-29 07:00:30.000000', -1, -232, -7, '2024-06-04 07:00:00.000000', '2024-06-07 07:00:29.990000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-20 07:00:30.000000', -2, -222, -9, '2024-06-20 07:00:30.000000', '2024-06-21 07:00:29.990000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-21 07:00:30.000000', -2, -224, -1, '2024-05-21 07:00:30.000000', '2024-05-24 07:00:29.990000', 'bobjones', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-24 07:00:30.000000', -2, -290, -1, '2024-05-24 07:00:30.000000', '2024-05-26 07:00:29.990000', 'bobjones', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-26 07:00:30.000000', -2, -291, -1, '2024-05-26 07:00:30.000000', '2024-05-29 07:00:29.990000', 'bobjones', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-29 07:00:30.000000', -2, -225, -10, '2024-05-29 07:00:30.000000', '2024-06-06 07:00:30.000000', 'bobjones', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-29 07:00:30.000000', -1, -233, -6, '2024-06-06 07:00:30.000000', '2024-06-10 07:00:00.000000', 'bobjones', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-29 07:00:30.000000', -2, -234, -7, '2024-06-10 07:00:00.000000', '2024-06-19 07:00:29.990000', 'bobjones', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-21 07:00:30.000000', -2, -223, -7, '2024-06-21 07:00:30.000000', null, 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-20 07:00:30.000000', -1, -231, -1, '2024-05-24 07:00:00.000000', '2024-05-25 07:00:29.990000', 'johndoe', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-05-15 10:00:00.000000', -1, -21, -7, '2024-05-15 10:00:00.000000', '2024-05-15 15:00:00.000000', 'johndoe', null);

INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-03 07:00:30.000000', -4, -72, -9, '2024-06-03 07:00:30.000000', '2024-06-06 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-09 07:00:30.000000', -4, -75, -9, '2024-06-09 07:00:30.000000', '2024-06-12 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-16 07:00:30.000000', -3, -77, -1, '2024-06-16 07:00:30.000000', '2024-06-17 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-06 07:00:30.000000', -3, -73, -11, '2024-06-06 07:00:30.000000', '2024-06-07 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-01 07:00:30.000000', -3, -71, -3, '2024-06-01 07:00:30.000000', '2024-06-03 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-12 07:00:30.000000', -3, -76, -11, '2024-06-12 07:00:30.000000', '2024-06-16 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-18 07:00:30.000000', -3, -79, -3, '2024-06-18 07:00:30.000000', '2024-06-19 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-07 07:00:30.000000', null, -294, null, '2024-06-07 07:00:30.000000', '2024-06-09 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-17 07:00:30.000000', null, -435, null, '2024-06-17 07:00:30.000000', '2024-06-18 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-19 07:00:30.000000', null, -450, null, '2024-06-19 07:00:30.000000', '2024-06-20 07:00:29.990000', 'mariatheresa', null);
INSERT INTO public.internal_record (ext_rec_start, group_id, id, project_id, start, time_end, user_name, description) VALUES ('2024-06-20 07:00:30.000000', null, -411, null, '2024-06-20 07:00:30.000000', '2024-06-21 07:00:29.990000', 'mariatheresa', null);




-------------------------------------------MEASUREMENT CHAIN----------------------------------------
INSERT INTO public.room (access_point_id, room_id) VALUES (null, 'room_1');
INSERT INTO public.room (access_point_id, room_id) VALUES (null, 'room_2');
INSERT INTO public.room (access_point_id, room_id) VALUES (null, 'room_3');
INSERT INTO public.room (access_point_id, room_id) VALUES (null, 'room_4');
INSERT INTO public.room (access_point_id, room_id) VALUES (null, 'room_5');
INSERT INTO public.room (access_point_id, room_id) VALUES (null, 'room_6');




INSERT INTO access_point (id, is_healthy, enabled, room_room_id) VALUES ('123e4567-e89b-12d3-a456-426614174001', TRUE, TRUE, 'room_1');
INSERT INTO access_point (id, is_healthy, enabled, room_room_id) VALUES ('456e4567-e89b-12d3-a456-426614174001', FALSE, TRUE, 'room_2');
INSERT INTO access_point (id, is_healthy, enabled, room_room_id) VALUES ('789e4567-e89b-12d3-a456-426614174001', TRUE, TRUE, 'room_3');
INSERT INTO access_point (id, is_healthy, enabled, room_room_id) VALUES ('111e4567-e89b-12d3-a456-426614174001', TRUE, TRUE, 'room_4');
INSERT INTO access_point (id, is_healthy, enabled, room_room_id) VALUES ('222e4567-e89b-12d3-a456-426614174001', TRUE, TRUE, 'room_5');
INSERT INTO access_point (id, is_healthy, enabled, room_room_id) VALUES ('333e4567-e89b-12d3-a456-426614174001', TRUE, TRUE, 'room_6');

INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, TRUE, '123e4567-e89b-12d3-a456-426614174001','admin', 'tempera_station_1');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (FALSE, FALSE,'123e4567-e89b-12d3-a456-426614174001', 'brucewayne', 'tempera_station_disabled_2');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (FALSE, FALSE, '123e4567-e89b-12d3-a456-426614174001', 'susikaufgern', 'tempera_station_disabled');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (FALSE, FALSE, '123e4567-e89b-12d3-a456-426614174001', 'elvis', 'tempera_station_disabled_elvis');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, TRUE, '111e4567-e89b-12d3-a456-426614174001', 'johndoe', 'TEMP123');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, TRUE, '111e4567-e89b-12d3-a456-426614174001', 'bobjones', 'TEMP125');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, TRUE, '222e4567-e89b-12d3-a456-426614174001', 'alicebrown', 'TEMP126');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, TRUE, '222e4567-e89b-12d3-a456-426614174001', 'chriswilliams', 'TEMP127');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, TRUE, '222e4567-e89b-12d3-a456-426614174001', 'peterparker', 'TEMP128');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, TRUE, '333e4567-e89b-12d3-a456-426614174001', 'tonystark', 'TEMP129');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (TRUE, TRUE, '333e4567-e89b-12d3-a456-426614174001', 'mariatheresa', 'TEMP130');
INSERT INTO TEMPERA_STATION (IS_HEALTHY, ENABLED, access_point_id, USER_USERNAME, ID) VALUES (FALSE, FALSE, '333e4567-e89b-12d3-a456-426614174001', 'clarkkent', 'TEMP131');

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
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)
VALUES (20.0, -1, '2024-05-15 09:00:00', 'tempera_station_1'),
       (22.5, -1, '2024-05-15 10:00:00', 'tempera_station_1'),
       (19.8, -1, '2024-05-15 11:00:00', 'tempera_station_1'),
       (23.1, -1, '2024-05-15 12:00:00', 'tempera_station_1'),
       (21.4, -1, '2024-05-15 13:00:00', 'tempera_station_1'),
       (20.7, -1, '2024-05-15 14:00:00', 'tempera_station_1'),
       (22.9, -1, '2024-05-15 15:00:00', 'tempera_station_1'),
       (18.3, -1, '2024-05-15 16:00:00', 'tempera_station_1'),
       (24.5, -1, '2024-05-15 17:00:00', 'tempera_station_1'),
       (21.0, -1, '2024-05-15 18:00:00', 'tempera_station_1'),
       (20.4, -1, '2024-05-16 09:00:00', 'tempera_station_1'),
       (22.8, -1, '2024-05-16 10:00:00', 'tempera_station_1'),
       (19.6, -1, '2024-05-16 11:00:00', 'tempera_station_1'),
       (23.5, -1, '2024-05-16 12:00:00', 'tempera_station_1'),
       (21.2, -1, '2024-05-16 13:00:00', 'tempera_station_1'),
       (20.3, -1, '2024-05-16 14:00:00', 'tempera_station_1'),
       (22.7, -1, '2024-05-16 15:00:00', 'tempera_station_1'),
       (18.7, -1, '2024-05-16 16:00:00', 'tempera_station_1'),
       (24.3, -1, '2024-05-16 17:00:00', 'tempera_station_1'),
       (21.6, -1, '2024-05-16 18:00:00', 'tempera_station_1'),
       (20.0, -10, '2024-05-10T08:30:00', 'TEMP123'),
       (25.9, -10, '2024-05-11T10:15:00', 'TEMP125'),
       (22.0, -10, '2024-05-11T11:30:00', 'TEMP126'),
       (24.0, -10, '2024-05-12T12:00:00', 'TEMP127'),
       (30.0, -10, '2024-05-12T13:15:00', 'TEMP128'),
       (17.0, -10, '2024-05-10T14:30:00', 'TEMP129'),
       (24.1, -10, '2024-05-11T15:45:00', 'TEMP130'),
       (24.1, -10, '2024-05-11T15:45:00', 'TEMP131');

-- fill in measurements for all the irradiance sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)
VALUES (999.8, -2, '2024-05-15 09:02:00', 'tempera_station_1'),
       (1000.2, -2, '2024-05-15 10:15:00', 'tempera_station_1'),
       (1012.3, -2, '2024-05-15 11:30:00', 'tempera_station_1'),
       (998.4, -2, '2024-05-15 12:45:00', 'tempera_station_1'),
       (1005.6, -2, '2024-05-15 13:50:00', 'tempera_station_1'),
       (999.1, -2, '2024-05-15 14:00:00', 'tempera_station_1'),
       (1003.8, -2, '2024-05-15 15:15:00', 'tempera_station_1'),
       (1008.2, -2, '2024-05-15 16:30:00', 'tempera_station_1'),
       (999.5, -2, '2024-05-15 17:45:00', 'tempera_station_1'),
       (1001.6, -2, '2024-05-15 18:00:00', 'tempera_station_1'),
       (1001.9, -2, '2024-05-16 09:02:00', 'tempera_station_1'),
       (1002.3, -2, '2024-05-16 10:15:00', 'tempera_station_1'),
       (1006.7, -2, '2024-05-16 11:30:00', 'tempera_station_1'),
       (999.4, -2, '2024-05-16 12:45:00', 'tempera_station_1'),
       (1004.5, -2, '2024-05-16 13:50:00', 'tempera_station_1'),
       (998.7, -2, '2024-05-16 14:00:00', 'tempera_station_1'),
       (1005.1, -2, '2024-05-16 15:15:00', 'tempera_station_1'),
       (1007.8, -2, '2024-05-16 16:30:00', 'tempera_station_1'),
       (999.2, -2, '2024-05-16 17:45:00', 'tempera_station_1'),
       (1003.4, -2, '2024-05-16 18:00:00', 'tempera_station_1'),
       (1000.0, -11, '2024-05-10T08:30:00', 'TEMP123'),
       (1100.0, -11, '2024-05-11T10:15:00', 'TEMP125'),
       (1200.0, -11, '2024-05-11T11:30:00', 'TEMP126'),
       (1240.0, -11, '2024-05-12T12:00:00', 'TEMP127'),
       (1900.0, -11, '2024-05-12T13:15:00', 'TEMP128'),
       (9000.0, -11, '2024-05-10T14:30:00', 'TEMP129'),
       (8900.0, -11, '2024-05-11T15:45:00', 'TEMP130'),
       (8900.0, -11, '2024-05-11T15:45:00', 'TEMP131');

-- fill in measurements for all the humidity sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)
VALUES (40.8, -3, '2024-05-15 09:02:00', 'tempera_station_1'),
       (41.2, -3, '2024-05-15 10:15:00', 'tempera_station_1'),
       (42.3, -3, '2024-05-15 11:30:00', 'tempera_station_1'),
       (39.4, -3, '2024-05-15 12:45:00', 'tempera_station_1'),
       (43.6, -3, '2024-05-15 13:50:00', 'tempera_station_1'),
       (40.1, -3, '2024-05-15 14:00:00', 'tempera_station_1'),
       (41.8, -3, '2024-05-15 15:15:00', 'tempera_station_1'),
       (38.2, -3, '2024-05-15 16:30:00', 'tempera_station_1'),
       (40.5, -3, '2024-05-15 17:45:00', 'tempera_station_1'),
       (41.6, -3, '2024-05-15 18:00:00', 'tempera_station_1'),
       (42.1, -3, '2024-05-16 09:02:00', 'tempera_station_1'),
       (43.4, -3, '2024-05-16 10:15:00', 'tempera_station_1'),
       (39.2, -3, '2024-05-16 11:30:00', 'tempera_station_1'),
       (40.9, -3, '2024-05-16 12:45:00', 'tempera_station_1'),
       (42.5, -3, '2024-05-16 13:50:00', 'tempera_station_1'),
       (39.8, -3, '2024-05-16 14:00:00', 'tempera_station_1'),
       (41.5, -3, '2024-05-16 15:15:00', 'tempera_station_1'),
       (43.3, -3, '2024-05-16 16:30:00', 'tempera_station_1'),
       (40.6, -3, '2024-05-16 17:45:00', 'tempera_station_1'),
       (42.7, -3, '2024-05-16 18:00:00', 'tempera_station_1'),
       (50.0, -12, '2024-05-10T08:30:00', 'TEMP123'),
       (55.0, -12, '2024-05-11T10:15:00', 'TEMP125'),
       (60.0, -12, '2024-05-11T11:30:00', 'TEMP126'),
       (65.0, -12, '2024-05-12T12:00:00', 'TEMP127'),
       (70.0, -12, '2024-05-12T13:15:00', 'TEMP128'),
       (75.0, -12, '2024-05-10T14:30:00', 'TEMP129'),
       (80.0, -12, '2024-05-11T15:45:00', 'TEMP130'),
       (80.0, -12, '2024-05-11T15:45:00', 'TEMP131');

-- fill in measurements for all the nmvoc sensors
INSERT INTO measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id)
VALUES (430.222, -4, '2024-05-15 09:02:00', 'tempera_station_1'),
       (432.345, -4, '2024-05-15 10:02:00', 'tempera_station_1'),
       (428.789, -4, '2024-05-15 11:02:00', 'tempera_station_1'),
       (431.567, -4, '2024-05-15 12:02:00', 'tempera_station_1'),
       (429.123, -4, '2024-05-15 13:02:00', 'tempera_station_1'),
       (433.890, -4, '2024-05-15 14:02:00', 'tempera_station_1'),
       (427.456, -4, '2024-05-15 15:02:00', 'tempera_station_1'),
       (434.678, -4, '2024-05-15 16:02:00', 'tempera_station_1'),
       (426.234, -4, '2024-05-15 17:02:00', 'tempera_station_1'),
       (435.789, -4, '2024-05-15 18:02:00', 'tempera_station_1'),
       (431.102, -4, '2024-05-16 09:02:00', 'tempera_station_1'),
       (432.865, -4, '2024-05-16 10:02:00', 'tempera_station_1'),
       (429.234, -4, '2024-05-16 11:02:00', 'tempera_station_1'),
       (430.987, -4, '2024-05-16 12:02:00', 'tempera_station_1'),
       (428.678, -4, '2024-05-16 13:02:00', 'tempera_station_1'),
       (434.123, -4, '2024-05-16 14:02:00', 'tempera_station_1'),
       (426.789, -4, '2024-05-16 15:02:00', 'tempera_station_1'),
       (433.567, -4, '2024-05-16 16:02:00', 'tempera_station_1'),
       (425.678, -4, '2024-05-16 17:02:00', 'tempera_station_1'),
       (436.234, -4, '2024-05-16 18:02:00', 'tempera_station_1'),
       (100.0, -13, '2024-05-10T08:30:00', 'TEMP123'),
       (110.0, -13, '2024-05-11T10:15:00', 'TEMP125'),
       (120.0, -13, '2024-05-11T11:30:00', 'TEMP126'),
       (124.0, -13, '2024-05-12T12:00:00', 'TEMP127'),
       (190.0, -13, '2024-05-12T13:15:00', 'TEMP128'),
       (900.0, -13, '2024-05-10T14:30:00', 'TEMP129'),
       (890.0, -13, '2024-05-11T15:45:00', 'TEMP130'),
       (890.0, -13, '2024-05-11T15:45:00', 'TEMP131');



--some Measurements for the presentation on 20th July 2024
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18.5, -1, '2024-06-14 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-14 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-14 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-14 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.2, -1, '2024-06-14 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (210, -2, '2024-06-14 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (42, -3, '2024-06-14 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (11000, -4, '2024-06-14 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20, -1, '2024-06-14 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (280, -2, '2024-06-14 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-14 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (3000, -4, '2024-06-14 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.5, -1, '2024-06-14 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-14 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-14 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-14 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.8, -1, '2024-06-14 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (290, -2, '2024-06-14 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (45, -3, '2024-06-14 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (13000, -4, '2024-06-14 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24, -1, '2024-06-14 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (300, -2, '2024-06-14 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (40, -3, '2024-06-14 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (10000, -4, '2024-06-14 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (26, -1, '2024-06-14 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-14 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (35, -3, '2024-06-14 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (17000, -4, '2024-06-14 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23.5, -1, '2024-06-14 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-14 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (70, -3, '2024-06-14 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20000, -4, '2024-06-14 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.8, -1, '2024-06-14 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-14 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-14 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25000, -4, '2024-06-14 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20, -1, '2024-06-14 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (200, -2, '2024-06-14 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-14 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (30000, -4, '2024-06-14 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19, -1, '2024-06-15 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-15 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (48, -3, '2024-06-15 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-15 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18.5, -1, '2024-06-15 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-15 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-15 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-15 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21, -1, '2024-06-15 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (260, -2, '2024-06-15 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (47, -3, '2024-06-15 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (14000, -4, '2024-06-15 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.5, -1, '2024-06-15 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (280, -2, '2024-06-15 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-15 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (11000, -4, '2024-06-15 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24, -1, '2024-06-15 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (300, -2, '2024-06-15 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (52, -3, '2024-06-15 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (16000, -4, '2024-06-15 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (26, -1, '2024-06-15 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-15 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-15 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (3000, -4, '2024-06-15 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23.5, -1, '2024-06-15 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-15 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (40, -3, '2024-06-15 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (10000, -4, '2024-06-15 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25, -1, '2024-06-15 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-15 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (70, -3, '2024-06-15 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18000, -4, '2024-06-15 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24, -1, '2024-06-15 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (290, -2, '2024-06-15 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (42, -3, '2024-06-15 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (30000, -4, '2024-06-15 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.5, -1, '2024-06-16 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-16 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-16 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18000, -4, '2024-06-16 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21, -1, '2024-06-16 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (275, -2, '2024-06-16 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (45, -3, '2024-06-16 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20000, -4, '2024-06-16 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.5, -1, '2024-06-16 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-16 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-16 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25000, -4, '2024-06-16 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22, -1, '2024-06-16 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (280, -2, '2024-06-16 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-16 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-16 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (26, -1, '2024-06-16 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-16 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (70, -3, '2024-06-16 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (14000, -4, '2024-06-16 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23.5, -1, '2024-06-16 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (260, -2, '2024-06-16 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (40, -3, '2024-06-16 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-16 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24, -1, '2024-06-16 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (300, -2, '2024-06-16 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-16 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (10000, -4, '2024-06-16 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.5, -1, '2024-06-16 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (240, -2, '2024-06-16 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-16 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (27000, -4, '2024-06-16 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18, -1, '2024-06-16 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (200, -2, '2024-06-16 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (30, -3, '2024-06-16 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (3000, -4, '2024-06-16 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.5, -1, '2024-06-16 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (230, -2, '2024-06-16 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-16 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (16000, -4, '2024-06-16 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18.5, -1, '2024-06-17 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-17 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-17 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-17 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19, -1, '2024-06-17 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (200, -2, '2024-06-17 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-17 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-17 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.5, -1, '2024-06-17 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-17 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-17 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (10000, -4, '2024-06-17 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24, -1, '2024-06-17 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-17 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (45, -3, '2024-06-17 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20000, -4, '2024-06-17 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (26, -1, '2024-06-17 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-17 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (40, -3, '2024-06-17 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (30000, -4, '2024-06-17 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23, -1, '2024-06-17 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (260, -2, '2024-06-17 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (30, -3, '2024-06-17 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25000, -4, '2024-06-17 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22, -1, '2024-06-17 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (240, -2, '2024-06-17 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-17 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-17 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20, -1, '2024-06-17 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (230, -2, '2024-06-17 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-17 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (14000, -4, '2024-06-17 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19, -1, '2024-06-17 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (210, -2, '2024-06-17 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-17 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (13000, -4, '2024-06-17 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18.5, -1, '2024-06-17 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (200, -2, '2024-06-17 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-17 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-17 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.5, -1, '2024-06-18 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-18 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-18 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18000, -4, '2024-06-18 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18, -1, '2024-06-18 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (200, -2, '2024-06-18 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (42, -3, '2024-06-18 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-18 03:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.5, -1, '2024-06-18 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (290, -2, '2024-06-18 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-18 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (13000, -4, '2024-06-18 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.8, -1, '2024-06-18 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-18 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (57, -3, '2024-06-18 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (10000, -4, '2024-06-18 09:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24, -1, '2024-06-18 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (240, -2, '2024-06-18 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (46, -3, '2024-06-18 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20000, -4, '2024-06-18 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.5, -1, '2024-06-18 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (230, -2, '2024-06-18 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (49, -3, '2024-06-18 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (14000, -4, '2024-06-18 15:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (26, -1, '2024-06-18 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (280, -2, '2024-06-18 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (40, -3, '2024-06-18 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (3000, -4, '2024-06-18 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23.5, -1, '2024-06-18 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-18 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (30, -3, '2024-06-18 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25000, -4, '2024-06-18 21:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21, -1, '2024-06-18 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-18 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (70, -3, '2024-06-18 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-18 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.5, -1, '2024-06-18 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (260, -2, '2024-06-18 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-18 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (30000, -4, '2024-06-18 23:59:59.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.6, -1, '2024-06-19 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (222.2, -2, '2024-06-19 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (44.2, -3, '2024-06-19 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24313.2, -4, '2024-06-19 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.5, -1, '2024-06-19 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-19 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (53.9, -3, '2024-06-19 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15777.7, -4, '2024-06-19 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.8, -1, '2024-06-19 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250.4, -2, '2024-06-19 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (56.6, -3, '2024-06-19 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (14029.3, -4, '2024-06-19 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18.8, -1, '2024-06-19 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (213.1, -2, '2024-06-19 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (51.3, -3, '2024-06-19 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (16159.1, -4, '2024-06-19 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.5, -1, '2024-06-19 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (244.4, -2, '2024-06-19 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (52.4, -3, '2024-06-19 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18547.5, -4, '2024-06-19 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23.1, -1, '2024-06-19 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-19 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (59.6, -3, '2024-06-19 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (11123.3, -4, '2024-06-19 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.8, -1, '2024-06-19 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (251.8, -2, '2024-06-19 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (46.6, -3, '2024-06-19 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (29041.5, -4, '2024-06-19 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.1, -1, '2024-06-19 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (239.2, -2, '2024-06-19 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (56.1, -3, '2024-06-19 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (10926.1, -4, '2024-06-19 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.9, -1, '2024-06-19 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (216.1, -2, '2024-06-19 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (43.2, -3, '2024-06-19 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (17715.1, -4, '2024-06-19 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.1, -1, '2024-06-19 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (242.6, -2, '2024-06-19 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (54.5, -3, '2024-06-19 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (13896.1, -4, '2024-06-19 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.5, -1, '2024-06-20 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (210, -2, '2024-06-20 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-20 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-20 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21, -1, '2024-06-20 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-20 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (47, -3, '2024-06-20 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (17000, -4, '2024-06-20 10:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23, -1, '2024-06-20 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (290, -2, '2024-06-20 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (42, -3, '2024-06-20 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (13000, -4, '2024-06-20 12:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24.5, -1, '2024-06-20 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-20 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-20 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (11000, -4, '2024-06-20 14:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22, -1, '2024-06-20 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-20 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-20 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-20 16:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (26, -1, '2024-06-20 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (240, -2, '2024-06-20 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (40, -3, '2024-06-20 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (10000, -4, '2024-06-20 18:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20, -1, '2024-06-20 20:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-20 20:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (70, -3, '2024-06-20 20:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (3000, -4, '2024-06-20 20:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18.5, -1, '2024-06-20 22:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (200, -2, '2024-06-20 22:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (43, -3, '2024-06-20 22:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22000, -4, '2024-06-20 22:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19, -1, '2024-06-20 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-20 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (58, -3, '2024-06-20 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (14000, -4, '2024-06-20 23:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.5, -1, '2024-06-20 23:59:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (230, -2, '2024-06-20 23:59:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (45, -3, '2024-06-20 23:59:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-20 23:59:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.5, -1, '2024-06-21 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-21 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-21 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-21 00:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21, -1, '2024-06-21 01:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-21 01:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (40, -3, '2024-06-21 01:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-21 01:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23, -1, '2024-06-21 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-21 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (45, -3, '2024-06-21 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18000, -4, '2024-06-21 02:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19, -1, '2024-06-21 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-21 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-21 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25000, -4, '2024-06-21 04:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18.5, -1, '2024-06-21 05:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (240, -2, '2024-06-21 05:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (70, -3, '2024-06-21 05:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (27000, -4, '2024-06-21 05:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22, -1, '2024-06-21 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (200, -2, '2024-06-21 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-21 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (14000, -4, '2024-06-21 06:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.2, -1, '2024-06-21 06:06:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-21 06:06:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (48, -3, '2024-06-21 06:06:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (17000, -4, '2024-06-21 06:06:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.5, -1, '2024-06-21 06:12:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-21 06:12:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-21 06:12:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (13000, -4, '2024-06-21 06:12:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.8, -1, '2024-06-21 06:18:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-21 06:18:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (42, -3, '2024-06-21 06:18:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (11000, -4, '2024-06-21 06:18:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.5, -1, '2024-06-21 06:24:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (230, -2, '2024-06-21 06:24:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (47, -3, '2024-06-21 06:24:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (28000, -4, '2024-06-21 06:24:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23, -1, '2024-06-21 06:30:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-21 06:30:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-21 06:30:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-21 06:30:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24, -1, '2024-06-21 06:36:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (300, -2, '2024-06-21 06:36:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (44, -3, '2024-06-21 06:36:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25000, -4, '2024-06-21 06:36:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (26, -1, '2024-06-21 06:42:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-21 06:42:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (30, -3, '2024-06-21 06:42:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (3000, -4, '2024-06-21 06:42:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.5, -1, '2024-06-21 06:54:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (260, -2, '2024-06-21 06:54:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (52, -3, '2024-06-21 06:54:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18000, -4, '2024-06-21 06:54:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.5, -1, '2024-06-21 07:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-21 07:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-21 07:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (15000, -4, '2024-06-21 07:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -2, '2024-06-21 07:10:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (230, -2, '2024-06-21 07:20:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21.2, -1, '2024-06-21 07:10:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-21 07:10:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25000, -4, '2024-06-21 07:10:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23, -1, '2024-06-21 07:20:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (57, -3, '2024-06-21 07:20:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (28000, -4, '2024-06-21 07:20:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.8, -1, '2024-06-21 06:48:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (200, -2, '2024-06-21 06:48:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (65, -3, '2024-06-21 06:48:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (14000, -4, '2024-06-21 06:48:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24.8, -1, '2024-06-21 07:30:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (19.8, -1, '2024-06-21 07:05:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (250, -2, '2024-06-21 07:05:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-21 07:05:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20000, -4, '2024-06-21 07:05:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.6, -1, '2024-06-21 07:15:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-21 07:15:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (53, -3, '2024-06-21 07:15:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (16000, -4, '2024-06-21 07:15:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (24.3, -1, '2024-06-21 07:25:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (260, -2, '2024-06-21 07:25:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (42, -3, '2024-06-21 07:25:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18000, -4, '2024-06-21 07:25:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (280, -2, '2024-06-21 07:30:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (65, -3, '2024-06-21 07:30:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22000, -4, '2024-06-21 07:30:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (25.5, -1, '2024-06-21 07:35:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (220, -2, '2024-06-21 07:35:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (58, -3, '2024-06-21 07:35:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (3000, -4, '2024-06-21 07:35:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23.7, -1, '2024-06-21 07:40:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (280, -2, '2024-06-21 07:40:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (48, -3, '2024-06-21 07:40:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (21000, -4, '2024-06-21 07:40:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.5, -1, '2024-06-21 07:45:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-21 07:45:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (50, -3, '2024-06-21 07:45:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (12000, -4, '2024-06-21 07:45:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (23, -1, '2024-06-21 07:50:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (230, -2, '2024-06-21 07:50:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-21 07:50:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (13000, -4, '2024-06-21 07:50:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (22.5, -1, '2024-06-21 07:55:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (260, -2, '2024-06-21 07:55:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (60, -3, '2024-06-21 07:55:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (16000, -4, '2024-06-21 07:55:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (20.3, -1, '2024-06-21 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (270, -2, '2024-06-21 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (55, -3, '2024-06-21 08:00:00.000000', 'tempera_station_1');
INSERT INTO public.measurement (measurement_value, sensor_sensor_id, timestamp, sensor_tempera_id) VALUES (18000, -4, '2024-06-21 08:00:00.000000', 'tempera_station_1');


-- Default tips
-- 1. add ThresholdTips, a lower and upper one for each of the sensors
-- 2. add Modification reason
-- 3. add Thresholds
INSERT INTO threshold_tip (id, tip) VALUES
                                        (-1, 'Heizen: Nutzen Sie Heizkörper beziehungsweise die entsprechenden Bedienfelder zur Raumklimasteuerung\nSchließen von Zugluftquellen: Überprüfen Sie Fenster und Türen und schließen Sie diese, um Zugluft zu reduzieren\nSchichtung von Kleidung: Im Fall z.B. eines technischen Defekts können vorübergehend mehrere Schichten warmer Kleidung Abhilfe schaffen\n'),
                                        (-2, 'Lüften: Öffnen Sie Fenster und Türen in den kühleren Morgen- oder Abendstunden um frische Luft hereinzulassen\nVerwendung von Ventilatoren: Verwenden Sie Ventilatoren, um die Luftzirkulation zu verbessern und für eine kühlere Atmosphäre zu sorgen\nVerdunkelung: Schließen Sie Vorhänge oder Jalousien um die direkte Sonneneinstrahlung zu reduzieren\nVerwendung von Klimaanlagen: Wenn möglich, verwenden Sie Klimaanlagen um die Raumtemperatur effektiv zu senken.\nReduzierung interner Wärmequellen: Schalten Sie elektronische Geräte aus oder reduzieren Sie deren Nutzung, um die interne Wärmeabgabe im Raum zu minimieren.\n'),
                                        (-10, 'Verwendung von Luftbefeuchtern: Platzieren Sie Luftbefeuchter im Raum, um die Luftfeuchtigkeit zu erhöhen\nPflanzen: Platzieren Sie Zimmerpflanzen da diese Feuchtigkeit abgeben\nVermeidung von Lufttrocknern/Klimaanlagen: Vermeiden Sie den Einsatz von Klimaanlagen, diese können die Luftfeuchte noch weiter senken.\n'),
                                        (-11, 'Verwendung von Entfeuchtern: Nutzen Sie Entfeuchter um überschüssige Feuchtigkeit aus der Luft zu entfernen\nBelüftung: Lüften Sie den Raum regelmäßig um Feuchtigkeit abzuführen und die Luftzirkulation zu verbessern\nVermeidung von Wasserquellen: Reduzieren Sie die Nutzung von Wasserdampf erzeugenden Geräten wie Wasserkochern oder Luftbefeuchtern\n'),
                                        (-20, 'Verwendung von Dimmern: Verwenden Sie, wenn gegeben, Dimmer-Schalter, um die Helligkeit der Beleuchtung flexibel anzupassen und bei Bedarf zu reduzieren\nVerwendung von Lampenschirmen oder Diffusoren: Platzieren Sie Lampenschirme oder Diffusoren über den Lichtquellen, um das Licht zu streuen und eine weichere Beleuchtung zu erzeugen\nReduzierung der Anzahl der Lichtquellen: Schalten Sie einige Lampen oder Leuchten aus\n'),
                                        (-21, 'Verwendung hellerer Lichtquellen: Installieren Sie hellere Glühbirnen oder Leuchten, um die allgemeine Beleuchtung zu erhöhen.\nHinzufügen von zusätzlichen Lichtquellen: Platzieren Sie zusätzliche Lampen oder Leuchten an strategischen Stellen, um dunkle Bereiche aufzuhellen\nOptimierung der natürlichen Beleuchtung: Öffnen Sie Vorhänge oder Jalousien um mehr natürliches Licht einzulassen, und positionieren Sie Möbel so, dass sie Lichtquellen möglichst nicht verdecken\n'),
                                        (-30, 'Regelmäßiges Lüften: Öffnen Sie Fenster und Türen mehrmals täglich für mindestens 10 Minuten, um frische Luft hereinzulassen und abgestandene Luft auszutauschen.\nVerwendung von Luftreinigern: Setzen Sie Luftreiniger ein, um Staub, Pollen und andere Schadstoffe aus der Luft zu filtern und die Raumluftqualität zu verbessern.\nPflanzen im Raum platzieren: Stellen Sie Zimmerpflanzen auf, die die Luftqualität verbessern können und das Wohlbefinden erhöhen.\n');
INSERT INTO Modification (id, reason, time_stamp) VALUES
    (-1, 'Default threshold.', NULL);

INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 20,            -1, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 18,            -2, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 26,            -3, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 28,            -4, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 40,            -5, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 30,            -6, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 65,            -7, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 75,            -8, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 220,          -9, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 200,          -10, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 540,          -11, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 600,          -12, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 10000,      -13, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 5000,        -14, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 1000000,  -15, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (true, 1000000,  -16, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');

INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 20,      -17, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 18,      -18, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 26,      -19, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 28,      -20, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 40,      -21, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 30,      -22, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 65,      -23, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 75,      -24, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 220,     -25, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 200,     -26, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 540,     -27, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 600,     -28, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10000,   -29, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 5000,    -30, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -31, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -32, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');

INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 20,      -33, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 18,      -34, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 26,      -35, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 28,      -36, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 40,      -37, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 30,      -38, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 65,      -39, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 75,      -40, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 220,     -41, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 200,     -42, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 540,     -43, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 600,     -44, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10000,   -45, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 5000,    -46, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -47, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -48, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');

INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 20,     -49, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 18,     -50, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 26,     -51, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 28,     -52, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 40,     -53, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 30,     -54, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 65,     -55, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 75,     -56, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 220,    -57, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 200,    -58, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 540,    -59, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 600,    -60, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10000,  -61, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 5000,   -62, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000,-63, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000,-64, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');

INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 20,      -65, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 18,      -66, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 26,      -67, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 28,      -68, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 40,      -69, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 30,      -70, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 65,      -71, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 75,      -72, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 220,     -73, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 200,     -74, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 540,     -75, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 600,     -76, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10000,   -77, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 5000,    -78, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -79, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -80, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');

INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 20,      -81, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 18,      -82, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 26,      -83, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 28,      -84, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 40,      -85, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 30,      -86, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 65,      -87, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 75,      -88, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 220,     -89, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 200,     -90, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 540,     -91, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 600,     -92, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10000,   -93, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 5000,    -94, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -95, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -96, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');

INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 20,      -97, -1, -1, 'TEMPERATURE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 18,      -98, -1, -1, 'TEMPERATURE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 26,      -99, -1, -2, 'TEMPERATURE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 28,      -100, -1, -2, 'TEMPERATURE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 40,      -101, -1, -10, 'HUMIDITY', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 30,      -102, -1, -10, 'HUMIDITY', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 65,      -103, -1, -11, 'HUMIDITY', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 75,      -104, -1, -11, 'HUMIDITY', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 220,     -105, -1, -20, 'IRRADIANCE', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 200,     -106, -1, -20, 'IRRADIANCE', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 540,     -107, -1, -21, 'IRRADIANCE', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 600,     -108, -1, -21, 'IRRADIANCE', 'UPPERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 10000,   -109, -1, -30, 'NMVOC', 'LOWERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 5000,    -110, -1, -30, 'NMVOC', 'LOWERBOUND_WARNING');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -111, -1, -30, 'NMVOC', 'UPPERBOUND_INFO');
INSERT INTO public.threshold (default_threshold, threshold_value, id, modification_id, tip_id, sensor_type, threshold_type) VALUES (false, 1000000, -112, -1, -30, 'NMVOC', 'UPPERBOUND_WARNING');


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
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-65, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-66, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-67, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-68, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-69, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-70, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-71, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-72, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-73, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-74, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-75, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-76, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-77, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-78, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-79, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-80, 'room_4');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-81, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-82, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-83, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-84, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-85, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-86, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-87, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-88, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-89, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-90, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-91, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-92, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-93, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-94, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-95, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-96, 'room_5');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-97, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-98, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-99, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-100, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-101, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-102, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-103, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-104, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-105, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-106, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-107, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-108, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-109, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-110, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-111, 'room_6');
INSERT INTO public.room_thresholds (thresholds_id, room_room_id) VALUES (-112, 'room_6');


INSERT INTO alert (ACKNOWLEDGED, PEAK_DEVIATION_VALUE, FIRST_INCIDENT, ID, LAST_INCIDENT, SENSOR_SENSOR_ID, THRESHOLD_ID, SENSOR_TEMPERA_ID)
VALUES (FALSE, 9000, '2024-05-10T08:00:00', -1, '2024-05-10T08:29:00', -10, -14, 'TEMP123');


