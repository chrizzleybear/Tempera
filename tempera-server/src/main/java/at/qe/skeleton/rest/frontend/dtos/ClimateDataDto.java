package at.qe.skeleton.rest.frontend.dtos;

import java.util.List;
import java.util.UUID;

public record ClimateDataDto(
    UUID access_point_id, SensorDto sensor, List<ClimateMeasurementDto> measurementDtos) {}
