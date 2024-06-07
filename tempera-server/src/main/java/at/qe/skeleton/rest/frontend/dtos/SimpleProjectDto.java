package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record SimpleProjectDto (@NotNull String projectId, @NotNull boolean isActive, @NotNull String name,@NotNull String description, @NotNull String manager) {}

