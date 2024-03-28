package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import jakarta.persistence.*;

@Entity
public class Threshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private SensorType sensorType;
    private ThresholdType thresholdType;
    private double value;
    @OneToOne
    private ModificationReason modificationReason;

    @OneToOne
    private ThresholdTip tip;


    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public ThresholdType getThresholdType() {
        return thresholdType;
    }

    public void setThresholdType(ThresholdType thresholdType) {
        this.thresholdType = thresholdType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ModificationReason getModificationReason() {
        return modificationReason;
    }

    public void setModificationReason(ModificationReason modificationReason) {
        this.modificationReason = modificationReason;
    }

    public ThresholdTip getTip() {
        return tip;
    }

    public void setTip(ThresholdTip tip) {
        this.tip = tip;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

