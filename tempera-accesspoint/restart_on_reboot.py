#!/usr/bin/env python3
from datetime import datetime
import os
from pathlib import Path

BASE = Path(__file__).parent.resolve()

with open('cron.log', "a") as clog:
    clog.write(f"[{datetime.now()} INFO] System restart complete. "
        "\tIf it wasn't planed check the 'access_point.log' for possible issues and causes.\n")

# TODO: add bluetooth activation on startup that doesn't require sudo
os.system(
    f"cd {BASE};"
    f"chmod +x {BASE / 'app.sh'};"
    "cd ..;"
    "docker compose run --build ble-app false"
)
