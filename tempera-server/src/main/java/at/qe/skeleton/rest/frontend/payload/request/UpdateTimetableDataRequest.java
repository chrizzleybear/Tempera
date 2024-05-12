package at.qe.skeleton.rest.frontend.payload.request;

import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for updating a timetable entry. Fields can be null, in which case they are not
 * updated.
 *
 * @param entryId The id of the timetable entry to update
 * @param splitTimestamp Time at which the time record should be split
 */
public record UpdateTimetableDataRequest(
    @NotNull Long entryId, ProjectDto project, String description, String splitTimestamp) {}
