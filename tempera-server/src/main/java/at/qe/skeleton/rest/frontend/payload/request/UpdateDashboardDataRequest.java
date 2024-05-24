package at.qe.skeleton.rest.frontend.payload.request;

import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import jakarta.validation.constraints.NotNull;

/**
 * @param project may be null
 */
public record UpdateDashboardDataRequest(@NotNull Visibility visibility, SimpleProjectDto project) {}
