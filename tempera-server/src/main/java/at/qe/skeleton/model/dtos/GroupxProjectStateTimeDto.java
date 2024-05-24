package at.qe.skeleton.model.dtos;

import at.qe.skeleton.model.enums.State;

import java.time.LocalDateTime;

public record GroupxProjectStateTimeDto(Long projectId, Long groupId, State state, LocalDateTime start, LocalDateTime end) {}
