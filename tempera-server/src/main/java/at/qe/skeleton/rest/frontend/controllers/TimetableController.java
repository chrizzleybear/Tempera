package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;
import at.qe.skeleton.rest.frontend.mappers.TimeTableDataMapper;
import at.qe.skeleton.rest.frontend.payload.request.UpdateTimetableDataRequest;
import at.qe.skeleton.rest.frontend.payload.request.SplitTimeRecordRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDescriptionRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateProjectRequest;
import at.qe.skeleton.rest.frontend.payload.response.GetTimetableDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.UserxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/timetable", produces = "application/json")
public class TimetableController {
  private static final Logger timeTabeControllerLogger = LoggerFactory.getLogger(TimetableController.class);
  private final TimeTableDataMapper timeTableDataMapper;
  private final UserxService userService;

  public TimetableController(TimeTableDataMapper timeTableDataMapper, UserxService userService){
    this.timeTableDataMapper = timeTableDataMapper;
    this.userService = userService;
  }

  @GetMapping("/data")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<GetTimetableDataResponse> getTimetableData() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Userx user = userService.loadUser(username);
    // for now we set the page and size in the controller itself, but can easily be sent by Frontend
    int page = 0;
    int size = 20;
    GetTimetableDataResponse response = timeTableDataMapper.getTimetableData(user, page, size);
    timeTabeControllerLogger.info("created TimeTableDataResponse page %d with size %d".formatted(page, size));
    return ResponseEntity.ok(response);
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
