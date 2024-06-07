package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;

public record AuditLogDto (
        String id,
        String timeStamp,
        SimpleUserDto triggeringUser,
        LogEvent actionType,
        LogAffectedType affectedType,
        String message
)
{}
