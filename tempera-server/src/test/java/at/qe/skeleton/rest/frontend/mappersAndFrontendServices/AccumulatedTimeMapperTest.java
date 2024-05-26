package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleUserDto;
import at.qe.skeleton.rest.frontend.payload.response.AccumulatedTimeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@WebAppConfiguration
class AccumulatedTimeMapperTest {
    @Autowired AccumulatedTimeMapper accumulatedTimeMapper;


    @Test
    @Sql(scripts = {"classpath:AccumulatedTimeMapper.sql"})
    @WithMockUser(
        username = "MariaTheresa",
        authorities = {"MANAGER"})
    void testGetManagerTimeData() {

        AccumulatedTimeResponse response = accumulatedTimeMapper.getManagerTimeData("MariaTheresa");
        assertEquals(6, response.accumulatedTimes().size());
        assertEquals(1, response.accumulatedTimes().stream().filter(timerecord -> timerecord.groupId().equals("-1") && timerecord.projectId().equals("-2")).toList().size(), "There should be exactly one timerecord with groupId -1 and projectId -2");
        assertEquals(1, response.accumulatedTimes().stream().filter(timerecord -> timerecord.groupId().equals("-3") && timerecord.projectId().equals("-6")).toList().size(), "There should be exactly one timerecord with groupId -3 and projectId -6");




        //projekte -1 bis -6 sollten zu MariaTheresa gehören
        assertEquals(6, response.availableProjects().size());
        SimpleProjectDto expectedProjectDto = new SimpleProjectDto("-1", "Serious Business", "This project beuts you aus", "MariaTheresa");
        assertTrue(response.availableProjects().contains(expectedProjectDto), "The project with id -1 should be in the list of available projects");
    // gruppen -1 bis -3 sollten diesen Projekten zugeordnet sein und damit auch zu MariaTheresa
    // gehören
    assertEquals(3, response.availableGroups().size());
        SimpleGroupDto expectedGroupDto = new SimpleGroupDto("-1","Research Team", "this is just for testing", "peterparker");
    assertTrue(response.availableGroups().contains(expectedGroupDto), "The group with id -1 should be in the list of available groups");
        SimpleGroupDto UnexpectedGroupDto = new SimpleGroupDto("-4","Expert Team", "this is also just for testing", "tonystark");
    assertFalse(response.availableGroups().contains(UnexpectedGroupDto), "The group with id -4 should not be in the list of available groups");
    }

    @Test
    @WithMockUser(
        username = "peterparker",
        authorities = {"GROUPLEAD"})
    void testGetGroupLeadTimeData() {
        //todo: write test for getGroupLeadTimeData

    }
}
