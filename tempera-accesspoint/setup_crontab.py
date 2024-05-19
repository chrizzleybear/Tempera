#!/usr/bin/env python3
import os
import time
from pathlib import Path

# apparently debian has cron per default https://wiki.debian.org/cron
BASE = Path(__file__).parent.resolve()

os.chmod(BASE / "restart_on_reboot.py", 0o755)
cron_job = f"@reboot {BASE / 'restart_on_reboot.py'} >/dev/null"
existing_crontab = os.popen("crontab -l >/dev/null").read()
new_crontab = existing_crontab + cron_job
os.popen(f'(echo "{new_crontab}") | crontab -')
time.sleep(1)
print(os.popen("crontab -l").read())
