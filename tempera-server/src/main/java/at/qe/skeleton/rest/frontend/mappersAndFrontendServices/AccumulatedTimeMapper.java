package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.dtos.GroupxProjectStateTimeDbDto;
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

  public AccumulatedTimeMapper(
      ProjectService projectService,
      ProjectMapperService projectMapper,
      GroupMapperService groupMapperService,
      GroupService groupService) {
    this.projectService = projectService;
    this.projectMapper = projectMapper;
    this.groupMapperService = groupMapperService;
    this.groupService = groupService;
  }

  public AccumulatedTimeResponse getManagerTimeData(String username) {
      // all GroupxProjects for this manager -> all GroupxProjects that feature a project where this manager is assigned
    // AND where the end is not null (meaning not currently still running
    List<GroupxProjectStateTimeDbDto> groupxProjectStateTimeDbDtos =
        projectService.gxpStateTimeDtosByManager(username).stream().filter(dto -> dto.end() != null).toList();
    List<AccumulatedTimeDto> accumulatedTimeDtos =
        groupxProjectStateTimeDbDtos.stream().map(this::mapToAccumulatedTimeDto).toList();
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
    // this group lead is assigned AND where the end is not null (meaning not currently still running)
    List<GroupxProjectStateTimeDbDto> groupxProjectStateTimeDbDtos =
        projectService.gxpStateTimeDtosByGroupLead(username).stream().filter(dto -> dto.end() != null).toList();
    List<AccumulatedTimeDto> accumulatedTimeDtos =
        groupxProjectStateTimeDbDtos.stream().map(this::mapToAccumulatedTimeDto).toList();
    // all available Projects for this group lead -> all Projects that are assigned to groups that
    // this group lead is assigned to
    List<Project> projects = projectService.getProjectsByGroupLead(username);
    List<SimpleProjectDto> simpleProjectDtos =
        projects.stream().map(projectMapper::mapToSimpleProjectDto).toList();
    // all available Groups for this group lead (active and inactive) -> all groups that are assigned to this grouplead
    List<Groupx> groups = groupService.getGroupsByGroupLead(username);
    List<SimpleGroupDto> simpleGroupDtos =
        groups.stream().map(groupMapperService::mapToSimpleGroupDto).toList();

    return new AccumulatedTimeResponse(accumulatedTimeDtos, simpleProjectDtos, simpleGroupDtos);
  }

  private AccumulatedTimeDto mapToAccumulatedTimeDto(
      GroupxProjectStateTimeDbDto groupxProjectStateTimeDbDto) {
    return new AccumulatedTimeDto(
        groupxProjectStateTimeDbDto.projectId().toString(),
        groupxProjectStateTimeDbDto.groupId().toString(),
        groupxProjectStateTimeDbDto.state(),
        groupxProjectStateTimeDbDto.start().format(DateTimeFormatter.ISO_DATE_TIME),
        groupxProjectStateTimeDbDto.end().format(DateTimeFormatter.ISO_DATE_TIME));
  }
}
