package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.Userx;

import java.util.Set;

public record ExtendedGroupDto(Long groupId, String name, String description, SimpleUserDto grouplead, Set<Userx> members) {
}
