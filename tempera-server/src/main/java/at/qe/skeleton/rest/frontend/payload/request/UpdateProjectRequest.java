package at.qe.skeleton.rest.frontend.payload.request;

import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for updating a project.
 *
 * @param entryId The id of the timetable entry to update
 * @param projectId The new project to assign to the timeRecord
 * @param groupId The group which the project is assigned to
 */
public record UpdateProjectRequest(@NotNull Long entryId, @NotNull String projectId, @NotNull String groupId) {}
