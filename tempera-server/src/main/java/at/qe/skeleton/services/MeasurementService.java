package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.AirQualityCouldNotBeDeterminedException;
import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.ThresholdNotAvailableException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.ClimateQuality;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import at.qe.skeleton.model.enums.*;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.rest.frontend.dtos.FrontendMeasurementDto;
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
  private final AuditLogService auditLogService;

  private static final String INVALID_MEASUREMENT = "Invalid Measurement ID: ";

  public MeasurementService(
      AlertService alertService,
      ThresholdService thresholdService,
      MeasurementRepository measurementRepository,
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
        .orElseThrow(() -> new CouldNotFindEntityException(INVALID_MEASUREMENT + id));
  }

  public Measurement loadMeasurement(MeasurementId id) throws CouldNotFindEntityException {
      Measurement m = measurementRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException(INVALID_MEASUREMENT + id));
      auditLogService.logEvent(LogEvent.LOAD, LogAffectedType.MEASUREMENT,
              "Measurement from station " + id.getSensorId().getTemperaId() + " at " + id.getTimestamp() + " was loaded.");
      return m;
  }

  public Measurement saveMeasurement(Measurement measurement) {
    LocalDateTime timestamp = measurement.getId().getTimestamp();
    measurement.getId().setTimestamp(timestamp);
    return measurementRepository.save(measurement);
  }

  /**
   * Creates a FrontendMeasurementDto based on the value of the measurement and the thresholds of the
   * room the Temperastation where the User is located. The method determines the ClimateQuality of the
   * measurement based on the thresholds and the value of the measurement.
   * @param value If the value is null, the method returns null.
   * @param thresholds The thresholds of the room the Temperastation of the User is located.
   * @param sensorType The SensorType of the measurement.
   */
  public FrontendMeasurementDto createFrontendMeasurementDto(
      Double value, Set<Threshold> thresholds, SensorType sensorType) {
    if (value == null){
      return null;
    }
    Optional<Threshold> optionalThreshold = getViolatedThreshold(value, sensorType, thresholds);
    if (optionalThreshold.isEmpty()) {
      return new FrontendMeasurementDto(value, ClimateQuality.GOOD);
    }
    ThresholdType violationType = optionalThreshold.get().getThresholdType();
    if (violationType.equals(ThresholdType.LOWERBOUND_INFO) || violationType.equals(ThresholdType.UPPERBOUND_INFO)) {
      return new FrontendMeasurementDto(value, ClimateQuality.MEDIOCRE);
    }
    if (violationType.equals(ThresholdType.LOWERBOUND_WARNING) || violationType.equals(ThresholdType.UPPERBOUND_WARNING)) {
      return new FrontendMeasurementDto(value, ClimateQuality.POOR);
    }
    throw new AirQualityCouldNotBeDeterminedException("There has been an error while determining the air quality.");
  }

  // delete
  public void deleteMeasurement(Measurement measurement) {
    measurementRepository.delete(measurement);
  }

  /**
   * Reviews all measurements if they trigger an alert. If an alert is triggered the method looks
   * for existing and unacknowledged alerts for the sensor of the measurement that match the
   * threshold type that was violated and updates the alert with the new information. If no alert is
   * found, a new alert is created and saved.
   *
   * @param measurementIds List of 4 MeasurementIds, that came via the MeasurementController from
   *     the AccessPoint and all belong to the same TemperaStation
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
              .orElseThrow(() -> new CouldNotFindEntityException(INVALID_MEASUREMENT + id)));
    }
    for (var measurement : measurements) {
      // ein Measurement wird maximal einen Alert auslösen, da es entweder gar keinen Alert, einen
      // Info-Alert oder einen Warn-Alert auslösen kann
      Alert alert = checkAlertConditions(measurement, thresholds);
      if (alert != null) {
        var threshold = alert.getThreshold();
        var sensorType = threshold.getSensorType();
        var thresholdType = threshold.getThresholdType();

        if (alert.getThreshold().isOfLowerBoundType()) {
          auditLogService.logEvent(LogEvent.WARN, LogAffectedType.THRESHOLD,
                  "Value for %s of station %s is below threshold value %s. Threshold type: %s".formatted(sensorType, temperaId, threshold.getValue(), thresholdType));
        } else {
          auditLogService.logEvent(LogEvent.WARN, LogAffectedType.THRESHOLD,
                  "Value for %s of station %s is above threshold value %s. Threshold type: %s".formatted(sensorType, temperaId, threshold.getValue(), thresholdType));
        }

        alertService.saveAlert(alert);
      }
    }
  }

  private Alert checkAlertConditions(Measurement measurement, Set<Threshold> thresholds) {
    if (thresholds == null) {
      return null;
    }
    double value = measurement.getValue();
    SensorType sensorType = measurement.getSensor().getSensorType();
    Optional<Threshold> optionalThreshold = getViolatedThreshold(value, sensorType, thresholds);
      return optionalThreshold.map(threshold -> alertBuilder(threshold, measurement)).orElse(null);
  }

  /**
   * Helper Method, that returns an Optional that either contains a specific Threshold that was violated
   * by the measurement represented by value and sensorType or is empty if no threshold was violated.
   * NMVOC is handled little differently, since it can not be too high (it is measured in OHM and high values are good).
   * Therefore the upperbound thresholds are not considered for NMVOC.
   * @param value
   * @param sensorType
   * @param thresholds
   * @return
   */
  private Optional<Threshold> getViolatedThreshold(
      double value, SensorType sensorType, Set<Threshold> thresholds) {
    Threshold lowerWarnThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType().equals(sensorType)
                        && t.getThresholdType().equals(ThresholdType.LOWERBOUND_WARNING))
            .findFirst()
            .orElseThrow(() -> new ThresholdNotAvailableException(sensorType, ThresholdType.LOWERBOUND_WARNING));
    Threshold lowerInfoThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType().equals(sensorType)
                        && t.getThresholdType().equals(ThresholdType.LOWERBOUND_INFO))
            .findFirst()
            .orElseThrow(() -> new ThresholdNotAvailableException(sensorType, ThresholdType.LOWERBOUND_INFO));
    Threshold upperInfoThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType().equals(sensorType)
                        && t.getThresholdType().equals(ThresholdType.UPPERBOUND_INFO))
            .findFirst()
            .orElseThrow(() -> new ThresholdNotAvailableException(sensorType, ThresholdType.UPPERBOUND_INFO));
    Threshold upperWarnThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType().equals(sensorType)
                        && t.getThresholdType().equals(ThresholdType.UPPERBOUND_WARNING))
            .findFirst()
            .orElseThrow(() -> new ThresholdNotAvailableException(sensorType, ThresholdType.UPPERBOUND_WARNING));
    if (value <= lowerWarnThreshold.getValue()) {
        return Optional.of(lowerWarnThreshold);
    } else if (value <= lowerInfoThreshold.getValue() ) {
      return Optional.of(lowerInfoThreshold);
      // airquality does not have upper limit. it is measured via ohm and high values are good (look at wiki).
    } else if (value >= upperWarnThreshold.getValue() && sensorType!=SensorType.NMVOC) {
        return Optional.of(upperWarnThreshold);
    } else if (value >= upperInfoThreshold.getValue() &&sensorType!=SensorType.NMVOC) {
      return Optional.of(upperInfoThreshold);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Builds an alert object based on the threshold and measurement. If an alert is already open for
   * the sensor and threshold, the alert is updated with the new information. If no alert is open, a
   * new alert is created.
   *
   * @param threshold
   * @param measurement
   * @return
   */
  private Alert alertBuilder(Threshold threshold, Measurement measurement) {

    Alert openAlert =
        alertService.findOpenAlertBySensorAndThreshold(measurement.getSensor(), threshold);
    if (openAlert == null) {
      openAlert = new Alert(threshold, measurement.getSensor());
      openAlert.setFirstIncident(measurement.getId().getTimestamp());
      openAlert.setLastIncident(measurement.getId().getTimestamp());
      openAlert.setPeakDeviationValue(measurement.getValue());
      return openAlert;
    }
    openAlert.setLastIncident(measurement.getId().getTimestamp());
    if (threshold.isOfLowerBoundType()
        && measurement.getValue() < openAlert.getPeakDeviationValue()) {
      openAlert.setPeakDeviationValue(measurement.getValue());
    }
    if (!threshold.isOfLowerBoundType()
        && measurement.getValue() > openAlert.getPeakDeviationValue()) {
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

}
