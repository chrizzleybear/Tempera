package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.Unit;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Sensor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "temperaStation_id")
  private TemperaStation temperaStation;

  @Enumerated(EnumType.STRING) //necessary to store the enum as a string in the database
  private SensorType sensorType;

  @Enumerated(EnumType.STRING)  //necessary to store the enum as a string in the database
  private Unit unit;

  public Sensor(SensorType sensorType, Unit unit) {
    this.sensorType = sensorType;
    this.unit = unit;
  }

  protected Sensor() {}

  public TemperaStation getTemperaStation() {
    return temperaStation;
  }

  public long getId() {
    return id;
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
}
