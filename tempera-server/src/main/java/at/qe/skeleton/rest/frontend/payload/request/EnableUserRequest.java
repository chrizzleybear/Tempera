package at.qe.skeleton.rest.frontend.payload.request;

import jakarta.validation.constraints.NotNull;

public record EnableUserRequest(@NotNull String username, @NotNull String token, @NotNull String password) {}
