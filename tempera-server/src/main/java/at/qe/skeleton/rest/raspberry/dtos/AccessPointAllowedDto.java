package at.qe.skeleton.rest.raspberry.dtos;

import java.util.List;
import java.util.UUID;

public record AccessPointAllowedDto(
    UUID id, boolean access_point_allowed, List<String> stations_allowed) {}
