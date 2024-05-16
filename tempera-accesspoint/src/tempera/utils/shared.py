import asyncio
import logging
import os
from pathlib import Path
from typing import Any, Dict

import sqlalchemy
import yaml
from requests.auth import HTTPBasicAuth
from sqlalchemy import create_engine

logger = logging.getLogger(f"tempera.{__name__}")


async def init_globals() -> None:
    """
    Initializes the global variables.
    Reads the config file 'conf.yaml' into a dictionary, creates the API request header and the database engine.

    :return:
    """
    global project_root
    global db_engine
    global config
    global header
    global current_station_id

    project_root = Path(__file__).parent.parent.parent.parent.resolve()
    config = await init_config()

    async with asyncio.TaskGroup() as tg:
        header = tg.create_task(init_header(config))
        engine = tg.create_task(init_engine())

    db_engine, header = engine.result(), header.result()


async def init_config() -> Dict[str, Any]:
    """

    :return:
    """
    config_file = project_root / "src" / "conf.yaml"

    logger.info(f"Loading config from file ({config_file.absolute()})")
    try:
        with open(config_file, "r") as config_file:
            conf = yaml.safe_load(config_file)
    except FileNotFoundError:
        logger.critical(
            f"Config file {config_file.absolute()} not found. "
            "Don't forget to create one with the configure.py script :)"
        )
        os._exit(0)

    if not conf:
        logger.critical(
            "Config file is empty. "
            "Make sure run configure.py to create a valid config file."
        )
        os._exit(0)

    for key in (
        "access_point_id",
        "password",
        "sending_interval",
        "user_name",
        "webserver_address",
    ):
        if key not in conf.keys():
            logger.critical(
                f"Missing parameter {key} in the configuration file. "
                "Make sure run configure.py to create a valid config file."
            )
            os._exit(0)

    return conf


async def init_header(conf: Dict[str, Any]) -> HTTPBasicAuth:
    """
    Returns the authentication objet to set as the 'headers' kwarg in any request to the web app server.

    :return:
    """
    logger.info("Creating api request header from config file parameters.")
    return HTTPBasicAuth(conf["user_name"], conf["password"])


async def init_engine() -> sqlalchemy.Engine:
    """

    :return:
    """
    database = project_root / "src" / "tempera" / "database" / "data.sqlite"

    if not Path(database).is_file():
        logger.critical(f"No database file found at {database}. Can't create engine.")
        os._exit(0)

    logger.info(f"Creating database engine from db file: {database}")
    engine = create_engine(f"sqlite:///{database}", echo=False)
    return engine
