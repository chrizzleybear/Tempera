package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.AlertType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private Threshold threshold;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime timeStamp;

  private AlertType alertType;

  @Column(name = "alert_value")
  private double value;

  private String message;

  private boolean acknowledged;

  /**
   * Constructor for Alerts with AlertType Threshold_Warning
   *
   * @param alertType
   * @param threshold that was violated
   * @param value the actual value of the measurement, that violated the threshold.
   */
  public Alert(AlertType alertType, Threshold threshold, double value) {

    this.threshold = threshold;
    this.alertType = alertType;
    this.value = value;
  }

  /**
   * Constructor for Alerts with AlertType transmission_error or Data_anomalies.
   *
   * @param alertType
   * @param message the message, that is supposed to be shown to user about the nature of the error
   *     or anomaly.
   */
  public Alert(AlertType alertType, String message) {
    this.alertType = alertType;
    this.message = message;
  }

  /** Non-Arg protected Constructor for JPA only. */
  protected Alert() {}
  ;

  public boolean isAcknowledged() {
    return acknowledged;
  }

  public void setAcknowledged(boolean acknowledged) {
    this.acknowledged = acknowledged;
  }

  public long getId() {
    return id;
  }

  public Threshold getThreshold() {
    return threshold;
  }

  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  public AlertType getAlertType() {
    return alertType;
  }

  public double getValue() {
    return value;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Alert alert = (Alert) o;
    return Objects.equals(timeStamp, alert.getTimeStamp());
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeStamp);
  }
}
