#!/bin/bash
cd ./src || exit

OVERWRITE=$1
if [ ! -f conf.yaml ]; then
  echo -e "\nNo configuration file 'conf.yaml' found. Launching configuration script.\n\n"
  sleep 3s
  python3 configure.py;
  python3 main.py
  exit 0
elif [ "$#" -ne 1 ]; then
  echo "Warning: this script takes exactly one parameter, but more/less were provided."
  echo "Continuing as if OVERWRITE == false. If you would like to configure the tempera access point"
  echo "pass exactly one parameter ('true') as a command line argument to docker compose run:"
  echo "'$ docker compose run ble-app true'"
  echo "If not, ignore this warning. The existing config file 'config.yaml' will be used."
  sleep 5s
  python3 main.py
  exit 0
elif [ "$OVERWRITE" == "true" ]; then
  python3 configure.py;
  python3 main.py
  exit 0
elif [ "$OVERWRITE" == "false" ]; then
  echo "Note: OVERWRITE == false. Using the existing config file 'config.yaml'."
  echo "If you want to update the configuration pass 'true' as a command line argument to docker compose run:"
  echo "'$ docker compose run ble-app true'"
  sleep 5s
  python3 main.py
  exit 0
fi
