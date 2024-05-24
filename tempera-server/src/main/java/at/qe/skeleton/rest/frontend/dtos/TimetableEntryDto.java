package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.State;
import jakarta.validation.constraints.NotNull;

/**
 * Data transfer object for timetable entries.
 *
 * @param id the id of the entry.
 * @param startTimestamp the start timestamp of the entry.
 * @param endTimestamp the end timestamp of the entry.
 * @param state the state of the entry.
 * @param assignedProject the project assigned to the entry. If there is no project assigned, this
 *     field is filled with default project.
 * @param description the description of the entry.
 */
public record TimetableEntryDto(
   @NotNull Long id,
   @NotNull String startTimestamp,
   @NotNull String endTimestamp,
   SimpleProjectDto assignedProject,
   @NotNull State state,
   String description) {}
