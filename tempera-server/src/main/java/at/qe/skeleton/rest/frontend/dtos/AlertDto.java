package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.AlertSeverity;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;

import java.time.LocalDateTime;

public record AlertDto(String id, String message, String start, String end, AlertSeverity severity, SensorType sensorType) {}
