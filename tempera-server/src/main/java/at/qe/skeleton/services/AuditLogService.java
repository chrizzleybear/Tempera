package at.qe.skeleton.services;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.repositories.AuditLogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("application")
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    private final UserxRepository userxRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, UserxRepository userxRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userxRepository = userxRepository;
    }

    public boolean logEvent(LogEvent actionType, LogAffectedType affectedType, String message) {
        Userx user;
        try {
            // User has to be set without using userxService to prevent cyclic dependencies
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            user = userxRepository.findById(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User " + auth.getName() + " not found, but has to exist."));
        } catch (NullPointerException e) {
            user = null;
        }
        AuditLog a = new AuditLog(user, actionType, affectedType, message);
        return auditLogRepository.save(a) != null;
    }

    public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }
}
