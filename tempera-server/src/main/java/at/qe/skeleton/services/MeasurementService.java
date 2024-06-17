package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.*;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class MeasurementService {

  private final MeasurementRepository measurementRepository;
  private final ThresholdService thresholdService;
  private final AlertService alertService;
  private static final String INVALID_MEASUREMENT_ID = "Invalid Measurement ID: ";
  private final AuditLogService auditLogService;

  public MeasurementService(
      AlertService alertService,
      ThresholdService thresholdService,
      TemperaStationRepository temperaStationRepository,
      MeasurementRepository measurementRepository,
      SensorRepository sensorRepository,
      AuditLogService auditLogService) {
    this.alertService = alertService;
    this.thresholdService = thresholdService;
    this.measurementRepository = measurementRepository;
    this.auditLogService = auditLogService;
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
      Measurement m = measurementRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException(INVALID_MEASUREMENT_ID + id));
      auditLogService.logEvent(LogEvent.LOAD, LogAffectedType.MEASUREMENT,
              "Measurement from station " + id.getSensorId().getTemperaId() + " at " + id.getTimestamp() + " was loaded.");
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

  /**
   * Reviews all measurements if they trigger an alert. If an alert is triggered the method looks for
   * existing and unacknowledged alerts for the sensor of the measurement that match the threshold type
   * that was violated and updates the alert with the new information. If no alert is found, a new alert
   * is created and saved.
   * @param measurementIds List of 4 MeasurementIds, that came via the MeasurementController from the AccessPoint and
   *                       all belong to the same TemperaStation
   * @param temperaId
   * @throws CouldNotFindEntityException
   */
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

    if (value <= lowerInfoThreshold.getValue()) {
      auditLogService.logEvent(LogEvent.WARN, LogAffectedType.THRESHOLD,
              "Value for " + measurement.getSensor().getSensorType() + " of station " + measurement.getSensor().getTemperaStation().getId() + " is below " + ((value <= lowerWarnThreshold.getValue()) ? "WARNING-" : "INFO-") + "Threshold."
      );
      return alertBuilder( (value <= lowerWarnThreshold.getValue()) ? lowerWarnThreshold : lowerInfoThreshold  , measurement);
    } else if (value >= upperInfoThreshold.getValue()) {
      auditLogService.logEvent(LogEvent.WARN, LogAffectedType.THRESHOLD,
              "Value for " + measurement.getSensor().getSensorType() + " of station " + measurement.getSensor().getTemperaStation().getId() + " is above " + ((value <= lowerWarnThreshold.getValue()) ? "WARNING-" : "INFO-") + "Threshold."
      );
      return alertBuilder( (value >= upperWarnThreshold.getValue()) ? upperWarnThreshold : upperInfoThreshold  , measurement);
    }
    return null;
  }

  /**
   * Builds an alert object based on the threshold and measurement. If an alert is already open for the
   * sensor and threshold, the alert is updated with the new information. If no alert is open, a new alert
   * is created.
   * @param threshold
   * @param measurement
   * @return
   */
  private Alert alertBuilder(Threshold threshold, Measurement measurement) {

    Alert openAlert = alertService.findOpenAlertBySensorAndThreshold(measurement.getSensor(), threshold);
    if (openAlert == null) {
      openAlert = new Alert(threshold, measurement.getSensor());
      openAlert.setFirstIncident(measurement.getId().getTimestamp());
      openAlert.setLastIncident(measurement.getId().getTimestamp());
      openAlert.setPeakDeviationValue(measurement.getValue());

      return openAlert;
    }
    openAlert.setLastIncident(measurement.getId().getTimestamp());

    if (threshold.isOfLowerBoundType() && measurement.getValue() < openAlert.getPeakDeviationValue()) {
      openAlert.setPeakDeviationValue(measurement.getValue());
    }
    if (!threshold.isOfLowerBoundType() && measurement.getValue() > openAlert.getPeakDeviationValue()) {
      openAlert.setPeakDeviationValue(measurement.getValue());
    }
    return openAlert;
  }

  public Optional<Measurement> findLatestMeasurementBySensor(Sensor sensor) {
    SensorId id = sensor.getSensorId();
    auditLogService.logEvent(LogEvent.LOAD, LogAffectedType.MEASUREMENT,
              "Lastest measurement of station " + sensor.getTemperaStation().getId() + " was loaded.");
    return measurementRepository.findFirstBySensorIdOrderById_TimestampDesc(id);
  }

  public Optional<List<Measurement>> find100LatestMeasurementsBySensor(Sensor sensor) {
    SensorId id = sensor.getSensorId();
    return measurementRepository.findTop100BySensorIdOrderById_TimestampAsc(id);
  }


  public List<Measurement> loadAllMeasurementsFromTempera() {
    return measurementRepository.findAll();
  }
}
