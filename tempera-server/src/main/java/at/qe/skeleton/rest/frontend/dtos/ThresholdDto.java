package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.ThresholdTip;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;

public record ThresholdDto(Long id, SensorType sensorType, ThresholdType thresholdType, double value, ThresholdTip tip) {

}
