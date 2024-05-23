package at.qe.skeleton.rest.frontend.dtos;

import java.util.List;

public record GroupxProjectDto(SimpleGroupDto group, SimpleProjectDto project, SimpleUserDto managerDetails, List<SimpleUserDto> contributors) {}

//todo: braucht io hier alle extended?
