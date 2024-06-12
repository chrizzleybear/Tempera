package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDashboardDataRequest;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@WebAppConfiguration
class DashboardDataServiceTest {
    @Autowired
    private UserxService userService;
    @Autowired private TemperaStationService temperaService;
    @Autowired private MeasurementService measurementService;
    @Autowired private TimeRecordService timeRecordService;
    @Autowired private DashboardDataService dashboardDataService;
    @Autowired private ProjectService projectService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Transactional
    @WithMockUser(username = "johndoe", authorities = "EMPLOYEE")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:DashboardDataMapper.sql")
    void mapUserToHomeDataResponse() {
        String johndoe = "johndoe";
        DashboardDataResponse homeDataResponse = dashboardDataService.mapUserToHomeDataResponse(johndoe);
        assertEquals(8, homeDataResponse.colleagueStates().size(), "ColleagueStates size should be 8, for reference look at DashboardDataService.sql");
        assertEquals(20.0, homeDataResponse.temperature(), "Temperature of johndoe should be 20");
        assertEquals(50.0, homeDataResponse.humidity(), "Humidity of johndoe should be 50.0");
        assertEquals(1000.0, homeDataResponse.irradiance(), "Irradiance of johndoe should be 1000.0");
        assertEquals(100.0, homeDataResponse.nmvoc(), "NMVOC of johndoe should be 100.0");
        Optional<ColleagueStateDto> optionalColleague= homeDataResponse.colleagueStates().stream().filter(c -> c.name().equals("alicebrown")).findAny();
        assertTrue(optionalColleague.isPresent(), "Colleague alicebrown should be present in the list");
        boolean aliceIsVisible = optionalColleague.get().isVisible();
        assertFalse(aliceIsVisible, "alicebrown should not be visible, since her Visibility is set to HIDDEN.");

        Optional<ColleagueStateDto> optionalColleague2= homeDataResponse.colleagueStates().stream().filter(c -> c.name().equals("chriswilliams")).findAny();
        assertTrue(optionalColleague2.isPresent(), "Colleague chriswilliams should be present in the list");
        boolean chrisIsVisible = optionalColleague2.get().isVisible();
        assertTrue(chrisIsVisible, "chriswilliams should be visible, since his Visibility is set to Private, but he is in one of john does Groups.");

        Optional<ColleagueStateDto> optionalColleague3= homeDataResponse.colleagueStates().stream().filter(c -> c.name().equals("tonystark")).findAny();
        assertTrue(optionalColleague3.isPresent(), "Colleague tonystark should be present in the list");
        boolean tonyIsVisible = optionalColleague3.get().isVisible();
        assertFalse(tonyIsVisible, "tonystark should not be visible, since his Visibility is set to private and he is not in one of john does groups.");

        Optional<ColleagueStateDto> optionalColleague4= homeDataResponse.colleagueStates().stream().filter(c -> c.name().equals("admin")).findAny();
        assertTrue(optionalColleague4.isPresent(), "Colleague admin should be present in the list");
        List<String> sharedGroupsOfManagerWithJohnDoe = optionalColleague4.get().groupOverlap();
        assertEquals(2, sharedGroupsOfManagerWithJohnDoe.size(), "overlap should be two groups");
        assertTrue(sharedGroupsOfManagerWithJohnDoe.contains("testGroup1"), "admin should be in group1");
        assertTrue(sharedGroupsOfManagerWithJohnDoe.contains("testGroup2"), "admin should be in group2");
    }

  @Test
  @Transactional
  @WithMockUser(username = "johndoe", authorities = "EMPLOYEE")
  @Sql(
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      scripts = "classpath:DashboardDataMapper.sql")
  void mapUserToDataResponseGroupxProjects() {
    String johndoe = "johndoe";
    DashboardDataResponse homeDataResponse = dashboardDataService.mapUserToHomeDataResponse(johndoe);

      assertEquals(6, homeDataResponse.availableProjects().size(), "Johndoe should have 6 available Projects, since he is assigned to 6 GroupxProjects");
      assertTrue(homeDataResponse.availableProjects().stream().anyMatch(p -> p.projectName().equals("Cost Reduction Initiative")), "Johndoe should have the Project 'Cost Reduction Initiative' available");
      assertFalse(homeDataResponse.availableProjects().stream().anyMatch(p -> p.projectName().equals("Efficiency")), "Johndoe should not have the Project 'Efficiency' available");
  }

