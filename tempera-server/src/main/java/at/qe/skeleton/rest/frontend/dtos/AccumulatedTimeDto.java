package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record AccumulatedTimeDto(
        SimpleProjectDto project,
        SimpleGroupDto group,
        @NotNull String startTimestamp,
        @NotNull String endTimestamp) {}
