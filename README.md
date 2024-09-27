TEMPERA - next generation time tracking
=======================================

[![pipeline](https://git.uibk.ac.at/informatik/qe/swess24/group4/g4t1/badges/main/pipeline.svg)](https://git.uibk.ac.at/informatik/qe/swess24/group4/g4t1/-/commits/main/)

# Running the components (Angular front-end, Java back-end & Python raspberry) with docker

---

> :rocket:
> Pro tip: you can run and manage docker images & containers from your IDE like Intellij or Pycharm.
> Just select the *services* widget on your sidebar and/or open any Dockerfile or compose.yaml in your IDE.

To run the web-app and the Bluetooth-LE Raspberry app,
docker images are provided, so you don't have to install anything locally.
If you don't already have docker on your machine, follow these [instructions](https://docs.docker.com/get-docker/) and
come back. If you are having trouble installing docker or any other docker/ci-cd relaetd issue,
you can always write an email to Leonardo.Pedri@uibk.ac.at :slight_smile:.

---

### Environment variables

To run the docker containers for the components, you first have to set environment variables as you would do
running the components directly on your machine. The variables to be set are:

* SWE_DB_KEY *(can be chosen freely)*
* EMAIL_KEY
 
> *The* SWE_DB_KEY *can be chosen freely.*
> 
> *The* EMAIL_KEY *must be the key to the spring.mail.username in the application.properties file. You can set your 
> own email there and use your password here*

You can do this by either adding `export SWE_DB_KEY=...` to your .bashrc or analogue and re-sourcing the shell 
(`source ~/.bashrc`), or by creating a docker run configuration in your IDE (by clicking the green arrow next to the 
services in the *compose.yaml* file) and setting the environment variables as you would for a maven or spring boot
configuration.

---

The compose.yaml file is designed to have the web-app (with its database) and the BLE app to be independent from each
other,
because you'll likely want to run them on separate machines. Here, independent means that you can create the container
of one
service (services are defined in compose.yaml) without creating it for the other. The same goes for running the
containers.

In practice, it looks like this

```bash
# In the project root directory (where the compose.yaml file is found)


# 1. To build/pull the image and create the container for the web-app/ble-app services from the compose.yaml
# If you want to use all services
$ docker compose create

# If you want to use only a specific service e.g., web-app
$ docker compose create <service>

# If you want to re-build the services (due to e.g., code/dependency changes)
$ docker compose create --build <service>

# (Optional) Check the images you built
$ docker image ls

# Note: The web-app depends on the back-end & the database, meaning that starting the web-app
# will start all the dependency services. In the same way, the back-end depends on the database.


# 2. Start the container of the service created and all its dependencies
# web-app (automatically starts back-end + database as well)
# => service reachable at localhost:4200
$ docker compose start web-app && docker compose attach web-app

# back-end (you can also choose to start just the back-end + database without the web-app)
# => service reachable at localhost:8080
$ docker compose start back-end && docker compose attach back-end

# Note: If you have postgres running on your machine, the default port 5432
# will be used by that process and you'll get an error like this:
# Error response from daemon: driver failed programming external connectivity on 
# endpoint app-postgres_db-1 (...): Error starting userland proxy: listen tcp4 0.0.0.0:5432: bind: address already in use
# In this case switch to the postgres user and stop the process using that port
$ su postgres
$ pg_ctl stop -D /var/lib/postgres/data/

# Ble-app
# Note: the BLE app requires user input in the config file. To be run in interactive mode, you have to use docker run.
# Important: if you are trying to send the data from the ble-app container to the back-end container, you have to set
# the hostname in the BLE configure script to the name of the service you are trying to reach, i.e., back-end:8080.
# If the server runs on your machine, set it to localhost:<port> as usual (see the note below for more info on this).
$ docker compose run ble-app


# 3. When you're done, hit ctrl-c to stop the containers currently active in your terminal. Then, delete the containers.
# Note: not deleting up the containers after use may lead to problems at the next start-up!
$ docker compose down


# (4. If you want to start a sevice again, just repeat steps 1 to 3)
```

> :mega:
> All docker containers started from a compose.yaml file share a common network. To use the API of the back-end
> container in the ble-app container set the name of the target service as host ID, docker will match
> the exact ID automatically. In other words, when prompted by the config script set e.g.,
> hostname >> http://back-end:8080.
> If you want to connect from your machine to a container or from a container to your machine, use localhost:port as
> usual.


# Running the components (Angular front-end, Java back-end) without docker
Alternatively you can setup angular and postgres locally on your machine without a container like this:

## Run Angular:
go to folder webapp
install node-packages: npm install
start angular: ng serve



## Setup DB Connection to postgres:
** On Linux/ Mac: **

Download psql
set environment variable SWE_DB_KEY to your password (choose one)
go to: tempera-server/src/main/resources
run the shellscript db_init.sh


** On Windows: **
Download psql
set environment variable SWE_DB_KEY to your password (choose one) 

run the following lines in your terminal:

CREATE USER g4t1 WITH PASSWORD <your Password>;
CREATE DATABASE tempera OWNER g4t1;



## Run Spring-Boot Project:

go to: /tempera-server
run: mvn spring-boot:run



# Exploring the Web-App

The web-app is reachable at localhost:4200.  We advise you to use one of the following two login credentials to explore the app:

username: admin
password:  passwd
purpose: admin user with all rights  - best for user management and tempera/ room configuration

username: mariatheresa
password: passwd
purpose: manager - best for exploring project & group management as well as accumulated time statistics

