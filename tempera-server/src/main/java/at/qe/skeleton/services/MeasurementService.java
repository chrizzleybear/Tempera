package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementId;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorId;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
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

  private final AuditLogService auditLogService;

  public MeasurementService(
      TemperaStationRepository temperaStationRepository,
      MeasurementRepository measurementRepository,
      SensorRepository sensorRepository,
      AuditLogService auditLogService) {
    this.measurementRepository = measurementRepository;
    this.auditLogService = auditLogService;
  }

  public Measurement loadMeasurementByIdComponents(String temperaId, Long sensorId, LocalDateTime timestamp) throws CouldNotFindEntityException {
    MeasurementId id = new MeasurementId();
    id.setSensorId(new SensorId(temperaId, sensorId));
    id.setTimestamp(timestamp);

    Measurement measurement = measurementRepository
            .findById(id)
            .orElseThrow(() -> new CouldNotFindEntityException("Invalid Measurement ID: " + id));
    auditLogService.logEvent(LogEvent.LOAD, LogAffectedType.MEASUREMENT,
            "Measurement of station " + temperaId + " and sensor " + sensorId + "was loaded.");
    return measurement;
  }

  public Measurement loadMeasurement(MeasurementId id) throws CouldNotFindEntityException {
    Measurement m =  measurementRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException("Invalid Measurement ID: " + id));
    auditLogService.logEvent(LogEvent.LOAD, LogAffectedType.MEASUREMENT,
            "Measurement " + id + "was loaded.");
    return m;
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
    auditLogService.logEvent(LogEvent.LOAD, LogAffectedType.MEASUREMENT,
            "Lastest measurement of sensor " + sensor.getId() + "was loaded.");
    return measurementRepository.findFirstBySensorIdOrderById_TimestampDesc(id);
  }
  public List<Measurement> loadAllMeasurementsFromTempera() {
    return null;
  }
}
