package at.qe.skeleton.services;

import at.qe.skeleton.model.Modification;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.ThresholdTip;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import at.qe.skeleton.repositories.TemperaStationRepository;
import at.qe.skeleton.repositories.ThresholdRepository;
import at.qe.skeleton.repositories.ThresholdTipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@Scope("application")
public class ThresholdService {

    private final ThresholdRepository thresholdRepository;
    private final TemperaStationRepository temperaStationRepository;
    private final ThresholdTipRepository thresholdTipRepository;
    private final AuditLogService auditLogService;

    @Autowired
    public ThresholdService(ThresholdRepository thresholdRepository,
                            TemperaStationRepository temperaStationRepository,
                            ThresholdTipRepository thresholdTipRepository,
                            AuditLogService auditLogService) {
        this.thresholdRepository = thresholdRepository;
        this.temperaStationRepository = temperaStationRepository;
        this.thresholdTipRepository = thresholdTipRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public Threshold createThreshold(SensorType sensorType, ThresholdType thresholdType, double value, String reason, String tip) {
        Threshold t = new Threshold(sensorType, thresholdType, value,
                new Modification(reason),
                new ThresholdTip(tip)
        );
        return thresholdRepository.save(t);
    }

  public Set<Threshold> getThresholdsByTemperaId(String temperaId) {
    return temperaStationRepository.getThresholdsByTemperaId(temperaId);
  }

  public Set<Threshold> getThresholdsByUsername(String username) {
    return thresholdRepository.getThresholdsByUsername(username);
  }

  public Threshold saveThreshold(Threshold threshold) {
    return thresholdRepository.save(threshold);
  }

  @Transactional
  public Threshold updateThreshold(
      Threshold oldThreshold,
      SensorType newSensorType,
      ThresholdType newThresholdType,
      double newValue,
      String newReason,
      String newTip) {
    oldThreshold.setSensorType(newSensorType);
    oldThreshold.setThresholdType(newThresholdType);
    oldThreshold.setValue(newValue);
    oldThreshold.setModificationReason(new Modification(newReason));
    oldThreshold.setTip(new ThresholdTip(newTip));
    return thresholdRepository.save(oldThreshold);
  }

  @Transactional
  public void deleteThreshold(Long id) {
    Threshold t =
        thresholdRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Threshold not found: " + id));
    thresholdRepository.delete(t);
  }

    public Threshold getThresholdById(Long id) {
        return thresholdRepository.findById(id).orElse(null);
    }

    public List<ThresholdTip> getAllThresholdTips() {
        return thresholdTipRepository.findAll();
    }

    public ThresholdTip getThresholdTipById(Long id) {
        return thresholdTipRepository.findById(id).orElse(null);
    }

    @Transactional
    public ThresholdTip updateThresholdTip(ThresholdTip tip) {
        if (thresholdTipRepository.findById(tip.getId()).isEmpty()) {
            throw new IllegalArgumentException("Threshold not found");
        }
        auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.THRESHOLD,
                "Tip of threshold with id: " + tip.getId() + " was updated to " + tip.getTip() + ".");
        return thresholdTipRepository.save(tip);
    }

}
