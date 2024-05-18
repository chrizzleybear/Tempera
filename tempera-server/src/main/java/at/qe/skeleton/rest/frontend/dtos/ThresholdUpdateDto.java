package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.Threshold;

public record ThresholdUpdateDto(ThresholdDto threshold, String reason) {}