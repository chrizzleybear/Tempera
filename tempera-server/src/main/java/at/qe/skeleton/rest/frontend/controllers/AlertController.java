package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.enums.AlertSeverity;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.rest.frontend.dtos.AlertDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/alerts", produces = "application/json")
public class AlertController {
  private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

  @GetMapping("/all")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'GROUPLEAD', 'EMPLOYEE')")
  public ResponseEntity<List<AlertDto>> getAlerts() {
    logger.info("getAlerts called");
    return ResponseEntity.ok(
            List.of(
                    new AlertDto("1", "Warning 1", "2021-08-01T12:00:00", "2021-08-01T13:00:00", AlertSeverity.WARNING, SensorType.HUMIDITY),
                    new AlertDto("2", "Warning 2", "2021-08-01T10:00:00", "2021-08-01T10:30:00", AlertSeverity.WARNING, SensorType.IRRADIANCE),
                    new AlertDto("3", "Warning 3", "2021-08-02T12:00:00", "2021-08-02T14:00:00", AlertSeverity.WARNING, SensorType.TEMPERATURE)));
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'GROUPLEAD', 'EMPLOYEE')")
  public ResponseEntity<String> deleteAlert(@PathVariable String id) {
    logger.info("deleteAlert called with id: {} ", id);
    return ResponseEntity.ok("Alert deleted");
  }
}
