package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record GroupDetailsDto(@NotNull String id, @NotNull String name, @NotNull String description, @NotNull SimpleUserDto groupLead) {
}
