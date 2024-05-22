package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.DashboardDataMapper;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DashboardControllerTest {

  private DashboardController dashboardController;
  @Mock private DashboardDataMapper dashboardDataMapper;
  @Mock private UserxService userXService;

  @BeforeEach
  void setUp() {
    dashboardController = new DashboardController(dashboardDataMapper, userXService);
  }

  @AfterEach
  void tearDown() {}

  @Test
  @WithMockUser(username ="johndoe", authorities = "EMPLOYEE")
  void homeData() {
    Userx johnny = new Userx();
    johnny.setUsername("johndoe");
    List<String> noGroups = new ArrayList<>();
    var colleagueStates =
        List.of(
            new ColleagueStateDto("Max Mustermann", "Raum 1", State.DEEPWORK, true, List.of("Gruppe 1")),
            new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE, true, List.of("Gruppe1","Gruppe 2")),
            new ColleagueStateDto("Cooler Typ", "Raum 1", State.MEETING, false, noGroups));
    var projects = List.of(new ExtendedProjectDto("project1", "Projekt 1"), new ExtendedProjectDto("project2", "Projekt 2"), new ExtendedProjectDto("project3", "Projekt 3"));
    DashboardDataResponse dashboardDataResponse =
        new DashboardDataResponse(
            1.0,
            20.0,
            300.0,
            400.2189,
            Visibility.HIDDEN,
            State.DEEPWORK,
            "10.05.2024T12:20:10",
            projects.get(0),
            projects,
            colleagueStates);

    when(dashboardDataMapper.mapUserToHomeDataResponse(johnny)).thenReturn(dashboardDataResponse);
    when(userXService.loadUser(johnny.getUsername())).thenReturn(johnny);


    ResponseEntity<DashboardDataResponse> returnValue = dashboardController.getDashboardData(johnny.getUsername());
    DashboardDataResponse response = returnValue.getBody();

    verify(dashboardDataMapper, times(1)).mapUserToHomeDataResponse(johnny);
    assertEquals(dashboardDataResponse, response);
  }
}
