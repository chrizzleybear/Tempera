package at.qe.skeleton.services;

import at.qe.skeleton.model.Alert;
import at.qe.skeleton.repositories.AlertRepository;
import org.springframework.stereotype.Service;

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
}
