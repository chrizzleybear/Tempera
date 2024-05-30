package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/auditlogs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditLogController {

    @Autowired private AuditLogService auditLogService;

    @GetMapping("/all")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        List<AuditLog> auditLogs = auditLogService.getAll();
        return ResponseEntity.ok(auditLogs);
    }

}
