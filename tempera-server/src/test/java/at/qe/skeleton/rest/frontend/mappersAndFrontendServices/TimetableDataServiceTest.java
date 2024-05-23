package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
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


    // this will fetch the first page containing of the last 5 entries (id -22 to -18)
    GetTimetableDataResponse dataResponse =
        timetableDataService.getTimetableData(user);
    assertNotNull(dataResponse);
    List<TimetableEntryDto> timeTableEntries1 = dataResponse.tableEntries();
    List<SimpleProjectDto> simpleProjectDtos1 = dataResponse.availableProjects();
    assertNotNull(timeTableEntries1);
    assertNotNull(simpleProjectDtos1);



    // this TimeRecord has a null Ending time so it should not be converted to a TimetableEntryDto
    long id = -22;
    TimetableEntryDto entry =
        timeTableEntries1.stream().filter(e -> e.id().equals(id)).findFirst().orElse(null);
    assertNull(entry, "TimeRecord with null as End should not be int the list");

    // here comes a second dataresponse2 as we fetch the second page containing of the next 5 entries (id -17 to -13)
    GetTimetableDataResponse dataResponse2 =
        timetableDataService.getTimetableData(user);
    assertNotNull(dataResponse2);
    List<TimetableEntryDto> timeTableEntries2 = dataResponse2.tableEntries();
    List<SimpleProjectDto> extendedProjectDtos2 = dataResponse2.availableProjects();
    assertNotNull(timeTableEntries2);
    assertNotNull(extendedProjectDtos2);

    long id2 = -15;
    String startTimestamp2 = "2024-05-12T11:00";
    String endTimestamp2 = "2024-05-12T13:59:59";
    SimpleProjectDto simpleProjectDto2 = null;
    State state2 = State.AVAILABLE;
    String description2 = null;

    TimetableEntryDto entry2 =
        timeTableEntries2.stream().filter(e -> e.id().equals(id2)).findFirst().orElse(null);
    assertNotNull(entry2);
    assertEquals(id2, entry2.id());
    assertEquals(startTimestamp2, entry2.startTimestamp());
    assertEquals(endTimestamp2, entry2.endTimestamp());
    assertEquals(simpleProjectDto2, entry2.assignedProject());
    assertEquals(state2, entry2.state());
    assertEquals(description2, entry2.description());
  }

  @Test
  @Sql(
          executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
          scripts = "classpath:TimetableDataServiceTest.sql")
  @WithMockUser(username = "johndoe")
    void getTimeTableData2() {

      //INSERT INTO internal_record (id,        group_id,      project_id,                   start,                          time_end,                              ext_rec_start,                  user_name)
     //                                                 (-20,                2,              -10,             '2024-05-14 13:00:00',      '2024-05-14 16:59:59',       '2024-05-14 13:00:00',        'johndoe'),

      //Expected Data
      long id = -20;
      String startTimestamp = "2024-05-14T13:00";
      String endTimestamp = "2024-05-14T16:59:59";
      SimpleProjectDto simpleProjectDto =
              new SimpleProjectDto("-10", "Marketing Campaign Launch", "This project involves planning and executing a new marketing campaign to attract customers.", "admin");
      State state = State.DEEPWORK;
      String entryDescription = null;

      // get our user:
      Userx user = userxService.loadUser("johndoe");

      //Actual Call:
      GetTimetableDataResponse dataResponse =
              timetableDataService.getTimetableData(user);

      List<TimetableEntryDto> timeTableEntries = dataResponse.tableEntries();


      //Tests:
      TimetableEntryDto entry =
              timeTableEntries.stream().filter(e -> e.id().equals(id)).findFirst().orElse(null);
      assertNotNull(entry);
      assertEquals(id, entry.id());
      assertEquals(startTimestamp, entry.startTimestamp());
      assertEquals(endTimestamp, entry.endTimestamp());
      assertEquals(simpleProjectDto, entry.assignedProject());
      assertEquals(state, entry.state());
      assertEquals(entryDescription, entry.description());
  }

  @Test
  void updateProject() {}

  @Test
  void updateProjectDescription() {}

  @Test
  void splitTimeRecord() {}
}
