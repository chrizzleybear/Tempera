import datetime
import secrets
from typing import List, Annotated, Any, Literal

from fastapi import FastAPI, Depends, HTTPException
from fastapi.security import HTTPBasic, HTTPBasicCredentials
from pydantic import BaseModel
from starlette import status

FAKE_ACCESS_POINT_ID = "1111_2222_3333_4444"


class ConnectionInfo(BaseModel):
    established: bool
    station_id: str
    error: str | None


class ValidDevices(BaseModel):
    access_point_allowed: bool
    stations_allowed: List[str]


class ScanOrder(BaseModel):
    scan: bool


class GetRequestBody(BaseModel):
    access_point_id: str


class TemperaStation(BaseModel):
    access_point_id: str
    station_id: str


class Measurement(BaseModel):
    tempera_station_id: str
    timestamp: datetime.datetime
    temperature: float
    irradiance: float
    humidity: float
    nmvoc: float


class TimeRecord(BaseModel):
    tempera_station_id: str
    start: datetime.datetime
    duration: int
    mode: Literal["OUT_OF_OFFICE", "DEEP_WORK", "IN_MEETING", "AVAILABLE"]
    auto_update: bool


class Measurements(BaseModel):
    access_point_id: str
    measurements: List[Measurement]


class TimeRecords(BaseModel):
    access_point_id: str
    time_records: List[TimeRecord]


app = FastAPI()

security = HTTPBasic()


def check_credentials(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
):
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
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Basic"},
        )
    return credentials.username


def check_access_point_id(access_point_id: str):
    raise HTTPException(
        status_code=status.HTTP_403_UNAUTHORIZED,
        detail=f"Access point {access_point_id} not registered or not enabled.",
    )


@app.get("/rasp/api/valid_devices", response_model=ValidDevices)
async def get_active_station_ids(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    device_id: str,
    body: GetRequestBody,
) -> Any:
    """
    'access_point_allowed' is True if the ID of the access point is registered as allowed AND enabled in the back end.
    'stations_allowed' is a list of tempera station addresses that are registered AND enabled in the back end.

    :param body:
    :param credentials:
    :param device_id: ID of the access point
    :return:
    """
    if body.access_point_id != FAKE_ACCESS_POINT_ID:
        check_access_point_id(body.access_point_id)

    return {"access_point_allowed": True, "stations_allowed": ["1234567890", "add2"]}


@app.get("/rasp/api/scan_order", response_model=ScanOrder)
async def get_scan_order(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    device_id: str,
    body: GetRequestBody,
) -> Any:
    if body.access_point_id != FAKE_ACCESS_POINT_ID:
        check_access_point_id(body.access_point_id)

    return {"scan": False}


@app.post("/rasp/api/new_station")
async def post_tempera_station(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    station: TemperaStation,
):
    if station.access_point_id != FAKE_ACCESS_POINT_ID:
        check_access_point_id(station.access_point_id)
    return {"received station": True}


# TODO: define custom errors (enums) in device discovery and send them to web server for displaying
@app.post("/rasp/api/connection")
async def post_connection(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    body: GetRequestBody,
):
    if body.access_point_id != FAKE_ACCESS_POINT_ID:
        check_access_point_id(body.access_point_id)
    return {"data sent"}


@app.post("/rasp/api/measurements")
async def post_measurements(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    measurements: Measurements,
):
    if measurements.access_point_id != FAKE_ACCESS_POINT_ID:
        check_access_point_id(measurements.access_point_id)
    return measurements


@app.post("/rasp/api/time_records")
async def post_measurements(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    time_records: TimeRecords,
):
    if time_records.access_point_id != FAKE_ACCESS_POINT_ID:
        check_access_point_id(time_records.access_point_id)
    return time_records


@app.get("/")
async def root():
    return {"message": "App up and running!"}
