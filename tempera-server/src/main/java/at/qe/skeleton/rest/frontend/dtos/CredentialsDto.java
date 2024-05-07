package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record CredentialsDto(@NotNull String username, @NotNull String password) {}
