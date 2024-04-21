package at.qe.skeleton.rest.dtos;

import at.qe.skeleton.model.TemperaStation;

import java.util.List;
import java.util.UUID;

public record AccessPointDto(UUID id, boolean isEnabled, List<TemperaStation> EnabledTemperaStations) {}
