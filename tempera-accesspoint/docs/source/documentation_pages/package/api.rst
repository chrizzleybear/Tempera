===
API
===


REST
====

High level coordinating/wrapper functions
-----------------------------------------

.. autofunction:: tempera.api.send.send_data
.. autofunction:: tempera.api.send.send_measurements_and_time_records

Low level helper functions
----------------------------

.. autofunction:: tempera.api.send._get_from_database
.. autofunction:: tempera.api.send._build_payload
.. autofunction:: tempera.api.send._safe_delete_data


Testing API
===========

Models
------

.. autoclass:: tempera.api._test_api.Measurement
.. autoclass:: tempera.api._test_api.ScanOrder
.. autoclass:: tempera.api._test_api.TemperaStation
.. autoclass:: tempera.api._test_api.TimeRecord
.. autoclass:: tempera.api._test_api.ValidDevices

Endpoints
---------

.. autofunction:: tempera.api._test_api.check_access_point_id
.. autofunction:: tempera.api._test_api.check_credentials
.. autofunction:: tempera.api._test_api.get_active_station_ids
.. autofunction:: tempera.api._test_api.get_scan_order
.. autofunction:: tempera.api._test_api.post_measurements
.. autofunction:: tempera.api._test_api.root
