package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.dtos.AccumulatedTimeDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.payload.response.AccumulatedTimeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/accumulated-time", produces = "application/json")
public class AccumulatedTimeController {
  private static final Logger logger = LoggerFactory.getLogger(AccumulatedTimeController.class);

  @GetMapping("/data")
  @PreAuthorize("hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<AccumulatedTimeResponse> getAccumulatedTimeData() {
    logger.info("getAccumulatedTimeData called");
    var response =
            new AccumulatedTimeResponse(
                    List.of(
                            new AccumulatedTimeDto(
                                    new SimpleProjectDto("1L", "Project 1", null, "manager1"),
                                    new SimpleGroupDto("1L", "Group 1", "Description 1", null),
                                    "2024-01-01T00:00:00",
                                    "2024-01-01T05:00:00")),
                    List.of(
                            new SimpleProjectDto("1L", "Project 1", null, "manager1"),
                            new SimpleProjectDto("2L", "Project 2", null, "manager1")),
                    List.of(
                            new SimpleGroupDto("1L", "Group 1", null, "grouplead1"),
                            new SimpleGroupDto("2L", "Group 2", null, "grouplead1")));
    return ResponseEntity.ok(response);
  }
}
