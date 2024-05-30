package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.enums.AlertSeverity;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.rest.frontend.dtos.AlertDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.AlertMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/alerts", produces = "application/json")
public class AlertController {
  private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
  private final AlertMapper alertMapper;

    public AlertController(AlertMapper alertMapper) {
        this.alertMapper = alertMapper;
    }


  @GetMapping("/all")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'GROUPLEAD', 'EMPLOYEE')")
  public ResponseEntity<List<AlertDto>> getAlerts() {
    logger.info("getAlerts called");
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    List<AlertDto> alerts = alertMapper.getAlerts(username);
    return  ResponseEntity.ok(alerts);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'GROUPLEAD', 'EMPLOYEE')")
  public ResponseEntity<String> deleteAlert(@PathVariable String id) {
    logger.info("deleteAlert called with id: {} ", id);
    try {
    alertMapper.deleteAlert(id);
    return ResponseEntity.ok("Alert " + id + " deleted successfully.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
