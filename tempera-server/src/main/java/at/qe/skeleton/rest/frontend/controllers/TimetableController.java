package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.TimetableDataService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/timetable", produces = "application/json")
public class TimetableController {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(TimetableController.class);
  private final TimetableDataService timeTableDataService;
  private final UserxService userService;

  public TimetableController(TimetableDataService timeTableDataService, UserxService userService) {
    this.timeTableDataService = timeTableDataService;
    this.userService = userService;
  }

  @GetMapping("/data")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<GetTimetableDataResponse> getTimetableData() {
    LOGGER.info("getTimetableData");
    try {
      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      GetTimetableDataResponse response = timeTableDataService.getTimetableData(username);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      LOGGER.warn("getTimetableData failed", e);
      return ResponseEntity.badRequest().build();
    }

  }

  @PostMapping("/update/project")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateProject(@RequestBody UpdateProjectRequest request) {
    LOGGER.info("updateProject");
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    MessageResponse response;
    try {
      response = timeTableDataService.updateProject(username, request);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/update/description")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateDescription(
      @RequestBody UpdateDescriptionRequest request) {
    LOGGER.info("updateDescription");
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    MessageResponse response;
    try {
      response = timeTableDataService.updateProjectDescription(username, request);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/split/time-record")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<GetTimetableDataResponse> splitTimeRecord(
      @RequestBody SplitTimeRecordRequest request) {
    LOGGER.info("splitTimeRecord");
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    GetTimetableDataResponse response;
    try {
      response = timeTableDataService.splitTimeRecord(username, request);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(response);
  }
}
