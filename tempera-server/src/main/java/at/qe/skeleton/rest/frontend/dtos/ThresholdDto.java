package at.qe.skeleton.rest.frontend.dtos;

public record ThresholdDto(String thresholdId, String name, String description, double value, boolean active) {
}
