package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.AlertType;
import at.qe.skeleton.model.enums.ThresholdType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private Sensor sensor;

  @ManyToOne private Threshold threshold;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime firstIncident;

  private boolean acknowledged;

  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime acknowledgedAt;

  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime lastIncident;

  @Column(name = "peak_deviation_value")
  private double peakDeviationValue;

  private String message;

  /**
   * Constructor for Alerts with AlertType Threshold_Warning
   *
   * @param threshold that was violated
   * @param sensor that caused the alert
   */
  public Alert(Threshold threshold, Sensor sensor) {
    this.threshold = threshold;
    this.acknowledged = false;
    this.sensor = sensor;
  }

  /**
   * Constructor for Alerts with AlertType transmission_error or Data_anomalies.
   *
   * @param message the message, that is supposed to be shown to user about the nature of the error
   *     or anomaly.
   */
  public Alert(String message) {
    this.message = message;
    acknowledged = false;
  }

  /** Non-Arg protected Constructor for JPA only. */
  protected Alert() {
    acknowledged = false;
  }

  public boolean isAcknowledged() {
    return acknowledged;
  }

  public LocalDateTime getAcknowledgedAt() {
    return acknowledgedAt;
  }

  public void setAcknowledgedAt(LocalDateTime acknowledgedAt) {
    this.acknowledgedAt = acknowledgedAt;
  }

  public void acknowledge(boolean acknowledged) {
    this.acknowledged = acknowledged;
    if (acknowledged) {
      acknowledgedAt = LocalDateTime.now();
    }
  }

  public Long getId() {
    return id;
  }

  public Threshold getThreshold() {
    return threshold;
  }

  public LocalDateTime getFirstIncident() {
    return firstIncident;
  }

  public double getValue() {
    return peakDeviationValue;
  }

  public String getMessage() {
    return message;
  }

  public Sensor getSensor() {
    return sensor;
  }

  public void setSensor(Sensor sensor) {
    this.sensor = sensor;
  }

  public void setThreshold(Threshold threshold) {
    this.threshold = threshold;
  }

  public void setFirstIncident(LocalDateTime firstIncident) {
    this.firstIncident = firstIncident;
  }

  public void setAcknowledged(boolean acknowledged) {
    this.acknowledged = acknowledged;
  }

  public LocalDateTime getLastIncident() {
    return lastIncident;
  }

  public void setLastIncident(LocalDateTime lastIncident) {
    this.lastIncident = lastIncident;
  }

  public double getPeakDeviationValue() {
    return peakDeviationValue;
  }

  public void setPeakDeviationValue(double peakDeviationValue) {
    this.peakDeviationValue = peakDeviationValue;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Alert alert = (Alert) o;
    return Objects.equals(sensor, alert.getSensor()) && Objects.equals(threshold, alert.getThreshold()) &&
            Objects.equals(firstIncident, alert.getFirstIncident()) && Objects.equals(lastIncident, alert.getLastIncident());
  }

  @Override
  public int hashCode() {
    return Objects.hash(sensor, threshold, firstIncident, lastIncident);
  }
}
