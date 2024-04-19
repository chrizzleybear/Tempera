package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.Unit;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Sensor {

  @EmbeddedId private SensorTemperaCompositeId sensorTemperaCompositeId;

  @ManyToOne(optional = false)
  @MapsId("temperaStationId")
  @JoinColumn(name = "tempera_station_id")
  private TemperaStation temperaStation;

  @Enumerated(EnumType.STRING) // necessary to store the enum as a string in the database
  private SensorType sensorType;

  @Enumerated(EnumType.STRING) // necessary to store the enum as a string in the database
  private Unit unit;

  public Sensor(SensorType sensorType, Unit unit) {
    this.sensorType = sensorType;
    this.unit = unit;
  }

  protected Sensor() {}

  public TemperaStation getTemperaStation() {
    return temperaStation;
  }

  public SensorTemperaCompositeId getId() {
    return sensorTemperaCompositeId;
  }

  public SensorType getSensorType() {
    return sensorType;
  }

  public void setSensorType(SensorType sensorType) {
    this.sensorType = sensorType;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

  @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Sensor other)) return false;
    return sensorTemperaCompositeId.equals(other.sensorTemperaCompositeId);
  }

@Override
public int hashCode() {
    return Objects.hash(sensorTemperaCompositeId);
}
}
