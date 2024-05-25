package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.dtos.GroupxProjectStateTimeDto;
import at.qe.skeleton.rest.frontend.dtos.AccumulatedTimeDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.payload.response.AccumulatedTimeResponse;
import at.qe.skeleton.services.GroupService;
import at.qe.skeleton.services.ProjectService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AccumulatedTimeMapper {

  ProjectService projectService;
  ProjectMapperService projectMapper;

  GroupMapperService groupMapperService;
  GroupService groupService;

  public AccumulatedTimeResponse getManagerTimeData(String username) {
      // all GroupxProjects for this manager -> all GroupxProjects that feature a project where this manager is assigned
    List<GroupxProjectStateTimeDto> groupxProjectStateTimeDtos =
        projectService.gxpStateTimeDtosByManager(username);
    List<AccumulatedTimeDto> accumulatedTimeDtos =
        groupxProjectStateTimeDtos.stream().map(this::mapToAccumulatedTimeDto).toList();
    // all available Projects for this manager -> all Projects where this manager is assigned
    List<Project> projects = projectService.getProjectsByManager(username);
    List<SimpleProjectDto> simpleProjectDtos =
        projects.stream().map(projectMapper::mapToSimpleProjectDto).toList();
    // all available Groups for this manager -> all groups that are assigned to the projects where
    // this manager is assigned
    List<SimpleGroupDto> simpleGroupDtos =
        groupService.getGroupsByManager(username).stream()
            .map(groupMapperService::mapToSimpleGroupDto)
            .toList();

    return new AccumulatedTimeResponse(accumulatedTimeDtos, simpleProjectDtos, simpleGroupDtos);
  }

  public AccumulatedTimeResponse getGroupLeadTimeData(String username) {
    // all available GroupxProjects for this group lead -> all GroupxProjects that have a group that
    // this group lead is assigned
    List<GroupxProjectStateTimeDto> groupxProjectStateTimeDtos =
        projectService.gxpStateTimeDtosByGroupLead(username);
    List<AccumulatedTimeDto> accumulatedTimeDtos =
        groupxProjectStateTimeDtos.stream().map(this::mapToAccumulatedTimeDto).toList();
    // all available Projects for this group lead -> all Projects that are assigned to groups that
    // this group lead is assigned to
    List<Project> projects = projectService.getProjectsByGroupLead(username);
    List<SimpleProjectDto> simpleProjectDtos =
        projects.stream().map(projectMapper::mapToSimpleProjectDto).toList();
    // all available Groups for this group lead -> all groups that are assigned this grouplead
    List<Groupx> groups = groupService.getGroupsByGroupLead(username);
    List<SimpleGroupDto> simpleGroupDtos =
        groups.stream().map(groupMapperService::mapToSimpleGroupDto).toList();

    return new AccumulatedTimeResponse(accumulatedTimeDtos, simpleProjectDtos, simpleGroupDtos);
  }

  private AccumulatedTimeDto mapToAccumulatedTimeDto(
      GroupxProjectStateTimeDto groupxProjectStateTimeDto) {
    return new AccumulatedTimeDto(
        groupxProjectStateTimeDto.projectId().toString(),
        groupxProjectStateTimeDto.groupId().toString(),
        groupxProjectStateTimeDto.state(),
        groupxProjectStateTimeDto.start().format(DateTimeFormatter.ISO_DATE_TIME),
        groupxProjectStateTimeDto.end().format(DateTimeFormatter.ISO_DATE_TIME));
  }
}
