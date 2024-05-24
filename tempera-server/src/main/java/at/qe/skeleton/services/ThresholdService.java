package at.qe.skeleton.services;

import at.qe.skeleton.model.Modification;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.ThresholdTip;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import at.qe.skeleton.repositories.ThresholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Scope("application")
public class ThresholdService {

    private final ThresholdRepository thresholdRepository;

    @Autowired
    public ThresholdService(ThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Transactional
    public List<Threshold> getAllThresholds() {
        return thresholdRepository.findAll();
    }

    @Transactional
    public List<Threshold> getDefaultThresholds() {
        return thresholdRepository.findDefaultThresholds();
    }

    @Transactional
    public Threshold createThreshold(SensorType sensorType, ThresholdType thresholdType, double value, String reason, String tip) {
        Threshold t = new Threshold(sensorType, thresholdType, value,
                new Modification(reason),
                new ThresholdTip(tip)
        );
        return thresholdRepository.save(t);
    }

    @Transactional
    public Threshold updateThreshold(Threshold oldThreshold, SensorType newSensorType, ThresholdType newThresholdType, double newValue, String newReason, String newTip) {
        oldThreshold.setSensorType(newSensorType);
        oldThreshold.setThresholdType(newThresholdType);
        oldThreshold.setValue(newValue);
        oldThreshold.setModificationReason(new Modification(newReason));
        oldThreshold.setTip(new ThresholdTip(newTip));
        return thresholdRepository.save(oldThreshold);
    }

    @Transactional
    public void deleteThreshold(Long id) {
        Threshold t = thresholdRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Threshold not found: " + id));
        thresholdRepository.delete(t);
    }
}
