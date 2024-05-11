package at.qe.skeleton.rest.frontend.payload.request;

import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import jakarta.validation.constraints.NotBlank;

public record UpdateDashboardDataRequest(
    @NotBlank Visibility visibility, @NotBlank ProjectDto project) {}
