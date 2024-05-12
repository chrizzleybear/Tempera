package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.mappers.DashboardDataMapper;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.services.UserxService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class DashboardController {

  private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

  private final DashboardDataMapper dashboardDataMapper;
  private final UserxService userXService;

  public DashboardController(DashboardDataMapper dashboardDataMapper, UserxService userXService) {
    this.userXService = userXService;
    this.dashboardDataMapper = dashboardDataMapper;
  }

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

    Userx user = userXService.loadUser(username);

    DashboardDataResponse homeDataResponse = dashboardDataMapper.mapUserToHomeDataResponse(user);
    return ResponseEntity.ok(homeDataResponse);
  }

  @PostMapping("/dashboardData")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateDashboardData(
      @RequestBody UpdateDashboardDataRequest request) {
    return ResponseEntity.ok(new MessageResponse("Dashboard data updated successfully!"));
  }
}
