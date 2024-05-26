package at.qe.skeleton.model.dtos;

import at.qe.skeleton.model.enums.State;

import java.time.LocalDateTime;

public record GroupxProjectStateTimeDbDto(Long projectId, Long groupId, State state, LocalDateTime start, LocalDateTime end) {}
