package at.qe.skeleton.exceptions;

import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;

public class ThresholdNotAvailableException extends RuntimeException{
    private final String message;
    public ThresholdNotAvailableException(SensorType sensorType, ThresholdType thresholdType) {
        message = "Threshold for sensor type " + sensorType + " and threshold type " + thresholdType + " not available";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
