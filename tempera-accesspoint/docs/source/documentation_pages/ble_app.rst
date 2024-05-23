=======
BLE app
=======


Configuration script
====================

A python configuration script *configure.py* is provided to configure the parameters necessary for running the BLE
application.

.. warning::
    Setting the parameters by manually writing/changing the *conf.yaml* parameter file
    can easily cause problems or program malfunction. Use the provided script.

Run the script with `python3 /path/to/configure.py` if you have the tempera-accesspoint package installed locally or
even better through the docker. The docker will run the configuration script automatically if no config file was
generated before.

.. code-block:: bash

    $ # In the project rood directory, where compose.yaml is
    $ docker compose run ble-app
    $ # If there is a config file already but you want to overwrite it,
    $ # add the true parameter to the command above.
    $ docker compose run ble-app true


BLE application
===============

Once you have created the configuration file with *configure.py*, you can run the BLE app with `python3 main.py` if
you installed the tempera-accesspoint package locally (`pip install .` in the tempera-access point directory) or run
the docker
as shown above.

.. note::

    The access point code is packaged as a python project with *pyproject.toml* and as such it can be installed
    with `pip install .` in the directory where *pyproject.toml* is found. The code requires python3.11.8 instead of the
    debian11 default which is python3.9. If you want to run it locally on you raspberry or machine without docker,
    you'll have to install python3.11 youself (see e.g., `pyenv <https://github.com/pyenv/pyenv>`_).

    The tempera-accesspoint package also provides a series of optional dependencies for things like code formatting,
    the testing API or the documentation building. They can be found in the *pyproject.toml* file and installed
    locally by specifying them after the . in the pip install command e.g., `pip install .[all]` to install all
    optional dependencies.

Logfiles and automatic restart at reboot
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The regular program logs are found in *access_point.log*.
To enable application restart when your machine is turned off and on again, setup a crontab with
the provided *setup_crontab.sh* script

.. important::

    On linux, enabling bluetooth via command line requires sudo privileges. Since the restart is supposed to be
    automatic and not require manual intervention, the permanent activation of the bluetooth service is left up to
    the user before running the application. This means, that if you want the restart on reboot functionality to
    work, you must run `sudo systemctl enable bluetooth.service` once to permanently enable bluetooth. After running
    this command, bluetooth should be automatically enabled after every restart allowing the reboot script to work.

.. code-block:: bash

    $ # Make the script executable
    $ chmod +x setup_crontab.sh
    $ # Execute it
    $ ./setup_crontab.sh

Now, at every restart of your machine the ble-app docker will be run. Logs of program restart are
stored in the *cron.log* file
