package at.qe.skeleton.services;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.repositories.AuditLogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@Scope("application")
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserxRepository userxRepository;
    private final UserDetailsImpl userDetails;

    @Autowired
    public AuditLogService(AuditLogRepository auditLogRepository, UserxRepository userxRepository, UserDetailsImpl userDetails) {
        this.auditLogRepository = auditLogRepository;
        this.userxRepository = userxRepository;
        this.userDetails = userDetails;
    }

    public AuditLog logEvent(LogEvent actionType, LogAffectedType affectedType, String message) {
        Optional<Userx> userx = userxRepository.findById(userDetails.getId());
        if (userx.isEmpty()) {
            userx = null;
            message = "User was not found. " + message;
        }
        AuditLog a = new AuditLog(userx.get(), actionType, affectedType, message);
        return auditLogRepository.save(a);
    }

    public Collection<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }
}
