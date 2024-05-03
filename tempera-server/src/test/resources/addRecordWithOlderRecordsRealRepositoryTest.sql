DELETE FROM superior_time_record_subordinate_records;
DELETE FROM subordinate_time_record;
DELETE FROM superior_time_record;

INSERT INTO superior_time_record (duration, start, time_end, user_username, state, tempera_station_id) VALUES (30, '2016-01-01 00:00:00', null, 'admin', 'DEEPWORK', 'tempera_station_1');
INSERT INTO subordinate_time_record (groupx_id, id, project_id, start, time_end) VALUES (null, -1, null, '2016-01-01 00:00:00', null);
INSERT INTO superior_time_record_subordinate_records (subordinate_records_id, superior_time_record_start, superior_time_record_user_username) VALUES (-1, '2016-01-01 00:00:00', 'admin');
