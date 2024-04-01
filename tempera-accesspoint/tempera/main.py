import asyncio
import sys

from bleak import BleakClient

from api.poster import post
from bleclient.device_notify import start_notify, stop_notify

device_address = "5A:09:EC:F3:F8:BB"

uuids = {
    "BATTERY_LEVEL": "00002a19-0000-1000-8000-00805f9b34fb",
    "TEMPERATURE": "0x272F",  # in deg C
    "AIRQUALITY": "",
    "LIGHTINTENSITY": "",
    "HUMIDITY": "",
}

data_collection_interval = 10
data_sending_interval = 5


async def main():
    # TODO: adapt to multiple connected devices
    async with BleakClient(
        device_address,
        timeout=120,
        services=list(uuids.values()),
        disconnected_callback=stop_notify,
    ) as client:
        print(f"Connected to device {device_address}.")

        while True:
            for val in uuids.values():
                await start_notify(client, val)

            await asyncio.sleep(data_collection_interval)

            for val in uuids.values():
                await post(val)

            await asyncio.sleep(data_sending_interval)

    sys.exit("Failed to connect to the ble server")


if __name__ == "__main__":
    asyncio.run(main())
