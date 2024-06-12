package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.ClimateQuality;

public record FrontendMeasurementDto(Double value, ClimateQuality quality) {}
