package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record ContributorAssignmentDto (@NotNull String groupId, @NotNull  String projectId,@NotNull String contributorId){}
