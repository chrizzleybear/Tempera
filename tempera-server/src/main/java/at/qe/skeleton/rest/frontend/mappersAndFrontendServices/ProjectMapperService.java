package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.services.GroupService;
import at.qe.skeleton.services.ProjectService;
import at.qe.skeleton.services.UserxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ProjectMapperService {
    private ProjectService projectService;
    private GroupService groupService;
    private UserxService userService;
    private GroupMapperService groupMapperService;


    public ProjectMapperService(ProjectService projectService, GroupService groupService, UserxService userService, GroupMapperService groupMapperService) {
        this.projectService = projectService;
        this.groupService = groupService;
        this.userService = userService;
        this.groupMapperService = groupMapperService;
    }

    @Transactional
    public List<ProjectDetailsDto> getAllDetailedProjects() {
        List<Project> projects = projectService.findAllProjects();
        return projects.stream().map(this::detailedProjectDtoMapper).collect(toList());
    }

  public ExtendedProjectDto loadExtendedProjectDto(Long projectId)
      throws CouldNotFindEntityException {
    Project project =
        projectService
            .getProjectById(projectId)
            .orElseThrow(() -> new CouldNotFindEntityException("Project not found: " + projectId));
    SimpleProjectDto simpleProjectDto = projectDtoMapper(project);
    SimpleUserDto managerDetails = userDtoMapper(project.getManager());

    List<GroupxProject> groupxProjects =
        projectService.findAllGroupxProjectsByProjectId(projectId);
    List<SimpleGroupDto> connectedGroups =
        groupxProjects.stream().map(groupMapperService::groupDtoMapper).toList();

    List<SimpleUserDto> contributors =
        projectService.findAllContributorsByProjectId(projectId);

    return new ExtendedProjectDto(managerDetails, simpleProjectDto, connectedGroups, contributors);
    }

    public ExtendedGroupDto loadExtendedGroupDto(Long groupId)
      throws CouldNotFindEntityException {
        Groupx group =
        groupService
            .getGroup(groupId);
        SimpleGroupDto simpleGroupDto = groupMapperService.groupDtoMapper(group);
        List<GroupxProject> groupxProjects = projectService.getGroupxProjectsByGroupId(groupId);
        Set<GroupxProjectDto> groupxProjectsDto = groupxProjects.stream().map(this::groupxProjectDtoMapper).collect(Collectors.toSet());
        List<Userx> groupMembers = groupService.getMembers(groupId);
        Set<SimpleUserDto> groupMembersDto = groupMembers.stream().map(this::userDtoMapper).collect(Collectors.toSet());
        return new ExtendedGroupDto(simpleGroupDto, groupxProjectsDto, groupMembersDto);
    }

    public List<SimpleGroupDto> getAllSimpleGroups(String projectId){
        List<GroupxProject> groupxProjects = projectService.findAllGroupxProjectsByProjectId(Long.valueOf(projectId));
        return groupxProjects.stream().map(groupMapperService::groupDtoMapper).collect(toList());
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


  private SimpleProjectDto projectDtoMapper(Project project) {
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
                groupMapperService.groupDtoMapper(groupxProject),
                projectDtoMapper(project),
                managerDetails,
                contributors
        );
    }

    public SimpleProjectDto updateProject(SimpleProjectDto projectDto) {
        Project updatedProject =
                projectService.updateProject(
                        Long.valueOf(projectDto.projectId()),
                        projectDto.name(),
                        projectDto.description(),
                        projectDto.manager());
        return projectDtoMapper(updatedProject);
    }

    public SimpleProjectDto createProject(SimpleProjectDto projectDto) {
        Project createdProject =
                projectService.createProject(
                        projectDto.name(),
                        projectDto.description(),
                        projectDto.manager());
        return projectDtoMapper(createdProject);
    }




    private ProjectDetailsDto detailedProjectDtoMapper(Project project) {
        String projectId = project.getId().toString();
        String projectName = project.getName();
        String projectDescription = project.getDescription();
        SimpleUserDto projectManagerDto = userDtoMapper(project.getManager());
        return new ProjectDetailsDto(projectId, projectName, projectDescription, projectManagerDto);
    }

    private SimpleUserDto userDtoMapper(Userx userx) {
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
