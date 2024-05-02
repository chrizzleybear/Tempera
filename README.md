TEMPERA - next generation time tracking
=======================================

# Running the components (Web-Server & Raspberry)

> :rocket:
> Pro tip: you can run and manage docker images & containers from your IDE like Intellij or Pycharm.
> Just select the *services* widget on your sidebar and/or open any Dockerfile or compose.yaml in your IDE.

To run the web-app back-end server and the Bluetooth-LE Raspberry app,
docker containers are provided, so you don't have to install anything locally.
If you don't already have docker on your machine, follow these [instructions](https://docs.docker.com/get-docker/).

If you have docker installed:

* If you want to run all the components

```bash
# In the project root directory (where the compose.yaml file is found)
$ docker compose run ble-app

# When you're done, hit ctrl-c to stop the containers and then shut them down
# (not shutting down or deleting the containers may lead to problems at the next start-up!)
$ docker compose down
```

> :bangbang:
> All docker containers started from a compose.yaml file share a common network. To use the API of the back-end
> container in the ble-app container set the name of the target service as host ID, docker will match
> the exact ID automatically. In other words, when prompted by the config script set e.g., hostname >> http://back-end.

> :warning:
> Don't forget to shut down all containers with *docker compose down* or else there might be database
> integrity violations at the next start-up with *docker compose up*.

> :information_source:
> Here we use *docker compose run* instead of *up* because the BLE app requires user input and is thus interactive.
> The BLE app depends on the back-end, which depends on the postgresql database. Therefore, each time
> the ble-app is run, it automatically spins up all other containers beforehand.
> Spin up means that docker builds the images defined for each component in the respective Dockerfiles across the
> project.
> Then, from each image a container is created at every *docker compose run* and destroyed at every
> *docker compose down*.

* If you want to run only the Bluetooth-LE Raspberry app

Use the command from above and comment out *depends on* in the ble-app service of the compose.yaml file
or run the Dockerfile in tempera-accesspoint directly like so

```bash
$ docker run -iv -v /var/run/dbus:/var/run/dbus tempera-ble-app
```

* If you want to run only the Web-server

```bash
# In the project root directory (where the compose.yaml file is found)
$ docker compose run back-end
```

> :information_source:
> You can reach the web-server API at localhost as usual, even if started in a container. 
