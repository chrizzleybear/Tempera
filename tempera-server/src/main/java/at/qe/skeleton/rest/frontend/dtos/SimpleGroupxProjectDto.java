package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record SimpleGroupxProjectDto(@NotNull String groupId, @NotNull String groupName,@NotNull String projectId,@NotNull String projectName) {}
