package at.qe.skeleton.services;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorId;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.repositories.SensorRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Scope("application")
public class SensorService {

  private final SensorRepository sensorRepository;

  public SensorService(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  public Sensor findSensorById(SensorId id) {
    return sensorRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid Sensor ID: " + id));
  }

  public List<Sensor> findAllSensorsByTemperaStationId(String temperaStationId) {
    return sensorRepository.findAllByTemperaStationId(temperaStationId);
  }

  public Sensor saveSensor(Sensor sensor) {
    return sensorRepository.save(sensor);
  }

  /**
   * deletes the Sensor. Should not be called by itself, but only when deleting a whole TemperaStation.
   * @param sensor
   */
  public void deleteSensor(Sensor sensor) {
    sensorRepository.delete(sensor);
  }
}
