#!/bin/bash
cd src || exit

OVERWRITE=$1
if [ "$#" -ne 1 ]; then
  echo "Warning: parameter OVERWRITE was not provided. Continuing as if OVERWRITE == false."
  echo "If you would like to configure the tempera access point pass 'true' as a command line argument to docker compose run:"
  echo "'$ docker compose run ble-app true'"
  echo "If not, ignore this warning, the existing config file 'config.yaml' will be used."
  python3 main.py
  exit 0
elif [ ! -f conf.yaml ] || [ "$OVERWRITE" == "true" ]; then
  python3 configure.py;
  python3 main.py
  exit 0
elif [ "$OVERWRITE" == "false" ]; then
  echo "Note: OVERWRITE == false. Using the existing config file 'config.yaml'."
  echo "If you want to update the configuration pass 'true' as a command line argument to docker compose run:"
  echo "'$ docker compose run ble-app true'"
  python3 main.py
  exit 0
fi
