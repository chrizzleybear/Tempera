package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.rest.frontend.dtos.AuditLogDto;
import org.springframework.stereotype.Service;

@Service
public class AuditLogMapper {
    private final UserMapper userMapper;

    public AuditLogMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public AuditLogDto getAuditLogDto(AuditLog auditLog) {
        return new AuditLogDto(
                auditLog.getId().toString(),
                auditLog.getTimeStamp().toString(),
                userMapper.getSimpleUser(auditLog.getTriggeringUser()),
                auditLog.getActionType(),
                auditLog.getAffectedType(),
                auditLog.getMessage()
        );
    }
}
