import secrets
from typing import List, Annotated

from fastapi import FastAPI, Depends, HTTPException
from fastapi.security import HTTPBasic, HTTPBasicCredentials
from pydantic import BaseModel
from starlette import status


class ConnectionInfo(BaseModel):
    established: bool
    station_id: str
    error: str | None


class ValidDevices(BaseModel):
    access_point_allowed: bool
    stations_allowed: List[str]


class ScanOrder(BaseModel):
    scan: bool


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


@app.get("/rasp/api/valid_devices")
async def get_active_station_ids(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)], device_id: str
) -> ValidDevices:
    """
    'access_point_allowed' is True if the ID of the access point is registered as allowed AND enabled in the back end.
    'stations_allowed' is a list of tempera station addresses that are registered AND enabled in the back end.

    :param credentials:
    :param device_id: ID of the access point
    :return:
    """
    return {"access_point_allowed": True, "stations_allowed": ["1234567890", "add2"]}


@app.get("/rasp/api/scan_order")
async def get_scan_order(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)], device_id: str
) -> ScanOrder:
    return {"scan": False}


# TODO: define custom errors (enums) in device discovery and send them to web server for displaying
@app.post("/rasp/api/connection")
async def post_connection(connection_info: ConnectionInfo):
    return {"data sent"}


@app.get("/")
async def root():
    return {"message": "App up and running!"}
