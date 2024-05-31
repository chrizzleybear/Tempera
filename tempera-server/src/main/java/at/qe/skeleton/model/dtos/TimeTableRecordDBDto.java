package at.qe.skeleton.model.dtos;

import at.qe.skeleton.model.enums.State;

import java.time.LocalDateTime;

public record TimeTableRecordDBDto(Long recordId, LocalDateTime start, LocalDateTime end, Long projectId, Long groupId, State state, String description) {}
