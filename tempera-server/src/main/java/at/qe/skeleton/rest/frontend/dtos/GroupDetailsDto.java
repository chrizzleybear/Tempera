package at.qe.skeleton.rest.frontend.dtos;

public record GroupDetailsDto(String id, String name, String description, SimpleUserDto groupLead) {
}
