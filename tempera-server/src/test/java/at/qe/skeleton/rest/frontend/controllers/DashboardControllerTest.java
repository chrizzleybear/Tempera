package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.DashboardDataMapper;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDashboardDataRequest;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DashboardControllerTest {

  private DashboardController dashboardController;
  @Mock private DashboardDataMapper dashboardDataMapper;
  @Mock private UserxService userXService;

  @BeforeEach
  void setUp() {
    TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken("johndoe", null);
    SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
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
    var projects =
        List.of(
            new SimpleProjectDto("project1", "Projekt 1", "Beschreibung 1", "manager1"),
            new SimpleProjectDto("project2", "Projekt 2", "Beschreibung 2", "manager2"),
            new SimpleProjectDto("project3", "Projekt 3", "Beschreibung 3", "manager3"));
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


    @Test
  void updateDashboardData() throws CouldNotFindEntityException {
    Userx userx = new Userx();
    MessageResponse messageResponse = new MessageResponse("Dashboard data updated successfully!");
    when(userXService.loadUser("johndoe")).thenReturn(userx);
    when(dashboardDataMapper.updateUserVisibilityAndTimeStampProject(any(), any())).thenReturn(messageResponse);

    ResponseEntity<MessageResponse> response =
        dashboardController.updateDashboardData(
            new UpdateDashboardDataRequest(
                Visibility.HIDDEN, new SimpleProjectDto("-1", "Projekt 1", "blabla", "johnathan hingeforth mc cringleberry")));
    assertEquals(messageResponse, response.getBody());
    verify(userXService, times(1)).loadUser("johndoe");
    verify(dashboardDataMapper, times(1)).updateUserVisibilityAndTimeStampProject(any(), any());
  }

}
