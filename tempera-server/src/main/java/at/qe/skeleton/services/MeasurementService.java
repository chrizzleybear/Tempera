package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementId;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorId;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Scope("application")
public class MeasurementService {

  private final MeasurementRepository measurementRepository;

  public MeasurementService(
      TemperaStationRepository temperaStationRepository,
      MeasurementRepository measurementRepository,
      SensorRepository sensorRepository) {
    this.measurementRepository = measurementRepository;
  }

  public Measurement loadMeasurementByIdComponents(String temperaId, Long sensorId, LocalDateTime timestamp) throws CouldNotFindEntityException {
    MeasurementId id = new MeasurementId();
    id.setSensorId(new SensorId(temperaId, sensorId));
    id.setTimestamp(timestamp);

    return measurementRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException("Invalid Measurement ID: " + id));
  }
  public Measurement loadMeasurement(MeasurementId id) throws CouldNotFindEntityException {
    return measurementRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException("Invalid Measurement ID: " + id));
  }


  public Measurement saveMeasurement(Measurement measurement) {
    LocalDateTime timestamp = measurement.getId().getTimestamp();
    measurement.getId().setTimestamp(timestamp);
    return measurementRepository.save(measurement);
  }

  // delete
  public void deleteMeasurement(Measurement measurement) {
    measurementRepository.delete(measurement);
  }


  public Optional<Measurement> findLatestMeasurementBySensor(Sensor sensor) {
    SensorId id = sensor.getSensorId();
    return measurementRepository.findFirstBySensorIdOrderById_TimestampDesc(id);
  }
  public List<Measurement> loadAllMeasurementsFromTempera() {
    return null;
  }
}
