package at.qe.skeleton.rest.frontend.mappers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import at.qe.skeleton.services.UserxService;
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
class DashboardDataMapperTest {
    @Autowired private UserxService userService;
    @Autowired private TemperaStationService temperaService;
    @Autowired private MeasurementService measurementService;
    @Autowired private TimeRecordService timeRecordService;
    @Autowired private DashboardDataMapper dashboardDataMapper;

    @BeforeEach
    void setUp() {
      }

    @AfterEach
    void tearDown() {
      }

    @Test
    @Transactional
    @WithMockUser(username = "johndoe", authorities = "EMPLOYEE")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:HomeDataMapperTest.sql")
    void mapUserToHomeDataResponse() {
        Userx johndoe = userService.loadUser("johndoe");
        DashboardDataResponse homeDataResponse = dashboardDataMapper.mapUserToHomeDataResponse(johndoe);
        assertEquals(8, homeDataResponse.colleagueStates().size(), "ColleagueStates size should be 8, for reference look at DashboardDataMapper.sql");
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

Optional<ColleagueStateDto> optionalColleague4= homeDataResponse.colleagueStates().stream().filter(c -> c.name().equals("manager")).findAny();
        assertTrue(optionalColleague4.isPresent(), "Colleague manager should be present in the list");
        List<String> managerGroups = optionalColleague4.get().groupOverlap();
        assertEquals(2, managerGroups.size(), "overlap should be two groups");
        assertTrue(managerGroups.contains("testGroup1"), "manager should be in group1");
        assertTrue(managerGroups.contains("testGroup2"), "manager should be in group2");
      }

}