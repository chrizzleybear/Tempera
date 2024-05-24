========
Database
========

Here, an overview of the models used to store the data associated with tempera stations is provided.

Entities
========

.. autoclass:: tempera.database.entities.TemperaStation
    :members:
    :undoc-members:
.. autoclass:: tempera.database.entities.Measurement
    :members:
    :undoc-members:
.. autoclass:: tempera.database.entities.TimeRecord
    :members:
    :undoc-members:


Enums
=====

.. autoclass:: tempera.database.entities.Mode
    :members:
    :undoc-members:


SQLAlchemy base class
=====================

.. autoclass:: tempera.database.entities.Base


Creating the schemas in the database
====================================

Execute the *main* function of the **ddl.py** module to create the defined entities in the database
with ``python3 ddl.py`` (in the database directory or adjust the file path).
