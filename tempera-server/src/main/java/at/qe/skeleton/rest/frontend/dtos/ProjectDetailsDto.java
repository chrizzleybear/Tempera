package at.qe.skeleton.rest.frontend.dtos;

public record ProjectDetailsDto(String projectId, String name, String description, SimpleUserDto manager) {}
