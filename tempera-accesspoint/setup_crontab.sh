#!/bin/bash

# apparently debian has cron per default https://wiki.debian.org/cron
BASE=$(readlink -m "$(dirname $0)")
{
  chmod +x "$BASE/setup_crontab.sh"
  crontab -l > cron_jobs
  echo "@reboot $BASE/restart_on_reboot.sh >/dev/null" >> cron_jobs
  crontab cron_jobs
  rm cron_jobs
  crontab -l
}
