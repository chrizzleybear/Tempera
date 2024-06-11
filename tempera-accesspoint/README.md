Tempera access point
====================

[![Code style: black](https://img.shields.io/badge/code%20style-black-000000.svg)](https://github.com/psf/black)

## Documentation

As is customary for python projects, [read the docs](https://about.readthedocs.com/?ref=readthedocs.com)
style documentation is provided for this package.

To build and visualize the documentation pages

```bash
# Install the tempera access point python package "." i.e., the code in this directory
# with the optional docs dependencies "[docs]".
$ pip install .[docs]

# Go to the docs directory, and build it with the makefile provided by sphinx.
$ cd ./docs
$ make html
# The output is found in ./docs/build/html, just go to that directory and open the index.html
# file with a browser or IDE of your choice.
```

## Raspberry setup

Networking on the raspberry pi was set up with NetworkManager. This means that connecting to
a WIFI network is very easy (and doesn't involve editing the wpa_supplicant.conf).
With GUI access to the raspberry or with ssh over LAN (ethernet) open a terminal and run
`sudo nmtui`. The text user interface of the NetworkManager will open and you'll be able to
connect to networks in a very intuitive manner. You can then ssh over WIFI or LAN, as you wish.
In some cases, ssh over WIFI may throw BleakDBusError caused by Software error. If you run
into such problems, try the ssh over LAN way.

> :warning:
> Remember to run the `setup_crontab.sh` script to enable the BLE app when the raspberry is rebooted.
