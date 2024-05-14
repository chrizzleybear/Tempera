package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.Userx;

import java.util.Set;

public record GroupDto(Long groupId, String name, String description, Userx groupLead, Set<Userx> members) {
}
