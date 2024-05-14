package at.qe.skeleton.rest.frontend.payload.request;

import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import jakarta.validation.constraints.NotNull;

/**
 * @param project may be null
 */
public record UpdateDashboardDataRequest(@NotNull Visibility visibility, ProjectDto project) {}
