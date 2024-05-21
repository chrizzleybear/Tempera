#!/bin/bash

BASE=$(readlink -m "$(dirname "$0")")
DOCKERBASE=$(readlink -m "$(dirname "$BASE")")

# TODO: add docs saying to enable bluetooth.service beforehand since starting it requires sudo and this script is supposed to require no manual intervetion and workarounds are a real paaaaiiinnnn.
{
  cd "$BASE" || exit
  echo -e "$(date --rfc-3339=seconds) - cron - INFO: System restart complete. If it wasn't planed check the 'access_point.log' file for possible issues and causes.\n" >> cron.log

  chmod +x "$BASE/app.sh"

  cd "$DOCKERBASE" || exit
  docker compose run --build ble-app false
}
