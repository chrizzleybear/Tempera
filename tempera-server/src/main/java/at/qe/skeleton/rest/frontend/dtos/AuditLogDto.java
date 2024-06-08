package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import jakarta.validation.constraints.NotNull;

public record AuditLogDto(
    @NotNull String id,
    @NotNull String timeStamp,
    @NotNull String triggeringUserName,
    @NotNull LogEvent actionType,
    @NotNull LogAffectedType affectedType,
    String message) {}
