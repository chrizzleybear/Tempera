package at.qe.skeleton.model;

import at.qe.skeleton.services.TimeRecordService;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class MeasurementId implements Serializable {

  private SensorTemperaCompositeId sensorTemperaCompositeId;
  private LocalDateTime timeStamp;

  public SensorTemperaCompositeId getSensorTemperaCompositeId() {
    return sensorTemperaCompositeId;
  }

  public void setSensorTemperaCompositeId(SensorTemperaCompositeId sensorTemperaCompositeId) {
    this.sensorTemperaCompositeId = sensorTemperaCompositeId;
  }

  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(LocalDateTime timeStamp) {
    this.timeStamp = timeStamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MeasurementId other)) return false;
    return sensorTemperaCompositeId.equals(other.sensorTemperaCompositeId)
        && timeStamp.equals(other.timeStamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sensorTemperaCompositeId, timeStamp);
  }
}
