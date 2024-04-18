package at.qe.skeleton.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;
import java.util.Objects;

// todo finish the empedded id key

/**
 * This class represents the composite key of the Sensor entity. It consists of the sensorId and the
 * temperaStationId - thus making it possible for sensors of different tempera stations to have the
 * same sensorId.
 */
@Embeddable
public class SensorTemperaCompositeId implements Serializable {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sensorId;

  private String temperaStationId;

  public SensorTemperaCompositeId() {}

  public Long getSensorId() {
    return sensorId;
  }

  public void setSensorId(Long id) {
    this.sensorId = id;
  }

  public String getTemperaStationId() {
    return temperaStationId;
  }

  public void setTemperaStationId(String temperaStationId) {
    this.temperaStationId = temperaStationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SensorTemperaCompositeId other)) return false;
    return sensorId.equals(other.sensorId) && temperaStationId.equals(other.temperaStationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sensorId, temperaStationId);
  }
}
