package at.qe.skeleton.rest.frontend.payload.request;

import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import jakarta.validation.constraints.NotNull;

/**
 * @param visibility the currently set visibility of the State
 * @param groupxProject the gxp assigned to this timeRecord (may be null)
 */
public record UpdateDashboardDataRequest(@NotNull Visibility visibility, SimpleGroupxProjectDto groupxProject) {}
