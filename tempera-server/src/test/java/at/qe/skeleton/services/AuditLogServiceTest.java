package at.qe.skeleton.services;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.repositories.AuditLogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
public class AuditLogServiceTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "johndoe", authorities = "EMPLOYEE")
    void logEventAndGetAllTest() {

        boolean result = auditLogService.logEvent(LogEvent.CREATE, LogAffectedType.USER, "Test message.");
        List<AuditLog> auditLogs = auditLogService.getAll();

        assertTrue(result);
        assertEquals(1, auditLogs.size());
        AuditLog log = auditLogs.iterator().next();
        assertEquals(LogEvent.CREATE, log.getActionType());
        assertEquals(LogAffectedType.USER, log.getAffectedType());
        assertEquals("Test message.", log.getMessage());
    }
}
