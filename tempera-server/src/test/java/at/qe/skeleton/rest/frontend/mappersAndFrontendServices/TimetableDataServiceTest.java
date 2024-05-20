package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;
import at.qe.skeleton.rest.frontend.payload.response.GetTimetableDataResponse;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
class TimetableDataServiceTest {

    private TimetableDataService timetableDataService;
    @Autowired
    private UserxService userxService;


    @BeforeEach
    void setUp() {
      }

    @AfterEach
    void tearDown() {
      }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:TimetableDataServiceTest.sql")
    @WithMockUser(username = "johndoe")
    void getTimetableData() {
        Userx user = userxService.loadUser("johndoe");
        int page1 = 0;
        int page2 = 10;
        int size = 5;

        GetTimetableDataResponse dataResponse = timetableDataService.getTimetableData(user, page1, size);
        assertNotNull(dataResponse);
        List<TimetableEntryDto> timeTableEntries1 = dataResponse.tableEntries();
        List<ProjectDto> projectDtos1 = dataResponse.availableProjects();
        assertNotNull(timeTableEntries1);
        assertNotNull(projectDtos1);
        assertEquals(size, timeTableEntries1.size());

        // INSERT INTO internal_record
        // (id, groupx_id, groupx_project_group_id, groupx_project_project_id, project_id, start, time_end, ext_rec_start, user_name)
        // (-22, null, -11, null, null, '2024-05-15 15:00:00', null, '2024-05-15 15:00:00', 'johndoe');
        //this is the data we expect, since we stored it in the db:
        long id = -2;
        String startTimestamp = "2024-05-10 09:30:00";
        String endTimestamp = "2024-05-11 09:29:59";
        ProjectDto projectDto = new ProjectDto("-7", "Product Development");
        State state = State.DEEPWORK;
        String description = null;

        TimetableEntryDto entry = new TimetableEntryDto(id, startTimestamp, endTimestamp, projectDto, state, description);
        assertTrue(timeTableEntries1.contains(entry));


        //todo: zweiten test mit null ende

      }

      //todo add test for sending two pages

    @Test
    void updateProject() {
      }

    @Test
    void updateProjectDescription() {
      }

    @Test
    void splitTimeRecord() {
      }
}