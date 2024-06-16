package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.UserxRole;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserxDto (
        @NotNull String username,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String email,
        @NotNull String password,
        @NotNull boolean enabled,
        @NotNull Set<UserxRole> roles
) {}