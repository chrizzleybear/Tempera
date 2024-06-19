package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * A DTO for a group and a project.
 * @param group SimpleGroupDto
 * @param project SimpleProjectDto
 * @param managerDetails SimpleUserDto
 * @param contributors List<SimpleUserDto> (might be null)
 * @param isActive boolean
 */

public record GroupxProjectDto(@NotNull SimpleGroupDto group, @NotNull SimpleProjectDto project, @NotNull SimpleUserDto managerDetails, List<SimpleUserDto> contributors, @NotNull boolean isActive) {}
