package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Alert;
import at.qe.skeleton.model.enums.AlertSeverity;
import at.qe.skeleton.rest.frontend.dtos.AlertDto;
import at.qe.skeleton.services.AlertService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



@Service
public class AlertMapper {

  private final AlertService alertService;

  public AlertMapper(AlertService alertService) {
    this.alertService = alertService;
  }

  public List<AlertDto> getAlerts(String username) {

    List<Alert> alerts = alertService.getRelevantAlertsDetailed(username);
    if (alerts.isEmpty()) {
      return new ArrayList<>();
    }
    for (Alert alert : alerts) {
      Alert lastAcknowledgedAlert =
          alertService.findLastAcknowledgedAlertBySensorAndThreshold(
              alert.getSensor(), alert.getThreshold());
      if (lastAcknowledgedAlert == null) {
        continue;
      }
      LocalDateTime acknowledgedAt = alert.getAcknowledgedAt();
      if (LocalDateTime.now().isBefore(acknowledgedAt.plusHours(1))) {
        alerts.remove(alert);
      }
    }
    return alerts.stream().map(this::mapAlertToAlertDto).toList();
  }

  public void deleteAlert(String id) throws CouldNotFindEntityException {
    Alert alert = alertService.loadAlertById(Long.parseLong(id));
    if (alert == null) {
      throw new CouldNotFindEntityException("Alert not found");
    }
    alert.setAcknowledged(true);
    alert.setAcknowledgedAt(LocalDateTime.now());
    alertService.saveAlert(alert);
  }

  private AlertDto mapAlertToAlertDto(Alert alert) {
    String message = messageBuilder(alert);
    AlertSeverity severity =
        switch (alert.getThreshold().getThresholdType()) {
          case LOWERBOUND_INFO, UPPERBOUND_INFO -> AlertSeverity.INFO;
          case LOWERBOUND_WARNING, UPPERBOUND_WARNING -> AlertSeverity.WARNING;
        };
    String start = alert.getFirstIncident().format(DateTimeFormatter.ISO_DATE_TIME);
    String end = alert.getLastIncident().format(DateTimeFormatter.ISO_DATE_TIME);
    return new AlertDto(
        alert.getId().toString(), message, start, end, severity, alert.getSensor().getSensorType());
  }

  private String messageBuilder(Alert alert) {
    String dataType =
        switch (alert.getSensor().getSensorType()) {
          case TEMPERATURE -> "Temperature";
          case HUMIDITY -> "Humidity";
          case IRRADIANCE -> "Irradiance";
          case NMVOC -> "AirQuality";
        };

    String[] severity = new String[3];
    switch (alert.getThreshold().getThresholdType()) {
      case LOWERBOUND_INFO:
        severity[0] = "Heads up!";
        severity[1] = "a little low";
        severity[2] = "down to";
        break;
      case LOWERBOUND_WARNING:
        severity[0] = "Oh no!";
        severity[1] = "too low";
        severity[2] = "down to";
        break;
      case UPPERBOUND_INFO:
        severity[0] = "Heads up!";
        severity[1] = "a little high";
        severity[2] = "up to";
        break;
      case UPPERBOUND_WARNING:
        severity[0] = "Oh no!";
        severity[1] = "too high";
        severity[2] = "up to";
        break;
    }
    return "%s The %s is %s. Values have reached %s %s %s."
        .formatted(
            severity[0],
            dataType,
            severity[1],
            severity[2],
            alert.getValue(),
            alert.getSensor().getUnit());
  }
}
