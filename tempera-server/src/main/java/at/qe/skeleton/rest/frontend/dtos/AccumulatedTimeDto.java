package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record AccumulatedTimeDto(
    ProjectDto project,
    GroupDto group,
    @NotNull String startTimestamp,
    @NotNull String endTimestamp) {}
