# https://github.com/hbldh/bleak
# https://github.com/hbldh/bleak/blob/master/examples/service_explorer.py

import asyncio
import sys

from bleak import BleakClient

device_address = "5A:09:EC:F3:F8:BB"
battery_notify_uuid = "00002a19-0000-1000-8000-00805f9b34fb"


# data is a byte array
def battery_notify_handler(characteristic, data):
    print(f"Battery level is: {int.from_bytes(data, byteorder='little')}")


async def main():
    async with BleakClient(device_address) as client:
        print(f"Connected to device {device_address}")
        while True:
            try:
                await asyncio.sleep(3)
                await client.start_notify(battery_notify_uuid, battery_notify_handler)
                await asyncio.sleep(10)
            except KeyboardInterrupt:
                print(f"INFO: Disconnecting from device {device_address} ...")
                await client.stop_notify(battery_notify_uuid)
                print(f"INFO: Disconnected from device {device_address}")
                sys.exit(0)


asyncio.run(main())
