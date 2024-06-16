package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.AccumulatedTimeDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.AccumulatedTimeMapper;
import at.qe.skeleton.rest.frontend.payload.response.AccumulatedTimeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AccumulatedTimeControllerTest {
  AccumulatedTimeController accumulatedTimeController;
  @Mock AccumulatedTimeMapper accumulatedTimeMapper;

  static final String testUsername = "Alfred";
  List<AccumulatedTimeDto> accumulatedTimeDtos;
  List<SimpleProjectDto> simpleProjectDtos;
  List<SimpleGroupDto> simpleGroupDtos;
  AccumulatedTimeResponse testResponse;

  @BeforeEach
  void setUp() {
    accumulatedTimeController = new AccumulatedTimeController(accumulatedTimeMapper);
    accumulatedTimeDtos =
        List.of(
            new AccumulatedTimeDto(
                "project1", "group1",true, State.DEEPWORK, "15.05.2024T15:00", "15.05.2024T15:00"));
    simpleProjectDtos =
        List.of(new SimpleProjectDto("1", false, "project1", "this is project 1", "someGuy"));
    simpleGroupDtos = List.of(new SimpleGroupDto("1", false, "group1", "this is group 1", "someGirl"));
testResponse =
        new AccumulatedTimeResponse(accumulatedTimeDtos, simpleProjectDtos, simpleGroupDtos);
Mockito.reset(accumulatedTimeMapper);

  }

  @Test
  @WithMockUser(
      username = testUsername,
      authorities = {"MANAGER"})
  void getAccumulatedTimeDataAsManager() {
    // Mock the Authentication object
    Authentication auth = new TestingAuthenticationToken(testUsername, "password", "MANAGER");
    SecurityContextHolder.getContext().setAuthentication(auth);

    // mock the accumulatedTimeResponse:
    when(accumulatedTimeMapper.getManagerTimeData(testUsername)).thenReturn(testResponse);

    //actual call and assertions
    AccumulatedTimeResponse response = accumulatedTimeController.getAccumulatedTimeData().getBody();
    verify(accumulatedTimeMapper).getManagerTimeData(testUsername);
    assertEquals(200, accumulatedTimeController.getAccumulatedTimeData().getStatusCode().value());
    assertEquals(accumulatedTimeDtos.size(), response.accumulatedTimes().size() );
    assertEquals(simpleProjectDtos.size(), response.availableProjects().size());
    assertEquals(simpleGroupDtos.size(), response.availableGroups().size());
    assertEquals(accumulatedTimeDtos.get(0).groupId(), response.accumulatedTimes().get(0).groupId());
    assertEquals(accumulatedTimeDtos.get(0).projectId(), response.accumulatedTimes().get(0).projectId());
    assertEquals(accumulatedTimeDtos.get(0).state(), response.accumulatedTimes().get(0).state());
  }

  @Test
  @WithMockUser(
      username = testUsername,
      authorities = {"GROUPLEAD"})
  void getAccumulatedTimeDataAsGroupLead() {
    // Mock the Authentication object
    Authentication auth = new TestingAuthenticationToken(testUsername, "password", "GROUPLEAD");
    SecurityContextHolder.getContext().setAuthentication(auth);

    // mock the accumulatedTimeResponse:
    when(accumulatedTimeMapper.getGroupLeadTimeData(testUsername)).thenReturn(testResponse);

    //actual call and assertions
    AccumulatedTimeResponse response = accumulatedTimeController.getAccumulatedTimeData().getBody();
    verify(accumulatedTimeMapper).getGroupLeadTimeData(testUsername);
    assertEquals(200, accumulatedTimeController.getAccumulatedTimeData().getStatusCode().value());
    assertEquals(accumulatedTimeDtos.size(), response.accumulatedTimes().size() );
    assertEquals(simpleProjectDtos.size(), response.availableProjects().size());
    assertEquals(simpleGroupDtos.size(), response.availableGroups().size());
    assertEquals(accumulatedTimeDtos.get(0).groupId(), response.accumulatedTimes().get(0).groupId());
    assertEquals(accumulatedTimeDtos.get(0).projectId(), response.accumulatedTimes().get(0).projectId());
    assertEquals(accumulatedTimeDtos.get(0).state(), response.accumulatedTimes().get(0).state());
  }


}
