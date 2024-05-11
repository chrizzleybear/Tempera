package at.qe.skeleton.rest.frontend.mappers;

import at.qe.skeleton.model.Userx;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(4, homeDataResponse.colleagueStates().size(), "ColleagueStates size should be 4, for reference look at DashboardDataMapper.sql");
        assertEquals(20.0, homeDataResponse.temperature(), "Temperature of johndoe should be 20");
        assertEquals(50.0, homeDataResponse.humidity(), "Humidity of johndoe should be 50.0");
        assertEquals(1000.0, homeDataResponse.irradiance(), "Irradiance of johndoe should be 1000.0");
        assertEquals(100.0, homeDataResponse.nmvoc(), "NMVOC of johndoe should be 100.0");
      }

}