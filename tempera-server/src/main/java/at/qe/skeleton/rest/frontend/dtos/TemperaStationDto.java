package at.qe.skeleton.rest.frontend.dtos;

public record TemperaStationDto(
    String id, String user, boolean enabled, boolean isHealthy, String accessPointId) {}
