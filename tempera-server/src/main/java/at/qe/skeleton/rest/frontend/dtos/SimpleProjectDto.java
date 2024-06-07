package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

/**
 * A simple DTO for a project.
 * @param projectId String
 * @param isActive boolean
 * @param name String
 * @param description String
 * @param manager String
 */
public record SimpleProjectDto (@NotNull String projectId, @NotNull boolean isActive, @NotNull String name,@NotNull String description, @NotNull String manager) {}

