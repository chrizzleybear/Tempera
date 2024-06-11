package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
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
  void getTimetableData() throws Exception{
    String username = "johndoe";

    GetTimetableDataResponse dataResponse = timetableDataService.getTimetableData(username);
    assertNotNull(dataResponse);
    List<TimetableEntryDto> timeTableEntries = dataResponse.tableEntries();
    List<SimpleGroupxProjectDto> simpleProjectDtos = dataResponse.availableProjects();
    assertNotNull(timeTableEntries);
    assertNotNull(simpleProjectDtos);

    // this TimeRecord has a null Ending time so it should not be converted to a TimetableEntryDto
    long id = -22;
    TimetableEntryDto entryNull =
        timeTableEntries.stream().filter(e -> e.id().equals(id)).findFirst().orElse(null);
    assertNull(entryNull, "TimeRecord with null as End should not be int the list");


    long id2 = -14;
    String startTimestamp = "2024-05-11T13:00:00";
    String endTimestamp = "2024-05-12T10:59:59";
    SimpleGroupxProjectDto simpleGxp =
        new SimpleGroupxProjectDto("2", "Research_Group", "-7", "Product Development");
    State state2 = State.OUT_OF_OFFICE;
    String description2 = null;
    TimetableEntryDto entryDto =
        timeTableEntries.stream().filter(e -> e.id().equals(id2)).findFirst().orElse(null);

    assertNotNull(entryDto);
    assertEquals(id2, entryDto.id());
    assertEquals(startTimestamp, entryDto.startTimestamp());
    assertEquals(endTimestamp, entryDto.endTimestamp());
    assertEquals(simpleGxp, entryDto.assignedGroupxProject());
    assertEquals(state2, entryDto.state());

    assertEquals(10, timeTableEntries.size(), "Johndoe should have 10 TimeTableEntries, since he has 10 closed internalRecords");
    assertEquals(6, dataResponse.availableProjects().size(), "Johndoe should have 6 available Projects, since he is assigned to 6 GroupxProjects");
    assertTrue(dataResponse.availableProjects().stream().anyMatch(p -> p.projectName().equals("Cost Reduction Initiative")), "Johndoe should have the Project 'Cost Reduction Initiative' available");
    assertFalse(dataResponse.availableProjects().stream().anyMatch(p -> p.projectName().equals("Efficiency")), "Johndoe should not have the Project 'Efficiency' available");
  }

  @Test
  @Sql(
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      scripts = "classpath:TimetableDataServiceTest.sql")
  @WithMockUser(username = "johndoe")
  void getTimeTableData1() throws Exception{
    String username = "johndoe";
    GetTimetableDataResponse dataResponse = timetableDataService.getTimetableData(username);
    assertNotNull(dataResponse);
    List<TimetableEntryDto> timeTableEntries = dataResponse.tableEntries();
    List<SimpleGroupxProjectDto> extendedProjectDtos2 = dataResponse.availableProjects();
    assertNotNull(timeTableEntries);
    assertNotNull(extendedProjectDtos2);

    long id2 = -15;
    String startTimestamp2 = "2024-05-12T11:00:00";
    String endTimestamp2 = "2024-05-12T13:59:59";
    SimpleGroupxProjectDto simpleGxp = null;
    State state2 = State.AVAILABLE;
    String description2 = null;

    TimetableEntryDto entry =
        timeTableEntries.stream().filter(e -> e.id().equals(id2)).findFirst().orElse(null);
    assertNotNull(entry);
    assertEquals(id2, entry.id());
    assertEquals(startTimestamp2, entry.startTimestamp());
    assertEquals(endTimestamp2, entry.endTimestamp());
    assertEquals(simpleGxp, entry.assignedGroupxProject());
    assertEquals(state2, entry.state());
    assertEquals(description2, entry.description());
  }

  @Test
  @Sql(
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      scripts = "classpath:TimetableDataServiceTest.sql")
  @WithMockUser(username = "johndoe")
  void getTimeTableData2() throws Exception{

    // Expected Data
    long id = -20;
    String startTimestamp = "2024-05-14T13:00:00";
    String endTimestamp = "2024-05-14T16:59:59";
    SimpleGroupxProjectDto simpleGxp =
        new SimpleGroupxProjectDto("2", "Research_Group", "-10", "Marketing Campaign Launch");
    State state = State.DEEPWORK;
    String entryDescription = null;
    String username = "johndoe";

    // Actual Call:
    GetTimetableDataResponse dataResponse = timetableDataService.getTimetableData(username);
    List<TimetableEntryDto> timeTableEntries = dataResponse.tableEntries();

    // Tests:
    TimetableEntryDto entry =
        timeTableEntries.stream().filter(e -> e.id().equals(id)).findFirst().orElse(null);
    assertNotNull(entry);
    assertEquals(id, entry.id());
    assertEquals(startTimestamp, entry.startTimestamp());
    assertEquals(endTimestamp, entry.endTimestamp());
    assertEquals(simpleGxp, entry.assignedGroupxProject());
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
