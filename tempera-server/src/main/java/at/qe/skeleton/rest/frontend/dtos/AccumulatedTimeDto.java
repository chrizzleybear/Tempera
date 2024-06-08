package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.State;
import jakarta.validation.constraints.NotNull;

public record AccumulatedTimeDto(
        String projectId,
        String groupId,
        @NotNull boolean isActive,
        @NotNull State state,
        @NotNull String startTimestamp,
        @NotNull String endTimestamp) {}
