import logging
from typing import Literal, Dict

import requests

from tempera.utils import shared

logger = logging.getLogger(f"tempera.{__name__}")


async def make_request(kind: Literal["get", "post"], url: str, **kwargs) -> Dict:
    """
    Wrapper around a request to handle common response scenarios.
    Ensures the request returns a non-empty response or a RuntimeError is raised.

    :return:
    """
    match kind:
        case "get":
            response = requests.get(url, **kwargs)
        case "post":
            response = requests.post(url, **kwargs)
        case _:
            logger.warning("REST operation not supported. Use one of 'get' or 'post'.")
            raise RuntimeError

    match response.status_code:
        case 200:
            logger.info(f"{response.status_code}: Successful request {response.json()}")
        case 201:
            logger.info(
                f"{response.status_code}: Successful request. Object created {response.json()}"
            )
        case 401:
            logger.error(
                f"{response.status_code}: Authentication failed! "
                "Check that the authentication username and password "
                f"set in the config file are correct. {response.json()}"
            )
            raise RuntimeError
        case 403:
            logger.error(
                f"{response.status_code}: Access point not approved or enabled! "
                f"Ensure that this access point ({shared.config['access_point_id']}) "
                f"is registered and enabled in the web server. {response.json()}"
            )
        case _:
            logger.error(f"{response.status_code}: {response}")
            raise RuntimeError

    response = response.json()

    if not response:
        logger.error("No response received!")
        raise RuntimeError

    return response
