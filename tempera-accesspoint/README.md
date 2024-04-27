# Running the python components with docker

### BLE app

* Build the image

```bash
# In the tempera-accesspoint directory
$ docker build -t access_point .
```

* Run the image (your bluetooth is used by the docker container because it doesn't have one, ergo the -v /var...).

```bash
$ docker run -itv /var/run/dbus:/var/run/dbus access_point:latest
```

### Testing API

```bash
# In the tempera-accesspoint/src/tempera/api directory
$ docker build -t api .
$ docker run -p 80:80 api:latest
```

You can connect to the testing api in your browser by clicking the link that appears in the terminal
when you run the command above or navigate to `http://0.0.0.0:80`.
