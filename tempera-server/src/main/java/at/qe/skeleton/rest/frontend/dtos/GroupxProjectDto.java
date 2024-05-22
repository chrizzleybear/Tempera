package at.qe.skeleton.rest.frontend.dtos;

import java.util.List;

public record GroupxProjectDto(ExtendedGroupDto extendedGroupDto, ExtendedProjectDto extendedProjectDto, UserSimpleDto managerDetails, List<UserSimpleDto> members) {}
