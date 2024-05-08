package at.qe.skeleton.rest.raspberry.dtos;

import at.qe.skeleton.model.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExternalRecordDto(
    UUID access_point_id,
    String tempera_station_id,
    LocalDateTime start,
    long duration,
    State mode,
    boolean auto_update) {}
;
