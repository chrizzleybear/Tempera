-- Projects
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

-- Users and roles
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, state, state_visibility) VALUES
    (TRUE, 'Admin', 'Istrator', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'admin', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PUBLIC'),
    (TRUE, 'Susi', 'Kaufgern', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user1', 'admin', '2016-01-01 00:00:00', 'DEEPWORK', 'PRIVATE'),
    (TRUE, 'Max', 'Mustermann', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user2', 'admin', '2016-01-01 00:00:00', 'OUT_OF_OFFICE', 'HIDDEN'),
    (TRUE, 'Elvis', 'The King', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'elvis', 'elvis', '2016-01-01 00:00:00', 'OUT_OF_OFFICE', 'PUBLIC');

INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES
    ('admin', 'ADMIN'), ('admin', 'EMPLOYEE'),
    ('user1', 'MANAGER'), ('user1', 'EMPLOYEE'),
    ('user2', 'EMPLOYEE'),
    ('elvis', 'ADMIN'), ('elvis', 'EMPLOYEE');

-- Time stamps of users
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
INSERT INTO userx_userx_role (userx_username, roles) VALUES
    ('johndoe', 'EMPLOYEE'), ('bobjones', 'EMPLOYEE'), ('alicebrown', 'EMPLOYEE'), ('chriswilliams', 'EMPLOYEE'), ('peterparker', 'EMPLOYEE'),
    ('tonystark', 'EMPLOYEE'), ('brucewayne', 'EMPLOYEE'), ('clarkkent', 'EMPLOYEE'), ('brucewayne', 'MANAGER');

-- Projects and contributors
INSERT INTO project_contributors (project_id, username) VALUES
     (-1, 'admin'), (-1, 'bobjones'), (-1, 'johndoe'), (-1, 'alicebrown'), (-1, 'brucewayne'), (-1, 'clarkkent'),
     (-2, 'admin'), (-2, 'bobjones'), (-2, 'johndoe'), (-2, 'alicebrown'), (-2, 'brucewayne'), (-2, 'clarkkent'),
     (-3, 'admin'), (-3, 'bobjones'), (-3, 'johndoe'), (-3, 'alicebrown'), (-3, 'brucewayne'), (-3, 'clarkkent'),
     (-4, 'admin'), (-4, 'bobjones'), (-4, 'johndoe'), (-4, 'alicebrown'), (-4, 'brucewayne'), (-4, 'clarkkent'),
     (-5, 'admin'), (-5, 'bobjones'), (-5, 'johndoe'), (-5, 'alicebrown'), (-5, 'brucewayne'), (-5, 'clarkkent'),
     (-6, 'admin'), (-6, 'bobjones'), (-6, 'johndoe'), (-6, 'alicebrown'), (-6, 'brucewayne'), (-6, 'clarkkent'),
     (-7, 'admin'), (-7, 'bobjones'), (-7, 'johndoe'), (-7, 'alicebrown'), (-7, 'brucewayne'), (-7, 'clarkkent'),
     (-8, 'admin'), (-8, 'bobjones'), (-8, 'johndoe'), (-8, 'alicebrown'), (-8, 'brucewayne'), (-8, 'clarkkent'),
     (-9, 'admin'), (-9, 'bobjones'), (-9, 'johndoe'), (-9, 'alicebrown'), (-9, 'brucewayne'), (-9, 'clarkkent'),
     (-10, 'admin'), (-10, 'bobjones'), (-10, 'johndoe'), (-10, 'alicebrown'), (-10, 'brucewayne'), (-10, 'clarkkent'),
     (-11, 'admin'), (-11, 'bobjones'), (-11, 'johndoe'), (-11, 'alicebrown'), (-11, 'brucewayne'), (-11, 'clarkkent'),
     (-12, 'admin'), (-12, 'bobjones'), (-12, 'johndoe'), (-12, 'alicebrown'), (-12, 'brucewayne'), (-12, 'clarkkent');

-- Groups
-- add some Groups to test db
INSERT INTO groupx (id, group_lead_username, description, name) VALUES
    (-1,'brucewayne', 'this is just for testing', 'testGroup1'),
    (-2,'brucewayne', 'this is also just for testing', 'testGroup2'),
    (-3,'brucewayne', 'this is also just for testing', 'outsiderGroup'),
    (-4,'brucewayne', 'this is also just for testing', 'outsiderGroup2');

-- Group members
INSERT INTO groupx_members (groups_id, members_username) VALUES
    (-1, 'johndoe'), (1, 'alicebrown'), (1, 'chriswilliams'), (1, 'admin'),
    (-2, 'johndoe'), (2, 'bobjones'), (2, 'admin'), (2, 'chriswilliams'),
    (-3, 'alicebrown'), (3, 'chriswilliams'), (3, 'admin'), (3, 'brucewayne'), (3, 'peterparker'), (3, 'tonystark'), (3, 'clarkkent'),
    (-4, 'chriswilliams'), (4, 'admin'), (4, 'brucewayne'), (4, 'peterparker'), (4, 'tonystark'), (4, 'clarkkent'), (4, 'alicebrown');

-- Rooms
INSERT INTO room (room_id) VALUES
    ('room_1'),
    ('room_2'),
    ('room_3'),
    ('room_10'),
    ('room_11'),
    ('room_12');

-- Access point for rooms
INSERT INTO access_point (enabled, id, room_room_id) VALUES
    (TRUE, '123e4567-e89b-12d3-a456-426614174001', 'room_1'),
    (FALSE, '456e4567-e89b-12d3-a456-426614174001', 'room_2'),
    (TRUE, '789e4567-e89b-12d3-a456-426614174001', 'room_3'),
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'room_10'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'room_11'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'room_12');

-- Stations in room
INSERT INTO TEMPERA_STATION (ENABLED, access_point_id, USER_USERNAME, ID) VALUES
    (TRUE, '123e4567-e89b-12d3-a456-426614174001','admin', 'tempera_station_1'),
    (FALSE,'123e4567-e89b-12d3-a456-426614174001', 'user2', 'tempera_station_disabled_2'),
    (FALSE, '123e4567-e89b-12d3-a456-426614174001', 'user1', 'tempera_station_disabled'),
    (FALSE, '123e4567-e89b-12d3-a456-426614174001', 'elvis', 'tempera_station_disabled_elvis'),
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'johndoe', 'TEMP123'),
    (TRUE, '111e4567-e89b-12d3-a456-426614174001', 'bobjones', 'TEMP125'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'alicebrown', 'TEMP126'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'chriswilliams', 'TEMP127'),
    (TRUE, '222e4567-e89b-12d3-a456-426614174001', 'peterparker', 'TEMP128'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'tonystark', 'TEMP129'),
    (TRUE, '333e4567-e89b-12d3-a456-426614174001', 'brucewayne', 'TEMP130'),
    (FALSE, '333e4567-e89b-12d3-a456-426614174001', 'clarkkent', 'TEMP131');

-- Sensor specifications for stations
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

-- Station Measurements
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
    (24.1, -10, '2024-05-11T15:45:00', 'TEMP131'),
    -- irradiance sensors
    (999.8, -2, '2024-05-15 09:02:00', 'tempera_station_1'),
    (1000.0, -11, '2024-05-10T08:30:00', 'TEMP123'),
    (1100.0, -11, '2024-05-11T10:15:00', 'TEMP125'),
    (1200.0, -11, '2024-05-11T11:30:00', 'TEMP126'),
    (1240.0, -11, '2024-05-12T12:00:00', 'TEMP127'),
    (1900.0, -11, '2024-05-12T13:15:00', 'TEMP128'),
    (9000.0, -11, '2024-05-10T14:30:00', 'TEMP129'),
    (8900.0, -11, '2024-05-11T15:45:00', 'TEMP130'),
    (8900.0, -11, '2024-05-11T15:45:00', 'TEMP131'),
    -- humidity sensors
    (40.8, -3, '2024-05-15 09:02:00', 'tempera_station_1'),
    (50.0, -12, '2024-05-10T08:30:00', 'TEMP123'),
    (55.0, -12, '2024-05-11T10:15:00', 'TEMP125'),
    (60.0, -12, '2024-05-11T11:30:00', 'TEMP126'),
    (65.0, -12, '2024-05-12T12:00:00', 'TEMP127'),
    (70.0, -12, '2024-05-12T13:15:00', 'TEMP128'),
    (75.0, -12, '2024-05-10T14:30:00', 'TEMP129'),
    (80.0, -12, '2024-05-11T15:45:00', 'TEMP130'),
    (80.0, -12, '2024-05-11T15:45:00', 'TEMP131'),
    -- nmvoc sensors
    (430.222, -4, '2024-05-15 09:02:00', 'tempera_station_1'),
    (100.0, -13, '2024-05-10T08:30:00', 'TEMP123'),
    (110.0, -13, '2024-05-11T10:15:00', 'TEMP125'),
    (120.0, -13, '2024-05-11T11:30:00', 'TEMP126'),
    (124.0, -13, '2024-05-12T12:00:00', 'TEMP127'),
    (190.0, -13, '2024-05-12T13:15:00', 'TEMP128'),
    (900.0, -13, '2024-05-10T14:30:00', 'TEMP129'),
    (890.0, -13, '2024-05-11T15:45:00', 'TEMP130'),
    (890.0, -13, '2024-05-11T15:45:00', 'TEMP131');

-- Testdata for TimeRecordService
INSERT INTO external_record (duration, start, time_end, user_username, state) VALUES
    (30, '2024-05-16 12:00:00', null, 'admin', 'DEEPWORK'),
    (3400, '2024-05-10 09:30:00', null, 'johndoe', 'DEEPWORK');
INSERT INTO internal_record (groupx_id, project_id, start, time_end, ext_rec_start, user_name) VALUES
    (null, null, '2024-05-16 12:00:00', null, '2024-05-16 12:00:00', 'admin'),
    (null,  -1, '2024-05-10 09:30:00', null, '2024-05-10 09:30:00', 'johndoe');

-- Default tips
-- 1. add ThresholdTips, a lower and upper one for each of the sensors
-- 2. add Modification reason
-- 3. add Thresholds
INSERT INTO threshold_tip (id, tip) VALUES
    (-01, 'Heizen: Nutzen Sie Heizkörper beziehungsweise die entsprechenden Bedienfelder zur Raumklimasteuerung\nSchließen von Zugluftquellen: Überprüfen Sie Fenster und Türen und schließen Sie diese, um Zugluft zu reduzieren\nSchichtung von Kleidung: Im Fall z.B. eines technischen Defekts können vorübergehend mehrere Schichten warmer Kleidung Abhilfe schaffen\n'),
    (-02, 'Lüften: Öffnen Sie Fenster und Türen in den kühleren Morgen- oder Abendstunden um frische Luft hereinzulassen\nVerwendung von Ventilatoren: Verwenden Sie Ventilatoren, um die Luftzirkulation zu verbessern und für eine kühlere Atmosphäre zu sorgen\nVerdunkelung: Schließen Sie Vorhänge oder Jalousien um die direkte Sonneneinstrahlung zu reduzieren\nVerwendung von Klimaanlagen: Wenn möglich, verwenden Sie Klimaanlagen um die Raumtemperatur effektiv zu senken.\nReduzierung interner Wärmequellen: Schalten Sie elektronische Geräte aus oder reduzieren Sie deren Nutzung, um die interne Wärmeabgabe im Raum zu minimieren.\n'),
    (-10, 'Verwendung von Luftbefeuchtern: Platzieren Sie Luftbefeuchter im Raum, um die Luftfeuchtigkeit zu erhöhen\nPflanzen: Platzieren Sie Zimmerpflanzen da diese Feuchtigkeit abgeben\nVermeidung von Lufttrocknern/Klimaanlagen: Vermeiden Sie den Einsatz von Klimaanlagen, diese können die Luftfeuchte noch weiter senken.\n'),
    (-11, 'Verwendung von Entfeuchtern: Nutzen Sie Entfeuchter um überschüssige Feuchtigkeit aus der Luft zu entfernen\nBelüftung: Lüften Sie den Raum regelmäßig um Feuchtigkeit abzuführen und die Luftzirkulation zu verbessern\nVermeidung von Wasserquellen: Reduzieren Sie die Nutzung von Wasserdampf erzeugenden Geräten wie Wasserkochern oder Luftbefeuchtern\n'),
    (-20, 'Verwendung von Dimmern: Verwenden Sie, wenn gegeben, Dimmer-Schalter, um die Helligkeit der Beleuchtung flexibel anzupassen und bei Bedarf zu reduzieren\nVerwendung von Lampenschirmen oder Diffusoren: Platzieren Sie Lampenschirme oder Diffusoren über den Lichtquellen, um das Licht zu streuen und eine weichere Beleuchtung zu erzeugen\nReduzierung der Anzahl der Lichtquellen: Schalten Sie einige Lampen oder Leuchten aus\n'),
    (-21, 'Verwendung hellerer Lichtquellen: Installieren Sie hellere Glühbirnen oder Leuchten, um die allgemeine Beleuchtung zu erhöhen.\nHinzufügen von zusätzlichen Lichtquellen: Platzieren Sie zusätzliche Lampen oder Leuchten an strategischen Stellen, um dunkle Bereiche aufzuhellen\nOptimierung der natürlichen Beleuchtung: Öffnen Sie Vorhänge oder Jalousien um mehr natürliches Licht einzulassen, und positionieren Sie Möbel so, dass sie Lichtquellen möglichst nicht verdecken\n'),
    (-30, 'Regelmäßiges Lüften: Öffnen Sie Fenster und Türen mehrmals täglich für mindestens 10 Minuten, um frische Luft hereinzulassen und abgestandene Luft auszutauschen.\nVerwendung von Luftreinigern: Setzen Sie Luftreiniger ein, um Staub, Pollen und andere Schadstoffe aus der Luft zu filtern und die Raumluftqualität zu verbessern.\nPflanzen im Raum platzieren: Stellen Sie Zimmerpflanzen auf, die die Luftqualität verbessern können und das Wohlbefinden erhöhen.\n');
INSERT INTO Modification (id, reason, time_stamp) VALUES
    (-1, 'Default threshold.', NULL);
INSERT INTO Threshold (id, default_threshold, sensor_type, threshold_type, threshold_value, modification_id, tip_id) VALUES
    (-11, TRUE, 0, 1, 20.0, 0, 00),
    (-12, TRUE, 0, 3, 19.0, 0, 00),
    (-13, TRUE, 0, 0, 24.0, 0, 01),
    (-14, TRUE, 0, 2, 25.0, 0, 01),
    (-21, TRUE, 2, 1, 50.0, 0, 10),
    (-22, TRUE, 2, 3, 40.0, 0, 10),
    (-23, TRUE, 2, 0, 60.0, 0, 11),
    (-24, TRUE, 2, 2, 70.0, 0, 11),
    (-31, TRUE, 1, 1, 220.0, 0, 20),
    (-32, TRUE, 1, 3, 200.0, 0, 20),
    (-33, TRUE, 1, 0, 540.0, 0, 21),
    (-34, TRUE, 1, 2, 600.0, 0, 21),
    (-41, TRUE, 3, 1, 11.0, 0, 30),
    (-42, TRUE, 3, 3, 10.0, 0, 30),
    (-43, TRUE, 3, 0, NULL, 0, 30),
    (-44, TRUE, 3, 2, NULL, 0, 30);



