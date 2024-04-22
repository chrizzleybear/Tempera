import logging
from typing import Any, List, Dict

from fastapi import FastAPI
from pydantic import BaseModel

from utils.db_utils import SensorType

logger = logging.getLogger(f"tempera.{__name__}")


class ConnectionInfo(BaseModel):
    established: bool
    station_id: str
    error: str | None


class ValidDevices(BaseModel):
    access_point_allowed: bool
    stations_allowed: List[str]


class ScanOrder(BaseModel):
    scan: bool


class Measurement(BaseModel):
    type: SensorType
    measurements: List[Dict[str, Any]]


app = FastAPI()


@app.get("/rasp/api/valid_devices")
async def get_active_station_ids(device_id: str) -> ValidDevices:
    """
    'access_point_allowed' is True if the ID of the access point is registered as allowed AND enabled in the back end.
    'stations_allowed' is a list of tempera station addresses that are registered AND enabled in the back end.

    :param device_id: ID of the access point
    :return:
    """
    return {"access_point_allowed": True, "stations_allowed": ["add1", "add2"]}


@app.get("/rasp/api/scan_order")
async def get_scan_order() -> ScanOrder:
    return {"scan": False}


@app.post("/rasp/api/connection")
async def post_connection(connection_info: ConnectionInfo):
    return {"data sent"}


@app.post("/api/measurement/", status_code=201)
async def post_measurement(payload: Measurement):
    logger.info(f"Measurement: {payload}")
    return {"Received payload": payload}


@app.get("/")
async def root():
    return {"message": "App up and running!"}
