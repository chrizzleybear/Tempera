import datetime
import http
import secrets
from typing import List, Annotated, Any, Literal

from fastapi import FastAPI, Depends, HTTPException
from fastapi.security import HTTPBasic, HTTPBasicCredentials
from pydantic import BaseModel

FAKE_ACCESS_POINT_ID = "123e4567-e89b-12d3-a456-426614174001"


class ValidDevices(BaseModel):
    access_point_allowed: bool
    stations_allowed: List[str]


class ScanOrder(BaseModel):
    scan: bool


class TemperaStation(BaseModel):
    access_point_id: str
    station_id: str


class Measurement(BaseModel):
    access_point_id: str
    tempera_station_id: str
    timestamp: datetime.datetime
    temperature: float
    irradiance: float
    humidity: float
    nmvoc: float


class TimeRecord(BaseModel):
    access_point_id: str
    tempera_station_id: str
    start: datetime.datetime
    duration: int
    mode: Literal["OUT_OF_OFFICE", "DEEP_WORK", "IN_MEETING", "AVAILABLE"]
    auto_update: bool


app = FastAPI()

security = HTTPBasic()


def check_credentials(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
):
    """
    Basic authentication checker copied from the `FastAPI docs`_.

    .. _FastAPI docs: https://fastapi.tiangolo.com/advanced/security/http-basic-auth/

    :param credentials:
    :return:
    """
    current_username_bytes = credentials.username.encode("utf8")
    correct_username_bytes = b"admin"
    is_correct_username = secrets.compare_digest(
        current_username_bytes, correct_username_bytes
    )
    current_password_bytes = credentials.password.encode("utf8")
    correct_password_bytes = b"passwd"
    is_correct_password = secrets.compare_digest(
        current_password_bytes, correct_password_bytes
    )
    if not (is_correct_username and is_correct_password):
        raise HTTPException(
            status_code=http.HTTPStatus.UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Basic"},
        )
    return credentials.username


def check_access_point_id(access_point_id: str):
    """

    :param access_point_id:
    :return:
    """
    if access_point_id != FAKE_ACCESS_POINT_ID:
        raise HTTPException(
            status_code=http.HTTPStatus.FORBIDDEN,
            detail=f"Access point {access_point_id} not registered or not enabled.",
        )


@app.get("/rasp/api/valid_devices", response_model=ValidDevices)
async def get_active_station_ids(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    access_point_id: str,
) -> Any:
    """
    'access_point_allowed' is True if the ID of the access point is registered as allowed AND enabled in the back end.
    'stations_allowed' is a list of tempera station addresses that are registered AND enabled in the back end.

    :param access_point_id:
    :param credentials:
    :return:
    """
    check_access_point_id(access_point_id)

    return {
        "access_point_allowed": True,
        "stations_allowed": ["tempera_station_1", "add2"],
    }


@app.get("/rasp/api/scan_order", response_model=ScanOrder)
async def get_scan_order(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    access_point_id: str,
) -> Any:
    """

    :param credentials:
    :param access_point_id:
    :return:
    """
    check_access_point_id(access_point_id)

    return {"scan": False}


# TODO: define custom errors (enums) in device discovery and send them to web server for displaying


@app.post("/rasp/api/measurement", status_code=201)
async def post_measurements(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    measurement: Measurement,
):
    check_access_point_id(measurement.access_point_id)
    return measurement


@app.post("/rasp/api/time_record", status_code=201)
async def post_measurements(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    time_record: TimeRecord,
):
    """

    :param credentials:
    :param time_record:
    :return:
    """
    check_access_point_id(time_record.access_point_id)
    return time_record


@app.get("/")
async def root():
    """

    :return:
    """
    return {"message": "App up and running!"}
