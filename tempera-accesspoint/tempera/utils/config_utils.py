import logging
from pathlib import Path
from typing import Dict, Any

import yaml
from requests.auth import HTTPBasicAuth

logger = logging.getLogger(f"tempera.{__name__}")


def init_config() -> Dict | None:
    config_file = Path(__name__).resolve().parent.parent / "conf.yaml"
    if not config_file.is_file():
        logger.warning(
            f"{config_file.absolute()} not found. Couldn't load configuration. "
        )
        return

    logger.debug(f"Reading config file ({config_file.absolute()})")
    with open(config_file, "r") as config_file:
        config = yaml.safe_load(config_file)

    return config


def init_header(config: Dict[str, Any]) -> HTTPBasicAuth:
    """
    Returns the authentication objet to set as the 'headers' kwarg in any request to the web app server.
    Raises an error if it fails the generation of the authentication object fails.

    :param config:
    :return:
    """
    try:
        return HTTPBasicAuth(config["user_name"], config["password"])
    except KeyError as e:
        logger.critical(
            "User name or password not found in config file. Unable to authenticate api requests."
        )
        logger.exception(e)
        raise RuntimeError
