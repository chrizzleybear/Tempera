import logging

from bleak import BleakClient

logger = logging.getLogger(f"tempera.{__name__}")


async def detection_callback(device, advertisement_data) -> None:
    logger.info(
        f"Device[name:{device.name};address:{device.address};signal_strenght(RSSI):{advertisement_data.rssi};"
        f"ad_data:{advertisement_data}]"
    )


###################
### DEVICE INFO ###
###################


async def manufacturer_name_handler(_characteristic, data: bytes) -> None:
    logger.debug(f"Recieved bytes: {data}")
    name = data.decode()
    logger.debug(f"Device name: {name}")
    # TODO: implement save to database


async def manufacturer_serial_number_handler(_characteristic, data: bytes) -> None:
    logger.debug(f"Recieved bytes: {data}")
    number = data.decode()
    logger.debug(f"Serial number: {number}")
    # TODO: implement save to database


####################
### ELAPSED TIME ###
####################


async def elapsed_time_handler(_characteristic, data: bytes):
    work_mode = {2: "deep_work", 3: "meeting", 4: "out_of_office", 5: "present"}
    record_status = {0: "old", 7: "new"}

    logger.debug(f"Recieved bytes: {data}")
    # TODO: properly map the data
    flag = data[0]
    time_value = data[1:7]
    time_sync_source_type = data[7]
    tzdst_offset = data[8]
    # TODO: implement save to database


####################
### MEASUREMENTS ###
####################


async def temperature_notify_handler(_characteristic, data: bytes) -> None:
    """
    Expects int16
    :param _characteristic:
    :param data:
    :return:
    """
    logger.debug(f"Recieved bytes: {data}")
    logger.info(
        f"Temperature[val:{int.from_bytes(data, byteorder='little', signed=True)}]"
    )
    # TODO: implement save to database


async def irradiance_notify_handler(_characteristic, data: bytes) -> None:
    """
    Expects uint16

    :param _characteristic:
    :param data:
    :return:
    """
    logger.debug(f"Recieved bytes: {data}")
    logger.info(
        f"Irradiance[val:{int.from_bytes(data, byteorder='little', signed=False)}]"
    )
    # TODO: implement save to database


async def humidity_notify_handler(_characteristic, data: bytes) -> None:
    """
    Expects uint16

    :param _characteristic:
    :param data:
    :return:
    """
    logger.debug(f"Recieved bytes: {data}")
    logger.info(
        f"Humidity[val:{int.from_bytes(data, byteorder='little', signed=False)}]"
    )
    # TODO: implement save to database


async def nmvoc_notify_handler(_characteristic, data: bytes) -> None:
    """
    Expects ... Check with Simon how we want to implement this. Medfloat is a nonsense format. Pack an int16 and get
    float by dividing on the python end? Seems a good solution.

    :param _characteristic:
    :param data:
    :return:
    """
    logger.debug(f"Recieved bytes: {data}")
    # TODO: check with Simon how the data (i.e., format) is transmitted exactly
    logger.info(f"NMVOC[val:{'DEBUG'}]")
    # TODO: implement save to database


#####################
### NOTIFICATIONS ###
#####################


async def notify(client: BleakClient, uuid: str) -> None:
    """
    Starts the notification to the server and maps the uuid to the respective handler

    :param client:
    :param uuid:
    :return:
    """
    ### DEVICE INFO
    # TODO: move these to main to check if the connected device is the correct one via the serial number
    if "2a29" in uuid:
        await client.start_notify(uuid, manufacturer_name_handler)
    elif "2a25" in uuid:
        await client.start_notify(uuid, manufacturer_serial_number_handler)
    ###
    elif "2bf2" in uuid:
        await client.start_notify(uuid, elapsed_time_handler)
    elif "2a6e" in uuid:
        await client.start_notify(uuid, temperature_notify_handler)
    elif "2a77" in uuid:
        await client.start_notify(uuid, irradiance_notify_handler)
    elif "2a6f" in uuid:
        await client.start_notify(uuid, humidity_notify_handler)
    elif "2bd3" in uuid:
        await client.start_notify(uuid, nmvoc_notify_handler)
    # else:
    #     # raise ValueError(f"Unknown UUID: {uuid} received as input.")
    #     print(f"Unknown UUID: {uuid} received as input.")
