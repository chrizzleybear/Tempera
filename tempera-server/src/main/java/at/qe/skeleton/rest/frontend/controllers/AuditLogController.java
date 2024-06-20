package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.dtos.AuditLogDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.AuditLogMapper;
import at.qe.skeleton.services.AuditLogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/auditlogs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditLogController {

    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    public AuditLogController(AuditLogService auditLogService, AuditLogMapper auditLogMapper) {
        this.auditLogService = auditLogService;
        this.auditLogMapper = auditLogMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AuditLogDto>> getAllAuditLogs() {
        List<AuditLogDto> auditLogs = auditLogService.getAll().stream().map(auditLogMapper :: getAuditLogDto).toList();
        return ResponseEntity.ok(auditLogs);
    }

}
