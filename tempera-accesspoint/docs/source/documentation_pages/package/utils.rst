=========
Utilities
=========


Request utils
=============

Every HTTP request made in the tempera package requires equal handling of the

* making
* receiving
* response handling

of HTTP requests. To avoid duplicate code, one unified function is defined as a util and imported where needed.

.. autofunction:: tempera.utils.request_utils.make_request


Global variables
================

High level function to initialize all global variables
""""""""""""""""""""""""""""""""""""""""""""""""""""""

.. autofunction:: tempera.utils.shared.init_globals


Low level functions to initialize individual global variables
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

.. autofunction:: tempera.utils.shared.init_config
.. autofunction:: tempera.utils.shared.init_header
.. autofunction:: tempera.utils.shared.init_engine
