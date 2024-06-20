package at.qe.skeleton.rest.frontend.dtos;

import java.time.LocalDateTime;

public record ClimateMeasurementDto(
    LocalDateTime timestamp,
    Double value) {}
