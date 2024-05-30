package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Alert;
import at.qe.skeleton.rest.frontend.dtos.AlertDto;
import at.qe.skeleton.services.AlertService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            return null;
        }
        for (Alert alert : alerts) {
            LocalDateTime lastIncident = alert.getLastIncident();
            Alert lastAcknowledgedAlert = alertService.findLastAcknowledgedAlertBySensorAndThreshold(alert.getSensor(), alert.getThreshold());
            if (lastAcknowledgedAlert != null) {
               continue;
            }
            LocalDateTime acknowledgedAt = alert.getAcknowledgedAt();
            if (LocalDateTime.now().isBefore(acknowledgedAt.plusHours(1))) {
                alerts.remove(alert);
            }
        }

        List<AlertDto> alertDtos = alerts.stream().map(this::mapAlertToAlertDto);



        return null;
    }


    public void deleteAlert(String id, String username) {
        // TODO implement


        // todo: set Alert to acknowledged && set acknowledgedAt to now
    }
}
