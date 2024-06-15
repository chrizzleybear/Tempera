package at.qe.skeleton.rest.raspberry.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeasurementDto(
    UUID access_point_id,
    String tempera_station_id,
    LocalDateTime timestamp,
    Double temperature,
    Double irradiance,
    Double humidity,
    Double nmvoc) {}