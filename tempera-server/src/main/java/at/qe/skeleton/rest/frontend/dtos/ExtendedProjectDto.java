package at.qe.skeleton.rest.frontend.dtos;

import java.util.List;

public record ExtendedProjectDto(SimpleUserDto manager, SimpleProjectDto simpleProjectDto, List<SimpleGroupDto> connectedGroups, List<SimpleUserDto> contributors) {}