  @Test
  @Transactional
  @Sql(
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      scripts = "classpath:DashboardDataMapper.sql")
  @WithMockUser(username = "tonystark", authorities = "EMPLOYEE")
  void mapUserToHomeDataResponseNullValues() {
        String user = "tonystark";
        DashboardDataResponse homeDataResponse = dashboardDataService.mapUserToHomeDataResponse(user);
        assertEquals(8, homeDataResponse.colleagueStates().size(), "ColleagueStates size should be 8, for reference look at DashboardDataService.sql");
        assertNull(homeDataResponse.temperature(), "Temperature of johndoe should be 20");
        assertNull(homeDataResponse.humidity(), "Humidity of johndoe should be 50.0");
        assertNull( homeDataResponse.irradiance(), "Irradiance of johndoe should be 1000.0");
        assertNull(homeDataResponse.nmvoc(), "NMVOC of johndoe should be 100.0");


        Optional<ColleagueStateDto> optionalColleague2= homeDataResponse.colleagueStates().stream().filter(c -> c.name().equals("chriswilliams")).findAny();
        assertTrue(optionalColleague2.isPresent(), "Colleague chriswilliams should be present in the list");
        boolean chrisIsVisible = optionalColleague2.get().isVisible();
        assertTrue(chrisIsVisible, "chriswilliams should be visible, since his Visibility is set to Private, but he is in one of john does Groups.");

        Optional<ColleagueStateDto> optionalColleague3= homeDataResponse.colleagueStates().stream().filter(c -> c.name().equals("tonystark")).findAny();
        assertFalse(optionalColleague3.isPresent(), "Colleague tonystark should not be present in the list");


        Optional<ColleagueStateDto> optionalColleague4= homeDataResponse.colleagueStates().stream().filter(c -> c.name().equals("peterparker")).findAny();
        assertTrue(optionalColleague4.isPresent(), "Colleague peterparker should be present in the list");
        List<String> managerGroups = optionalColleague4.get().groupOverlap();
        assertEquals(2, managerGroups.size(), "overlap should be two groups");
        assertTrue(managerGroups.contains("outsiderGroup"), "peterparker should be in outsiderGroup");
        assertTrue(managerGroups.contains("outsiderGroup2"), "peterparker should be in outsiderGroup2");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:DashboardDataMapper.sql")
    @WithMockUser(username = "johndoe", authorities = "EMPLOYEE")
    @Transactional
    void mapUserToHomeDataResponseNoMeasurements() throws CouldNotFindEntityException {
        Userx johndoe = userService.loadUser("johndoe");
        assertEquals(Visibility.PUBLIC, johndoe.getStateVisibility(), "Visibility of johndoe should be PUBLIC before the update");
        GroupxProject projectBefore = timeRecordService.findLatestInternalRecordByUser(johndoe).get().getGroupxProject();
        assertNull(projectBefore, "Project of johndoe should be null before the update");

        // choose another visibility and set a gxp that John Doe is a member of to the current timestamp
        Visibility visibilityUpdate = Visibility.HIDDEN;
    SimpleGroupxProjectDto gxpUpdate =
        new SimpleGroupxProjectDto(
                "-2",
            "testGroup2",
            "-12",
            "Infrastructure Upgrade");
        UpdateDashboardDataRequest request = new UpdateDashboardDataRequest(visibilityUpdate, gxpUpdate);
        dashboardDataService.updateUserVisibilityAndTimeStampProject(request, johndoe);
        assertEquals(Visibility.HIDDEN, johndoe.getStateVisibility(), "Visibility of johndoe should be HIDDEN after the update");
        GroupxProject gxpAfter = timeRecordService.findLatestInternalRecordByUser(johndoe).get().getGroupxProject();
        assertEquals("Infrastructure Upgrade", gxpAfter.getProject().getName(), "Project of johndoe should be Infrastructure Upgrade after the update");
        assertEquals("testGroup2", gxpAfter.getGroup().getName(), "Group of johndoe should be testGroup2 after the update");

    }


}