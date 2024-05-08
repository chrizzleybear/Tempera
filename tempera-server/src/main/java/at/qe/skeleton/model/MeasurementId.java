package at.qe.skeleton.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class MeasurementId implements Serializable {

  private SensorTemperaCompositeId sensorId;
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime timestamp;

  public SensorTemperaCompositeId getSensorId() {
    return sensorId;
  }

  public void setSensorId(SensorTemperaCompositeId sensorTemperaCompositeId) {
    this.sensorId = sensorTemperaCompositeId;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timeStamp) {
    this.timestamp = timeStamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MeasurementId other)) return false;
    return sensorId.equals(other.sensorId)
        && timestamp.equals(other.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sensorId, timestamp);
  }
}
