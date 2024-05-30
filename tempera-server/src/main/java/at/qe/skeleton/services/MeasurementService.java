package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.AlertType;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Scope("application")
public class MeasurementService {

  private final MeasurementRepository measurementRepository;
  private final ThresholdService thresholdService;
  private final AlertService alertService;

  public MeasurementService(
      AlertService alertService,
      ThresholdService thresholdService,
      TemperaStationRepository temperaStationRepository,
      MeasurementRepository measurementRepository,
      SensorRepository sensorRepository) {
    this.alertService = alertService;
    this.thresholdService = thresholdService;
    this.measurementRepository = measurementRepository;
  }

  public Measurement loadMeasurementByIdComponents(
      String temperaId, Long sensorId, LocalDateTime timestamp) throws CouldNotFindEntityException {
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

  public void reviewForAlerts(List<MeasurementId> measurementIds, String temperaId)
      throws CouldNotFindEntityException {
    Set<Threshold> thresholds = thresholdService.getThresholdsByTemperaId(temperaId);
    List<Measurement> measurements = new ArrayList<>();
    for (var id : measurementIds) {
      measurements.add(
          (measurementRepository.findByIdDetailed(id))
              .orElseThrow(() -> new CouldNotFindEntityException("Invalid Measurement ID: " + id)));
    }
    for (var measurement : measurements) {
      // ein Measurement kann maximal einen Alert auslösen, da es entweder gar keinen Alert, einen
      // Info-Alert oder einen Warn-Alert auslösen kann
      Alert alert = checkAlertConditions(measurement, thresholds);
      if (alert != null) {
        alertService.saveAlert(alert);
      }
    }
  }

  private Alert checkAlertConditions(Measurement measurement, Set<Threshold> thresholds) {
    if (thresholds == null) {
      return null;
    }
    Threshold lowerInfoThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType().equals(measurement.getSensor().getSensorType())
                        && t.getThresholdType().equals(ThresholdType.LOWERBOUND_INFO))
            .findFirst()
            .orElse(null);
    Threshold lowerWarnThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType().equals(measurement.getSensor().getSensorType())
                        && t.getThresholdType().equals(ThresholdType.LOWERBOUND_WARNING))
            .findFirst()
            .orElse(null);
    Threshold upperInfoThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType().equals(measurement.getSensor().getSensorType())
                        && t.getThresholdType().equals(ThresholdType.UPPERBOUND_INFO))
            .findFirst()
            .orElse(null);
    Threshold upperWarnThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType().equals(measurement.getSensor().getSensorType())
                        && t.getThresholdType().equals(ThresholdType.UPPERBOUND_WARNING))
            .findFirst()
            .orElse(null);

    double value = measurement.getValue();

    if (value <= lowerWarnThreshold.getValue()) {
      return alertBuilder(lowerWarnThreshold, measurement);
    } else if (value <= lowerInfoThreshold.getValue()) {
      return alertBuilder(lowerInfoThreshold, measurement);
    } else if (value >= upperWarnThreshold.getValue()) {
      return alertBuilder(upperWarnThreshold, measurement);
    } else if (value >= upperInfoThreshold.getValue()) {
      return alertBuilder(upperInfoThreshold, measurement);
    } else {
      return null;
    }
  }

  private Alert alertBuilder(Threshold threshold, Measurement measurement) {
    return new Alert(AlertType.THRESHOLD_WARNING, threshold, measurement.getValue(), measurement.getId().getTimestamp());
  }

  public Optional<Measurement> findLatestMeasurementBySensor(Sensor sensor) {
    SensorId id = sensor.getSensorId();
    return measurementRepository.findFirstBySensorIdOrderById_TimestampDesc(id);
  }

  public List<Measurement> loadAllMeasurementsFromTempera() {
    return null;
  }
}
