INSERT INTO station(station_id, enabled)
VALUES (1, TRUE),
       (2, FALSE);


INSERT INTO sensor(sensor_id, type, station_id)
VALUES (1, 'TEMPERATURE', 1),
       (2, 'AIRQUALITY', 1),
       (3, 'LIGHTINTENSITY', 1),
       (4, 'HUMIDITY', 1),
       (5, 'TEMPERATURE', 2),
       (6, 'AIRQUALITY', 2),
       (7, 'LIGHTINTENSITY', 2),
       (8, 'HUMIDITY', 2);

INSERT INTO measurement(measurement_id, sensor_id, value, timestamp)
VALUES (1, 1, 24.7, '2024-03-30 18:25:25'),
       (2, 2, 0.94, '2024-03-30 18:25:25'),
       (3, 3, 0.74, '2024-03-30 18:25:25'),
       (4, 4, 43.1, '2024-03-30 18:25:25'),
       (5, 5, 25.3, '2024-03-30 18:25:26'),
       (7, 6, 0.90, '2024-03-30 18:25:26'),
       (8, 7, 0.67, '2024-03-30 18:25:26'),
       (9, 8, 58.4, '2024-03-30 18:25:26');
