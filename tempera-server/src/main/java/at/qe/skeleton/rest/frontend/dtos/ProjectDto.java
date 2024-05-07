package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.model.Userx;

import java.util.Set;

public record ProjectDto(Long id, String name, String description, Userx manager, Set<Group> groups, Set<Userx> contributors) {}
