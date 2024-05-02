package at.qe.skeleton.rest.raspberry.dtos;

import at.qe.skeleton.model.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

public record SuperiorTimeRecordDto (Long Id, String stationId, UUID accessPointId, LocalDateTime start, LocalDateTime end, State state){};
