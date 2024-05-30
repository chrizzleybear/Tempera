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

  @Column(name = "default_threshold")
  private boolean defaultThreshold;

  @Column(name = "sensor_type")
  private SensorType sensorType;

  @Column(name = "threshold_type")
  private ThresholdType thresholdType;

  @Column(name = "threshold_value")
  private double value;

  @ManyToOne
  @JoinColumn(name = "modification_id")
  private Modification modification;

  @ManyToOne
  @JoinColumn(name = "tip_id")
  private ThresholdTip tip;

  public Threshold(SensorType sensorType, ThresholdType thresholdType, double value, Modification modification, ThresholdTip tip) {
    this.defaultThreshold = false;
    this.sensorType = sensorType;
    this.thresholdType = thresholdType;
    this.value = value;
    this.modification = modification;
    this.tip = tip;
  }

  public Threshold() {
  }

  public Long getId() {
    return id;
  }
  public SensorType getSensorType() {
    return sensorType;
  }

  public void setSensorType(SensorType sensorType) {
    this.sensorType = sensorType;
  }

  public ThresholdType getThresholdType() {
    return thresholdType;
  }

  public boolean isDefaultThreshold() { return defaultThreshold; }

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
    return id.equals(threshold.getId());
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
