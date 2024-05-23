package at.qe.skeleton.rest.frontend.payload.request;

import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for updating a project.
 *
 * @param entryId The id of the timetable entry to update
 * @param project The new project to assign to the timeRecord
 */
public record UpdateProjectRequest(@NotNull Long entryId, @NotNull SimpleProjectDto project) {}
