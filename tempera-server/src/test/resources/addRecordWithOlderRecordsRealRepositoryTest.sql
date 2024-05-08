DELETE FROM external_record_internal_records;
DELETE FROM internal_record;
DELETE FROM external_record;

INSERT INTO external_record (duration, start, time_end, user_username, state) VALUES (30, '2016-01-01 00:00:00', null, 'admin', 'DEEPWORK');
INSERT INTO internal_record (groupx_id, id, project_id, start, time_end) VALUES (null, -1, null, '2016-01-01 00:00:00', null);
INSERT INTO external_record_internal_records (internal_records_id, external_record_start, external_record_user_username) VALUES (-1, '2016-01-01 00:00:00', 'admin');
