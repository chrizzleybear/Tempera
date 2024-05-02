package at.qe.skeleton.rest.raspberry.dtos;

import at.qe.skeleton.model.TemperaStation;

import java.util.List;
import java.util.UUID;

public record AccessPointDto(UUID id, boolean access_point_allowed, List<String> stations_allowed) {}
