package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.dtos.AlertDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.AlertMapper;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
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
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    List<AlertDto> alerts = alertMapper.getAlerts(username);

    return  ResponseEntity.ok(alerts);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'GROUPLEAD', 'EMPLOYEE')")
  public ResponseEntity<MessageResponse> deleteAlert(@PathVariable String id) {
    logger.info("deleteAlert called with id: {} ", id);
    try {
    alertMapper.deleteAlert(id);
    return ResponseEntity.ok(new MessageResponse("Alert " + id + " deleted successfully."));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
    }
  }
}
