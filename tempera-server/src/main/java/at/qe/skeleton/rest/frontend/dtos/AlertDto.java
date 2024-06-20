package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.AlertSeverity;
import at.qe.skeleton.model.enums.SensorType;

public record AlertDto(String id, String message, String start, String end, AlertSeverity severity, SensorType sensorType, Boolean isUpperBound) {}
