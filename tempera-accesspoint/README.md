Tempera access point
====================

[![pipeline](https://git.uibk.ac.at/informatik/qe/swess24/group4/g4t1/badges/main/pipeline.svg)](https://git.uibk.ac.at/informatik/qe/swess24/group4/g4t1/-/commits/main/)
[![Code style: black](https://img.shields.io/badge/code%20style-black-000000.svg)](https://github.com/psf/black)

## Documentation

As is customary for python projects, [read the docs](https://about.readthedocs.com/?ref=readthedocs.com)
style documentation is provided for this package.

To build and visualize the documentation pages

```bash
# Install the tempera access point python package "." i.e., the code in this directory
# with the optional docs dependencies "[docs]".
$ pip install .[docs]
# Go to the docs directory where the documentation source is, and build it with the makefile
# provided by sphinx.
$ cd ./docs
$ make html
# The output is found in ./docs/build/html, just go to that directory and open the index.html
# file with a browser or IDE of your choice.
```

## Running the python components with docker

(In the project root directory) it is as simple as typing

```bash
# Since the app container depends on the back-end, this
# command will start both in the correct order and launch the app.
$ docker compose run ble-app
# Or if you want to rebuild the images before starting the app
# (like after making changes to the code)
$ docker compose run --build ble-app

# If you just want to run the back-end container.
$ docker compose run back-end
```

> :warning:
> To allow the containers to communicate amongst each other, use the name
> of the service of one container as the host. E.g., to make REST calls from
> the ble-app to the back-end set http://back-end:port as the host. The port
> must be the one exposed in the container you are trying to connect to
> (see -ports in the docker-compose.yaml).

### Test api

If you run just the api container, you can connect to the testing api in your browser
by clicking the link that appears in the terminal
when you run the container or navigate to `http://0.0.0.0:80` directly.
The host and port are set in the api `Dockerfile` *CMD* command if you want to change them (you also
have to change the mapping in the `compose.yaml` -> api service -> ports, to match the new one!).

### Details

There are 2 Dockerfile, one for each component (ble-app & api).
`compose.yaml` points to the dirs of those 2 Dockerfiles with the *build* command.
When running *docker compose [command]* it automatically executes *docker build -t [name] .*
in the directory specified with build.

API container:
> The api container must expose the api port to the host machine, thus the ports key in the api
> service (port: [host]:[docker]).

APP container:
> The app container takes user input and must therefore be run interactively. This happens with
> *docker compose run [service]* and **not** with the canonical *docker compose up* command.
> Additionally, the app container needs bluetooth to work. The best way to achieve this is to use
> the host machine bluetooth in the container. This happens with the *volumes: /var/...:/var...* command.


If you still want to run the containers individually, you can do it like so. If you use the `compose.yaml`
all the flags are take care of for you.

```bash
# api
$ docker run -p 80:80 api:latest
# app
$ docker run -itv /var/run/dbus:/var/run/dbus access_point:latest
```
