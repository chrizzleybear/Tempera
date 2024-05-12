package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.State;

public record TimetableEntryDto(
    Long id,
    String startTimestamp,
    String endTimestamp,
    ProjectDto assignedProject,
    State state,
    String description) {}
