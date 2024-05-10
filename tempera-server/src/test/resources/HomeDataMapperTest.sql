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
    (FALSE, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-09T09:00:00', '2024-05-09T10:15:00', 'admin', 'moderator', 'janedoe', 'janedoe@example.com', 'Jane', 'Doe', 'hashed_password456'),
    (TRUE, 'MEETING', 'PUBLIC', '2024-05-10T10:00:00', '2024-05-10T11:45:00', 'moderator', 'admin', 'bobjones', 'bobjones@example.com', 'Bob', 'Jones', 'hashed_password789'),
    (TRUE, 'OUT_OF_OFFICE', 'HIDDEN', '2024-05-08T15:30:00', '2024-05-08T17:00:00', 'admin', 'admin', 'alicebrown', 'alicebrown@example.com', 'Alice', 'Brown', 'hashed_password321'),
    (FALSE, 'DEEPWORK', 'PRIVATE', '2024-05-07T14:00:00', '2024-05-07T16:30:00', 'moderator', 'admin', 'chriswilliams', 'chriswilliams@example.com', 'Chris', 'Williams', 'hashed_password654');

-- these are not inside any same group as the user in question
INSERT INTO userx
(enabled, state, state_visibility, create_date, update_date, create_user_username, update_user_username, username, email, first_name, last_name, password)
VALUES
    (TRUE, 'MEETING', 'PUBLIC', '2024-05-11T10:30:00', '2024-05-11T11:45:00', 'moderator', 'admin', 'peterparker', 'peterparker@example.com', 'Peter', 'Parker', 'hashed_password987'),
    (FALSE, 'OUT_OF_OFFICE', 'PRIVATE', '2024-05-11T13:00:00', '2024-05-11T14:15:00', 'admin', 'admin', 'tonystark', 'tonystark@example.com', 'Tony', 'Stark', 'hashed_password852'),
    (TRUE, 'DEEPWORK', 'HIDDEN', '2024-05-10T15:30:00', '2024-05-10T17:00:00', 'moderator', 'admin', 'brucewayne', 'brucewayne@example.com', 'Bruce', 'Wayne', 'hashed_password753');


-- add the members to the groups

INSERT INTO groupx_members (group_id, members_username) VALUES (1, 'johndoe'), (1, 'janedoe'), (1, 'bobjones');
INSERT INTO groupx_members (group_id, members_username) VALUES (2, 'alicebrown'), (2, 'chriswilliams'), (2, 'johndoe');
INSERT INTO groupx_members (group_id, members_username) VALUES (3, 'peterparker'), (3, 'tonystark');

INSERT INTO tempera_station
(enabled, user_username, id)
VALUES
    (TRUE, 'johndoe', 'TEMP123'),
    (TRUE, 'janedoe', 'TEMP124'),
    (TRUE, 'bobjones', 'TEMP125'),
    (TRUE, 'alicebrown', 'TEMP126'),
    (TRUE, 'chriswilliams', 'TEMP127'),
    (TRUE, 'peterparker', 'TEMP128'),
    (TRUE, 'tonystark', 'TEMP129'),
    (TRUE, 'brucewayne', 'TEMP130');


INSERT INTO access_point