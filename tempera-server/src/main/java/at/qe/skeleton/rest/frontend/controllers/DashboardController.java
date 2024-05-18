package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.DashboardDataMapper;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDashboardDataRequest;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.UserxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  public ResponseEntity<DashboardDataResponse> getDashboardData(@RequestParam String username) {
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
