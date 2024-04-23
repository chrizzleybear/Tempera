import logging
from typing import Literal, Dict

import requests


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
            logging.warning("REST operation not supported. Use one of 'get' or 'post'.")
            raise RuntimeError

    match response.status_code:
        case 200:
            logging.info(
                f"{response.status_code}: Successful request {response.json()}"
            )
        case 201:
            logging.info(
                f"{response.status_code}: Successful request. Object created {response.json()}"
            )
        case 401:
            logging.error(
                f"{response.status_code}: Authentication failed! {response.json()}"
            )
            raise RuntimeError
        case _:
            logging.error(f"{response.status_code}: {response.json()}")
            raise RuntimeError

    response = response.json()

    if not response:
        logging.error("No response received!")
        raise RuntimeError

    return response
