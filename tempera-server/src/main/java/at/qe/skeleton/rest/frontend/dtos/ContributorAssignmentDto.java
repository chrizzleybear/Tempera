package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record ContributorAssignmentDto (@NotNull Long groupId, @NotNull  Long projectId,@NotNull String contributorId){}
