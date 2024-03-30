# example adapted from github.com/hbldh/bleak

import asyncio

from bleak import BleakScanner


def detection_callback(device, advertisement_data):
    if "G4T1" in str(device.name):
        print(
            f"name: {device.name}\taddress: {device.address}\t"
            f"signal strenght (RSSI): {advertisement_data.rssi}\tad data: {advertisement_data}"
        )


async def main():
    scanner = BleakScanner(detection_callback)
    print("Scanning for BLE devices...")

    while True:
        print("Restarting scanner...")
        await scanner.start()
        await asyncio.sleep(5)  # run scanner
        await scanner.stop()


asyncio.run(main())
