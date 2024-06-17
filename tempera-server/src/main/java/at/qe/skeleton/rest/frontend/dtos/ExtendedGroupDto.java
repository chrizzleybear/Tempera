package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public record ExtendedGroupDto(@NotNull GroupDetailsDto groupDetailsDto, @NotNull List<SimpleProjectDto> activeProjects, @NotNull Set<SimpleUserDto> members) {
}
