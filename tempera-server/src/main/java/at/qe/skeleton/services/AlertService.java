package at.qe.skeleton.services;

import at.qe.skeleton.model.Alert;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.repositories.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public void deletAlert(Alert alert) {
        alertRepository.delete(alert);
    }

    public void saveAlert(Alert alert) {
        alertRepository.save(alert);
    }

    public Alert findOpenAlertBySensorAndThreshold(Sensor sensor, Threshold threshold) {
        return alertRepository.findOpenAlertBySensorAndThreshold(sensor, threshold).orElse(null);
    }

    public Alert findLastAcknowledgedAlertBySensorAndThreshold(Sensor sensor, Threshold threshold) {
        return alertRepository.findLastAcknowledgedAlertBySensorAndThreshold(sensor, threshold).orElse(null);
    }

    public List<Alert> getRelevantAlertsDetailed(String username) {
        return alertRepository.findRelevantAlertsDetailedSensThresh(username);
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Alert loadAlertById(Long id) {
        return alertRepository.findById(id).orElse(null);
    }
}
