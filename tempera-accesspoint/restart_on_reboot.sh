#!/bin/bash

BASE=$(readlink -m "$(dirname "$0")")
DOCKERBASE=$(readlink -m "$(dirname "$BASE")")

{
  cd "$BASE" || exit
  echo -e "$(date --rfc-3339=seconds) - cron - INFO: System restart complete. If it wasn't planed check the 'access_point.log' file for possible issues and causes.\n" >> cron.log

  chmod +x "$BASE/app.sh"

  cd "$DOCKERBASE" || exit
  docker compose run --build ble-app false
}
