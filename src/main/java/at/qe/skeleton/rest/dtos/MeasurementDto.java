package at.qe.skeleton.rest.dtos;

import java.time.LocalDateTime;

public record MeasurementDto(Long id, Long sensorId, String stationId, Double value, String unit, LocalDateTime timestamp) {}
