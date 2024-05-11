package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDashboardDataRequest;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class DashboardController {

  private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

  @GetMapping("/dashboardData")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<DashboardDataResponse> getDashboardData(@RequestParam String username) {
    var colleague1 =
        new ColleagueStateDto(
            "Max Mustermann", "Raum 1", State.DEEPWORK, true, List.of("Gruppe 1"));
    var colleague2 =
        new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE, true, List.of("Gruppe 2"));
    var colleague3 =
        new ColleagueStateDto(
            "Otto Normal", "Raum 2", State.MEETING, true, List.of("Gruppe 1", "Gruppe 2"));
    var colleague4 =
        new ColleagueStateDto(
            "Hans Wurst", "Raum 4", State.OUT_OF_OFFICE, true, List.of("Gruppe 3"));
    var colleague5 =
        new ColleagueStateDto(
            "Peter Fröhlich", "Raum 1", State.OUT_OF_OFFICE, false, List.of("Gruppe 3"));

    var colleagueStates =
        List.of(
            colleague1,
            colleague2,
            colleague3,
            colleague4,
            colleague5,
            colleague2,
            colleague1,
            colleague3,
            colleague2,
            colleague4);

    return ResponseEntity.ok(
        new DashboardDataResponse(
            25.0,
            50.0,
            100.0,
            500.0,
            Visibility.PUBLIC,
            State.AVAILABLE,
            LocalDateTime.now().toString(),
            new ProjectDto(2L, "Projekt 2"),
            List.of(new ProjectDto(1L, "Projekt 1"), new ProjectDto(2L, "Projekt 2")),
            colleagueStates));
  }

  @PostMapping("/dashboardData")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateDashboardData(
      @RequestBody UpdateDashboardDataRequest request) {
    return ResponseEntity.ok(new MessageResponse("Dashboard data updated successfully!"));
  }
}
