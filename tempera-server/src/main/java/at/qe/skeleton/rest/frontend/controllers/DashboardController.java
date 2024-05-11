package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class DashboardController {

  @GetMapping("/dashboardData")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<DashboardDataResponse> dashboardData() {
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

    var colleagueStates =
        List.of(
            colleague1,
            colleague2,
            colleague3,
            colleague4,
            colleague2,
            colleague1,
            colleague3,
            colleague2,
            colleague4);

    return ResponseEntity.ok(
        new DashboardDataResponse(
            25,
            50,
            100,
            500,
            Visibility.PUBLIC,
            State.AVAILABLE,
            LocalDateTime.now().toString(),
            null,
            colleagueStates));
  }
}
