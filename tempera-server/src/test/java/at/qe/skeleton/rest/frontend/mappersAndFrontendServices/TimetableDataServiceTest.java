package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
class TimetableDataServiceTest {

  @Autowired private TimetableDataService timetableDataService;
  @Autowired private UserxService userxService;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  @Sql(
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      scripts = "classpath:TimetableDataServiceTest.sql")
  @WithMockUser(username = "johndoe")
  void getTimetableData() {
    Userx user = userxService.loadUser("johndoe");
    int page1 = 0;
    int page2 = 1;
    int size = 5;

    // this will fetch the first page containing of the last 5 entries (id -22 to -18)
    GetTimetableDataResponse dataResponse =
        timetableDataService.getTimetableData(user, page1, size);
    assertNotNull(dataResponse);
    List<TimetableEntryDto> timeTableEntries1 = dataResponse.tableEntries();
    List<ExtendedProjectDto> extendedProjectDtos1 = dataResponse.availableProjects();
    assertNotNull(timeTableEntries1);
    assertNotNull(extendedProjectDtos1);
    assertEquals(size, timeTableEntries1.size());

    // INSERT INTO internal_record
    // (id, groupx_id, groupx_project_group_id, groupx_project_project_id, project_id, start,
    // time_end, ext_rec_start, user_name)
    // (-22, null, -11, null, null, '2024-05-15 15:00:00', null, '2024-05-15 15:00:00', 'johndoe');
    // this is the data we expect, since we stored it in the db:
    long id = -22;
    String startTimestamp = "2024-05-15T15:00";
    String endTimestamp = null;
    ExtendedProjectDto extendedProjectDto = new ExtendedProjectDto("-11", "Training and Development Program");
    State state = State.MEETING;
    String description = null;

    TimetableEntryDto entry =
        timeTableEntries1.stream().filter(e -> e.id().equals(id)).findFirst().orElse(null);
    assertNotNull(entry);
    assertEquals(id, entry.id());
    assertEquals(startTimestamp, entry.startTimestamp());
    assertEquals(endTimestamp, entry.endTimestamp());
    assertEquals(extendedProjectDto, entry.assignedProject());
    assertEquals(state, entry.state());
    assertEquals(description, entry.description());


    // here comes a second dataresponse2 as we fetch the second page containing of the next 5 entries (id -17 to -13)
    GetTimetableDataResponse dataResponse2 =
        timetableDataService.getTimetableData(user, page2, size);
    assertNotNull(dataResponse2);
    List<TimetableEntryDto> timeTableEntries2 = dataResponse2.tableEntries();
    List<ExtendedProjectDto> extendedProjectDtos2 = dataResponse2.availableProjects();
    assertNotNull(timeTableEntries2);
    assertNotNull(extendedProjectDtos2);
    assertEquals(size, timeTableEntries2.size());

    long id2 = -15;
    String startTimestamp2 = "2024-05-12T11:00";
    String endTimestamp2 = "2024-05-12T13:59:59";
    ExtendedProjectDto extendedProjectDto2 = new ExtendedProjectDto(null, null); // projectId and GroupId are null on this one
    State state2 = State.AVAILABLE;
    String description2 = null;

    TimetableEntryDto entry2 =
        timeTableEntries2.stream().filter(e -> e.id().equals(id2)).findFirst().orElse(null);
    assertNotNull(entry2);
    assertEquals(id2, entry2.id());
    assertEquals(startTimestamp2, entry2.startTimestamp());
    assertEquals(endTimestamp2, entry2.endTimestamp());
    assertEquals(extendedProjectDto2, entry2.assignedProject());
    assertEquals(state2, entry2.state());
    assertEquals(description2, entry2.description());
  }

  @Test
  void updateProject() {}

  @Test
  void updateProjectDescription() {}

  @Test
  void splitTimeRecord() {}
}
