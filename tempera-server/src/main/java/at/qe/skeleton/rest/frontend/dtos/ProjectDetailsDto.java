package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record ProjectDetailsDto(@NotNull String projectId, @NotNull String name, @NotNull String description, @NotNull SimpleUserDto manager) {}
