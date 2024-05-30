package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.dtos.WarningDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/warnings", produces = "application/json")
public class WarningController {
  private static final Logger logger = LoggerFactory.getLogger(WarningController.class);

  @GetMapping("/all")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'GROUPLEAD', 'EMPLOYEE')")
  public ResponseEntity<List<WarningDto>> getWarnings() {
    logger.info("getAllWarnings called");
    return ResponseEntity.ok(
        List.of(
            new WarningDto("1", "Warning 1"),
            new WarningDto("2", "Warning 2"),
            new WarningDto("3", "Warning 3")));
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'GROUPLEAD', 'EMPLOYEE')")
  public ResponseEntity<String> deleteWarning(@PathVariable String id) {
    logger.info("deleteWarning called with id: {} ", id);
    return ResponseEntity.ok("Warning deleted");
  }
}
