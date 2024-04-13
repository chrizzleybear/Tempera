package at.qe.skeleton.rest.dtos;

import at.qe.skeleton.model.enums.Unit;

import java.time.LocalDateTime;

public record MeasurementDto(Long id, Long sensorId, String stationId, Double value, Unit unit, LocalDateTime timestamp) {}
