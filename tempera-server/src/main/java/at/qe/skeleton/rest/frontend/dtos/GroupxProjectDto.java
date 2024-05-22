package at.qe.skeleton.rest.frontend.dtos;

import java.util.List;

public record GroupxProjectDto(ExtendedGroupDto extendedGroupDto, ExtendedProjectDto extendedProjectDto, SimpleUserDto managerDetails, List<SimpleUserDto> members) {}
