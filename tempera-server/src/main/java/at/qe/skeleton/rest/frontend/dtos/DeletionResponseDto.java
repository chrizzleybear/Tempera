package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.DeletionResponseType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DeletionResponseDto(@NotNull DeletionResponseType responseType,  List<SimpleProjectDto> affectedProjects,  List<SimpleGroupDto> affectedGroups) {}
