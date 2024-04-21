package at.qe.skeleton.rest.dtos;

import at.qe.skeleton.model.enums.Unit;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeasurementDto(Long id, Long sensorId, String stationId, UUID accessPointId, Double value, Unit unit, LocalDateTime timestamp) {}

