package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.State;

/**
 * Data transfer object for timetable entries.
 *
 * @param assignedProject the project assigned to the entry. If there is no project assigned, this
 *     field is filled with default project.
 */
public record TimetableEntryDto(
    Long id,
    String startTimestamp,
    String endTimestamp,
    ProjectDto assignedProject,
    State state,
    String description) {}
