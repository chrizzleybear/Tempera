import datetime
from enum import StrEnum
from typing import List

from sqlalchemy import ForeignKey
from sqlalchemy.orm import DeclarativeBase, mapped_column, Mapped, relationship


class Mode(StrEnum):
    OUT_OF_OFFICE = "OUT_OF_OFFICE"
    DEEP_WORK = "DEEPWORK"
    IN_MEETING = "MEETING"
    AVAILABLE = "AVAILABLE"


class Base(DeclarativeBase):
    """
    Base class for defining entities with SQLAlchemy. All entities must inherit from this base class.
    This class shouldn't contain any implementation.
    """

    pass


class TemperaStation(Base):
    """"""

    __tablename__ = "tempera_station"

    id: Mapped[str] = mapped_column(primary_key=True)
    time_record: Mapped[List["TimeRecord"]] = relationship(
        back_populates="tempera_station"
    )
    measurement: Mapped[List["Measurement"]] = relationship(
        back_populates="tempera_station"
    )

    def __repr__(self):
        return f"TemperaStation[id={self.id}]"


class TimeRecord(Base):
    """"""

    __tablename__ = "time_record"

    tempera_station_id: Mapped[int] = mapped_column(
        ForeignKey("tempera_station.id"), primary_key=True
    )
    tempera_station: Mapped["TemperaStation"] = relationship(
        back_populates="time_record"
    )
    start: Mapped[datetime.datetime] = mapped_column(primary_key=True)
    duration: Mapped[int]
    mode: Mapped[Mode]
    auto_update: Mapped[bool]

    def __repr__(self):
        return (
            f"TimeRecord["
            f"tempera_station={self.tempera_station}, "
            f"start={self.start}, "
            f"duration={self.duration}, "
            f"mode={self.mode}, "
            f"auto_update={self.auto_update}]"
        )


class Measurement(Base):
    """"""

    __tablename__ = "measurement"

    tempera_station_id: Mapped[int] = mapped_column(
        ForeignKey("tempera_station.id"), primary_key=True
    )
    tempera_station: Mapped["TemperaStation"] = relationship(
        back_populates="measurement"
    )
    timestamp: Mapped[datetime.datetime] = mapped_column(primary_key=True)
    temperature: Mapped[float]
    irradiance: Mapped[float]
    humidity: Mapped[float]
    nmvoc: Mapped[float]

    def __repr__(self):
        return (
            f"Measurement["
            f"tempera_station={self.tempera_station}, "
            f"timestamp={self.timestamp}, "
            f"temperature={self.temperature}, "
            f"irradiance={self.irradiance}, "
            f"humidity={self.humidity}, "
            f"nmvoc={self.nmvoc}]"
        )
