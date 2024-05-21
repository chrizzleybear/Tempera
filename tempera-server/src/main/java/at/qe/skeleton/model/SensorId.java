package at.qe.skeleton.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;
import java.util.Objects;


/**
 * This class represents the composite key of the Sensor entity. It consists of the sensorId and the
 * temperaStationId - thus making it possible for sensors of different tempera stations to have the
 * same sensorId.
 */
@Embeddable
public class SensorId implements Serializable {

  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long sensorId;

  private String temperaId;

  public SensorId(String temperaId, Long sensorId) {
    this.sensorId = sensorId;
    this.temperaId = temperaId;
  }

  public SensorId() {}

  public Long getSensorId() {
    return sensorId;
  }

  public String getTemperaId() {
    return temperaId;
  }

  public void setTemperaId(String temperaStationId) {
    this.temperaId = temperaStationId;
  }

  public void setSensorId(Long sensorId) {
    this.sensorId = sensorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SensorId other)) return false;
    return sensorId.equals(other.sensorId) && temperaId.equals(other.temperaId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sensorId, temperaId);
  }
}
