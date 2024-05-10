package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.mappers.HomeDataMapper;
import at.qe.skeleton.rest.frontend.payload.response.HomeDataResponse;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class HomeControllerTest {

  private HomeController homeController;
  @Mock private HomeDataMapper homeDataMapper;

  @BeforeEach
  void setUp() {
    homeController = new HomeController(homeDataMapper);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void homeData() {
    Userx user1 = new Userx();
    var colleagueStates =
        List.of(
            new ColleagueStateDto("Max Mustermann", "Raum 1", State.DEEPWORK),
            new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE),
            new ColleagueStateDto("Cooler Typ", "Raum 1", State.MEETING));
    HomeDataResponse homeDataResponse =
        new HomeDataResponse(
            1.0,
            20,
            300,
            400.2189,
            Visibility.HIDDEN,
            State.DEEPWORK,
            "10.05.2024T12:20:10",
            null,
            colleagueStates);

    when(homeDataMapper.mapUserToHomeDataResponse(user1)).thenReturn(homeDataResponse);

    ResponseEntity<HomeDataResponse> returnValue = homeController.homeData(user1);
    HomeDataResponse response = returnValue.getBody();

    verify(homeDataMapper, times(1)).mapUserToHomeDataResponse(user1);
    assertEquals(homeDataResponse, response);
  }
}
