package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record SimpleUserDto(@NotNull String username,@NotNull String firstName,@NotNull String lastName,@NotNull String email) {

}
