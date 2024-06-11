package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;

import java.util.Set;

public record ExtendedGroupDto(SimpleGroupDto simpleGroupDto, Set<GroupxProjectDto> projects, Set<SimpleUserDto> members) {
}
