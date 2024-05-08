package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Threshold implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private SensorType sensorType;
  private ThresholdType thresholdType;

  @Column(name = "threshold_value")
  private double value;

  @OneToOne private Modification modification;

  @OneToOne private ThresholdTip tip;

  public SensorType getSensorType() {
    return sensorType;
  }

  public void setSensorType(SensorType sensorType) {
    this.sensorType = sensorType;
  }

  public ThresholdType getThresholdType() {
    return thresholdType;
  }

  public void setThresholdType(ThresholdType thresholdType) {
    this.thresholdType = thresholdType;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public Modification getModificationReason() {
    return modification;
  }

  public void setModificationReason(Modification modification) {
    this.modification = modification;
  }

  public ThresholdTip getTip() {
    return tip;
  }

  public void setTip(ThresholdTip tip) {
    this.tip = tip;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Threshold threshold = (Threshold) o;
    return id == threshold.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return String.valueOf(id);
  }
}
