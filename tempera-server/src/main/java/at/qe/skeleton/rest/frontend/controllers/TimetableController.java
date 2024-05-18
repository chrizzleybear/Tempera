package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;
import at.qe.skeleton.rest.frontend.mappers.TimeTableDataMapper;
import at.qe.skeleton.rest.frontend.payload.request.UpdateTimetableDataRequest;
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

  @PostMapping("/update")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateTimetableEntry(
      @RequestBody UpdateTimetableDataRequest request) {
    if (request.project() != null) {
      // update project
      timeTabeControllerLogger.info("New project set for time record: {}", request.project());
    }
    if (request.description() != null) {
      // update description
      timeTabeControllerLogger.info("New description set for time record: {}", request.description());
    }
    if (request.splitTimestamp() != null) {
      // update splitTimestamp
      timeTabeControllerLogger.info("Time record was split at: {}", request.splitTimestamp());
    }
    return ResponseEntity.ok(new MessageResponse("Timetable data updated: "));
  }
}
