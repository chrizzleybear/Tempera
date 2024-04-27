import asyncio
import logging
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

    project_root = Path(__file__).parent.parent.parent.resolve()
    config = await init_config()

    async with asyncio.TaskGroup() as tg:
        header = tg.create_task(init_header(config))
        engine = tg.create_task(init_engine())

    db_engine, header = engine.result(), header.result()


async def init_config() -> Dict[str, Any]:
    config_file = project_root / "conf.yaml"

    if not config_file.is_file():
        logger.critical(
            f"{config_file.absolute()} not found. Couldn't load configuration. "
        )
        raise FileNotFoundError

    logger.info(f"Loading config from file ({config_file.absolute()})")
    with open(config_file, "r") as config_file:
        conf = yaml.safe_load(config_file)

    if not conf:
        logger.critical(f"Config file is empty.")
        raise RuntimeError

    for key in (
        "access_point_id",
        "password",
        "sending_interval",
        "user_name",
        "webserver_address",
    ):
        if key not in conf.keys():
            logger.critical(f"Missing parameter {key} in the configuration file.")
            raise KeyError

    return conf


async def init_header(conf: Dict[str, Any]) -> HTTPBasicAuth:
    """
    Returns the authentication objet to set as the 'headers' kwarg in any request to the web app server.
    Raises an error if it fails the generation of the authentication object fails.

    :return:
    """
    try:
        logger.info("Creating api request header from config file parameters.")
        return HTTPBasicAuth(conf["user_name"], conf["password"])
    except KeyError as e:
        logger.critical(
            f"{e.args[0]} not found in config file. "
            "Unable to create header for api requests authentication."
        )
        raise RuntimeError


async def init_engine() -> sqlalchemy.Engine:
    database = project_root / "tempera" / "database" / "data.sqlite"

    if not Path(database).is_file():
        logger.critical(
            f"{database} is not a valid file path. No database engine can be created from it."
        )
        raise FileNotFoundError

    logger.info(f"Creating database engine from db file: {database}")
    return create_engine(f"sqlite:///{database}", echo=False)
