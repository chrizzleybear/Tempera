# https://github.com/hbldh/bleak
# https://github.com/hbldh/bleak/blob/master/examples/service_explorer.py

import asyncio

from bleak import BleakClient

# z.B. via Device Discovery oder nRF Connect App
device_name = "Tempera SensorStation G4T1"
device_address = "5A:09:EC:F3:F8:BB"


async def main():
    async with BleakClient(device_address) as client:
        print(f"Connected to device {device_name}")
        await asyncio.sleep(3)
        print(f"INFO: Disconnecting from device {device_name} ...")
    print(f"INFO: Disconnecting from device {device_name} ...")


asyncio.run(main())
