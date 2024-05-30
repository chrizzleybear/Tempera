package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.AuditLog;

import java.util.Collection;

public record AuditLogDto (
        Collection<AuditLog> auditLogs
)
{}
