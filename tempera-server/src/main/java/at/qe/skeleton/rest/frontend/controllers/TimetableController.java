package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;
import at.qe.skeleton.rest.frontend.payload.request.SplitTimeRecordRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDescriptionRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateProjectRequest;
import at.qe.skeleton.rest.frontend.payload.response.GetTimetableDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/timetable", produces = "application/json")
public class TimetableController {
  private static final Logger logger = LoggerFactory.getLogger(TimetableController.class);

  @GetMapping("/data")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<GetTimetableDataResponse> getTimetableData() {
    var entry1 =
        new TimetableEntryDto(
            1L,
            "2024-05-12T08:00:00",
            "2024-05-12T09:00:00",
            null,
            State.AVAILABLE,
            "I did a lot of work today.");
    var entry2 =
        new TimetableEntryDto(
            2L,
            "2024-05-12T09:00:00",
            "2024-05-12T12:00:00",
            new ProjectDto("2", "Project 2"),
            State.DEEPWORK,
            "This project is nice.");
    return ResponseEntity.ok(
        new GetTimetableDataResponse(
            List.of(entry1, entry2),
            List.of(new ProjectDto("1", "Project 1"), new ProjectDto("2", "Project 2"))));
  }

  @PostMapping("/update/project")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateProject(@RequestBody UpdateProjectRequest request) {
    logger.info("New project set for time record: {}", request.project());
    return ResponseEntity.ok(new MessageResponse("Project for timeRecord updated successfully."));
  }

  @PostMapping("/update/description")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateDescription(
      @RequestBody UpdateDescriptionRequest request) {
    logger.info("New description set for time record: {}", request.description());
    return ResponseEntity.ok(
        new MessageResponse("Description for timeRecord updated successfully."));
  }

  @PostMapping("/split/time-record")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> splitTimeRecord(
      @RequestBody SplitTimeRecordRequest request) {
    logger.info("Time record was split at: {}", request.splitTimestamp());
    return ResponseEntity.ok(new MessageResponse("Time record was split successfully."));
  }
}
