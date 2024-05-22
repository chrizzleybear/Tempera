package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.Groupx;

import java.util.List;

public record ExtendedProjectDto(String id, String name, String description, String manager, List<Groupx> groups, List<UserSimpleDto> contributors) {}
