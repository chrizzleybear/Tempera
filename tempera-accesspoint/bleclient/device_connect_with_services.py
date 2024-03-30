# https://github.com/hbldh/bleak
# https://github.com/hbldh/bleak/blob/master/examples/service_explorer.py

import asyncio

from bleak import BleakClient

device_address = "5A:09:EC:F3:F8:BB"


async def main():
    async with BleakClient(device_address) as client:
        print("Connected to device {0}".format(device_address))

        # print all services and all characteristics provided by device
        for service in client.services:  # iterate all defined services on peripheral

            print("Serivce: {0}".format(service))

            for (
                characteristic
            ) in service.characteristics:  # print the characteristics of the service
                print(
                    "Characteristic: {0} \n\twith properties: {1}".format(
                        characteristic, ", ".join(characteristic.properties)
                    )
                )
                try:
                    value = await client.read_gatt_char(characteristic.uuid)
                    print("Value is: {0}".format(value))
                except Exception as e:
                    print(
                        "ERROR: reading characteristic {0}. Error is {1}".format(
                            characteristic, e
                        )
                    )

                for descriptor in characteristic.descriptors:
                    try:
                        value = await client.read_gatt_descriptor(descriptor.handle)
                        print("Descriptor {0} says: {1}".format(descriptor, value))
                    except Exception as e:
                        print(
                            "ERROR: reading descriptor {0}. Error is {1}".format(
                                descriptor, e
                            )
                        )
                print()
            print("================\n")

        print("INFO: Disconnecting from device {0} ...".format(device_address))
    print("INFO: Disconnected from device {0}".format(device_address))


asyncio.run(main())
