package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.UserxRole;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserxDto (
        @NotNull String username,
        String firstName,
        String lastName,
        String email,
        String password,
        boolean enabled,
        Set<UserxRole> roles
) {}