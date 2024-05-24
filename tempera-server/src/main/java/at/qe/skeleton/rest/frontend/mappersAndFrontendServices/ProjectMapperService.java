package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.services.GroupService;
import at.qe.skeleton.services.ProjectService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ProjectMapperService {
    private ProjectService projectService;
    private GroupService groupService;


    public ProjectMapperService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<SimpleProjectDto> getAllSimpleProjects() {
        List<Project> projects = projectService.findAllProjects();
        return projects.stream().map(this::projectDtoMapper).collect(toList());
    }

  public ExtendedProjectDto loadExtendedProjectDto(Long projectId)
      throws CouldNotFindEntityException {
    Project project =
        projectService
            .getProjectById(projectId)
            .orElseThrow(() -> new CouldNotFindEntityException("Project not found: " + projectId));
    SimpleProjectDto simpleProjectDto = projectDtoMapper(project);

    List<GroupxProject> groupxProjects =
        projectService.findAllGroupxProjectsByProjectId(projectId);
    List<SimpleGroupDto> connectedGroups =
        groupxProjects.stream().map(this::groupDtoMapper).toList();

    List<SimpleUserDto> contributors =
        projectService.findAllContributorsByProjectId(projectId);

    return new ExtendedProjectDto(simpleProjectDto, connectedGroups, contributors);
    }

    public ExtendedGroupDto loadExtendedGroupDto(Long groupId)
      throws CouldNotFindEntityException {
        Groupx group =
        groupService
            .getGroup(groupId);
        SimpleGroupDto simpleGroupDto = groupDtoMapper(group);
        List<GroupxProject> groupxProjects = projectService.getGroupxProjectsByGroupId(groupId);
        Set<GroupxProjectDto> groupxProjectsDto = groupxProjects.stream().map(this::groupxProjectDtoMapper).collect(Collectors.toSet());
        List<Userx> groupMembers = groupService.getMembers(groupId);
        Set<SimpleUserDto> groupMembersDto = groupMembers.stream().map(this::userDtoMapper).collect(Collectors.toSet());
        return new ExtendedGroupDto(simpleGroupDto, groupxProjectsDto, groupMembersDto);
    }

    public List<SimpleGroupDto> getAllSimpleGroups(String projectId){
        List<GroupxProject> groupxProjects = projectService.findAllGroupxProjectsByProjectId(Long.valueOf(projectId));
        return groupxProjects.stream().map(this::groupDtoMapper).collect(toList());
    }


    public ExtendedProjectDto addGroupToProject(GroupAssignmentDto groupAssignmentDto) throws IOException {
        GroupxProject groupxProject = projectService.createGroupxProject(groupAssignmentDto.projectId(), groupAssignmentDto.groupId());
        projectService.saveGroupxProject(groupxProject);
        return loadExtendedProjectDto(groupAssignmentDto.projectId());
    }

    public List<SimpleProjectDto> getSimpleProjectsByGroupId(Long groupId) {
        List<Project> projects = projectService.getProjectsByGroupId(groupId);
        return projects.stream().map(this::projectDtoMapper).collect(toList());
    }


  public SimpleProjectDto projectDtoMapper(Project project) {
        return new SimpleProjectDto(
                project.getId().toString(),
                project.getName(),
                project.getDescription(),
                project.getManager().getUsername()
        );
    }

    private GroupxProjectDto groupxProjectDtoMapper(GroupxProject groupxProject) {
        Project project = groupxProject.getProject();
        SimpleUserDto managerDetails = userDtoMapper(project.getManager());
        List<SimpleUserDto> contributors = groupxProject.getContributors().stream().map(this::userDtoMapper).toList();
        return new GroupxProjectDto(
                groupDtoMapper(groupxProject),
                projectDtoMapper(project),
                managerDetails,
                contributors
        );
    }

    private SimpleGroupDto groupDtoMapper(GroupxProject groupxProject) {
        return groupDtoMapper(groupxProject.getGroup());
    }

    private SimpleGroupDto groupDtoMapper(Groupx groupx) {
        return new SimpleGroupDto(
                groupx.getId().toString(),
                groupx.getName(),
                groupx.getDescription(),
                groupx.getGroupLead().getUsername()
        );
    }

    public SimpleUserDto userDtoMapper(Userx userx) {
        return new SimpleUserDto(
                userx.getUsername(),
                userx.getFirstName(),
                userx.getLastName(),
                userx.getEmail()
        );
    }

    public GroupDetailsDto groupDetailsDto(Groupx groupx) {
        return new GroupDetailsDto(
                groupx.getId().toString(),
                groupx.getName(),
                groupx.getDescription(),
                userDtoMapper(groupx.getGroupLead())
        );
    }
}
