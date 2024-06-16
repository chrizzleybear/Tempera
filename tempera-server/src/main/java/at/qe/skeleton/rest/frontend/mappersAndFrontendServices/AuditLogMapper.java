package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.rest.frontend.dtos.AuditLogDto;
import org.springframework.stereotype.Service;

@Service
public class AuditLogMapper {
  public AuditLogDto getAuditLogDto(AuditLog auditLog) {
    return new AuditLogDto(
        auditLog.getId().toString(),
        auditLog.getTimeStamp().toString(),
        auditLog.getTriggeringUser().getUsername(),
        auditLog.getActionType(),
        auditLog.getAffectedType(),
        auditLog.getMessage());
  }
}
