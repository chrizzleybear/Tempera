import logging
import sys
from typing import Literal, Dict

import requests

from tempera.utils import shared

logger = logging.getLogger(f"tempera.{__name__}")


async def make_request(kind: Literal["get", "post", "put"], url: str, **kwargs) -> Dict:
    """
    Wrapper around :func:`requests.get`/:func:`requests.post`/:func:`requests.put` to send the request and
    handle common response scenarios in a unified way. Ensures the request returns a non-empty response or a
    RuntimeError is raised.

    :kind: the type of the HTTP request (i.e., get, post, put).
    :url: the url to send the request to.
    :kwargs: additional kwargs to pass to the :func:`requests.get`/... function.
    :return: the response content as python dictionary.
    :raises RuntimeError: if there is no response.
    """
    match kind:
        case "get":
            response = requests.get(url, **kwargs)
        case "post":
            response = requests.post(url, **kwargs)
        case "put":
            response = requests.put(url, **kwargs)
        case _:
            logger.warning("REST operation not supported. Use one of 'get' or 'post'.")
            sys.exit(0)

    match response.status_code:
        case 200:
            logger.info(f"{response.status_code}: Successful request {response.json()}")
        case 201:
            logger.info(
                f"{response.status_code}: Successful request. Object created {response.json()}"
            )
        case 401:
            logger.critical(
                f"{response.status_code}: Authentication failed! "
                "Check that the authentication username and password "
                f"set in the config file are correct. {response.json()}"
            )
            sys.exit(0)
        case 403:
            logger.critical(
                f"{response.status_code}: Access point not approved or enabled! "
                f"Ensure that this access point ({shared.config['access_point_id']}) "
                f"is registered and enabled in the web server. {response.json()}"
            )
            sys.exit(0)
        case _:
            logger.critical(
                f"Got an unexpected response from the web server. {response.status_code}: {response}"
            )
            sys.exit(0)

    response = response.json()

    if not response:
        logger.error("No response received!")
        raise RuntimeError

    return response
