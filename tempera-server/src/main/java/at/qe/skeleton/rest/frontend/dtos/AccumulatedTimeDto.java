package at.qe.skeleton.rest.frontend.dtos;

public record AccumulatedTimeDto(
    ProjectDto project, GroupDto group, String startTimestamp, String endTimestamp) {}
