package at.qe.skeleton.rest.frontend.dtos;


import jakarta.validation.constraints.NotNull;

public record SimpleGroupDto(@NotNull String id, @NotNull Boolean isActive, @NotNull String name, @NotNull String description, String groupLead) {


}
