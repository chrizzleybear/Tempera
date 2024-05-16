DELETE FROM internal_record;
DELETE FROM external_record;


INSERT INTO external_record (duration, start, time_end, user_username, state) VALUES (30, '2016-01-01 00:00:00', null, 'admin', 'DEEPWORK');
INSERT INTO internal_record (groupx_id, project_id, start, time_end, ext_rec_start, user_name) VALUES (null, null, '2016-01-01 00:00:00', null, '2016-01-01 00:00:00', 'admin');
