package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.mappers.DashboardDataMapper;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DashboardControllerTest {

  private DashboardController dashboardController;
  @Mock private DashboardDataMapper dashboardDataMapper;

  @BeforeEach
  void setUp() {
    dashboardController = new DashboardController(dashboardDataMapper);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void homeData() {
    Userx user1 = new Userx();
    user1.setUsername("johndoe");
    List<String> noGroups = new ArrayList<>();
    var colleagueStates =
        List.of(
            new ColleagueStateDto("Max Mustermann", "Raum 1", State.DEEPWORK, true, List.of("Gruppe 1")),
            new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE, true, List.of("Gruppe1","Gruppe 2")),
            new ColleagueStateDto("Cooler Typ", "Raum 1", State.MEETING, false, noGroups));
    DashboardDataResponse dashboardDataResponse =
        new DashboardDataResponse(
            1.0,
            20,
            300,
            400.2189,
            Visibility.HIDDEN,
            State.DEEPWORK,
            "10.05.2024T12:20:10",
            null,
            colleagueStates);

    when(dashboardDataMapper.mapUserToHomeDataResponse(user1)).thenReturn(dashboardDataResponse);

    ResponseEntity<DashboardDataResponse> returnValue = dashboardController.dashboardData(user1.getUsername());
    DashboardDataResponse response = returnValue.getBody();

    verify(dashboardDataMapper, times(1)).mapUserToHomeDataResponse(user1);
    assertEquals(dashboardDataResponse, response);
  }
}
