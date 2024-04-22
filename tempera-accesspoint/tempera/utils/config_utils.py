import logging
from pathlib import Path
from typing import Dict

import yaml

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
