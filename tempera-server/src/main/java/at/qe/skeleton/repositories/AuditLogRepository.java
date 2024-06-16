package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AuditLog;

import java.util.List;

public interface AuditLogRepository extends AbstractRepository<AuditLog, Long> {
    List<AuditLog> findAll();
}
