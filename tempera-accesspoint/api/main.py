from typing import List

from fastapi import FastAPI
from pydantic import BaseModel


class TemperatureData(BaseModel):
    station_name: str
    description: str | None
    temperature_data: List[float]


app = FastAPI()


@app.post("/{station_id}/temperature")
async def put_temperature_data(station_id: int, t_data: TemperatureData):
    return {"station": f"{station_id}",
            "message": f"Delivered the station data: {t_data}"}


@app.get("/")
async def root():
    return {"message": "Hello World"}
