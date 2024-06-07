package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.dtos.SimpleProjectDbDto;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.services.GroupService;
import at.qe.skeleton.services.ProjectService;
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
    private GroupMapperService groupMapperService;
    private UserMapper userMapper;


    public ProjectMapperService(ProjectService projectService, GroupService groupService, GroupMapperService groupMapperService, UserMapper userMapper) {
        this.projectService = projectService;
        this.groupService = groupService;
        this.groupMapperService = groupMapperService;
        this.userMapper = userMapper;
    }

    @Transactional
    public List<ProjectDetailsDto> getAllDetailedProjects() {
        List<Project> projects = projectService.findAllProjects();
        return projects.stream().map(this::detailedProjectDtoMapper).collect(toList());
    }

    @Transactional
    public List<SimpleProjectDto> getAllSimpleProjects() {
        return projectService.getAllSimpleProjectDtos();
    }

  public ExtendedProjectDto loadExtendedProjectDto(Long projectId)
      throws CouldNotFindEntityException {
    Project project =
        projectService
            .getProjectById(projectId)
            .orElseThrow(() -> new CouldNotFindEntityException("Project not found: " + projectId));
    SimpleProjectDto simpleProjectDto = mapToSimpleProjectDto(project);
    SimpleUserDto managerDetails = userMapper.getSimpleUser(project.getManager());

    List<GroupxProject> groupxProjects =
        projectService.findAllGroupxProjectsByProjectId(projectId);
    List<SimpleGroupDto> activeGroups =
        groupxProjects.stream().filter(GroupxProject::isActive).map(groupMapperService::mapToSimpleGroupDto).toList();
    List<SimpleGroupDto> deactivatedGroups =
        groupxProjects.stream().filter(groupxProject -> !groupxProject.isActive()).map(groupMapperService::mapToSimpleGroupDto).toList();

    List<SimpleUserDto> contributors =
        projectService.findAllContributorsByProjectId(projectId);

    return new ExtendedProjectDto(managerDetails, simpleProjectDto, activeGroups, deactivatedGroups, contributors);
    }

    public ExtendedGroupDto loadExtendedGroupDto(Long groupId)
      throws CouldNotFindEntityException {
        Groupx group =
        groupService
            .getGroup(groupId);
        SimpleGroupDto simpleGroupDto = groupMapperService.mapToSimpleGroupDto(group);
        List<GroupxProject> groupxProjects = projectService.getGroupxProjectsByGroupId(groupId);
        Set<GroupxProjectDto> groupxProjectsDto = groupxProjects.stream().map(this::groupxProjectDtoMapper).collect(Collectors.toSet());
        List<Userx> groupMembers = groupService.getMembers(groupId);
        Set<SimpleUserDto> groupMembersDto = groupMembers.stream().map(userMapper::getSimpleUser).collect(Collectors.toSet());
        return new ExtendedGroupDto(simpleGroupDto, groupxProjectsDto, groupMembersDto);
    }

    public List<SimpleGroupDto> getAllActiveSimpleGroups(String projectId){
        List<GroupxProject> groupxProjects = projectService.findAllGroupxProjectsByProjectId(Long.valueOf(projectId));
        return groupxProjects.stream().filter(GroupxProject::isActive).map(groupMapperService::mapToSimpleGroupDto).collect(toList());
    }

    public List<SimpleGroupDto> getAllDeactivatedSimpleGroups(String projectId){
        List<GroupxProject> groupxProjects = projectService.findAllGroupxProjectsByProjectId(Long.valueOf(projectId));
        return groupxProjects.stream().filter(groupxProject -> !groupxProject.isActive()).map(groupMapperService::mapToSimpleGroupDto).collect(toList());
    }


    public ExtendedProjectDto addGroupToProject(minimalGxpDto minimalGxpDto) throws IOException {
        GroupxProject groupxProject = projectService.createGroupxProject(Long.parseLong(minimalGxpDto.projectId()), Long.parseLong(minimalGxpDto.groupId()));
        projectService.saveGroupxProject(groupxProject);
        return loadExtendedProjectDto(Long.parseLong(minimalGxpDto.projectId()));
    }

    public List<SimpleProjectDto> getSimpleProjectsByGroupId(Long groupId) {
        List<Project> projects = projectService.getProjectsByGroupId(groupId);
        return projects.stream().map(this::mapToSimpleProjectDto).collect(toList());
    }
    public List<SimpleUserDto> findAllContributorsByGroupIdAndProjectId(Long groupId, Long projectId) {
        GroupxProject groupxProject = projectService.findByGroupAndProject(groupId, projectId);
        GroupxProjectDto groupxProjectdto = groupxProjectDtoMapper(groupxProject);
        List<SimpleUserDto> simpleUserDtos =  groupxProjectdto.contributors();
        return simpleUserDtos;
    }
  public SimpleProjectDto mapToSimpleProjectDto(Project project) {
        return new SimpleProjectDto(
                project.getId().toString(),
                project.isActive(),
                project.getName(),
                project.getDescription(),
                project.getManager().getUsername()
        );
    }

    public SimpleGroupxProjectDto mapToSimpleGroupxProjectDto(GroupxProject gxp) {
        return new SimpleGroupxProjectDto(
                gxp.getGroup().getId().toString(),
                gxp.getGroup().getName(),
                gxp.getProject().getId().toString(),
                gxp.getProject().getName()
        );
    }

    public GroupxProjectDto groupxProjectDtoMapper(GroupxProject groupxProject) {
        Project project = groupxProject.getProject();
        SimpleUserDto managerDetails = userMapper.getSimpleUser(project.getManager());
        List<SimpleUserDto> contributors = groupxProject.getContributors().stream().map(userMapper::getSimpleUser).toList();
        return new GroupxProjectDto(
                groupMapperService.mapToSimpleGroupDto(groupxProject),
                mapToSimpleProjectDto(project),
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
        return mapToSimpleProjectDto(updatedProject);
    }

    public SimpleProjectDto createProject(SimpleProjectDto projectDto) {
        Project createdProject =
                projectService.createProject(
                        projectDto.name(),
                        projectDto.description(),
                        projectDto.manager());
        return mapToSimpleProjectDto(createdProject);
    }

    private ProjectDetailsDto detailedProjectDtoMapper(Project project) {
        String projectId = project.getId().toString();
        String projectName = project.getName();
        String projectDescription = project.getDescription();
        SimpleUserDto projectManagerDto = userMapper.getSimpleUser(project.getManager());
        return new ProjectDetailsDto(projectId, projectName, projectDescription, projectManagerDto);
    }

    public GroupDetailsDto groupDetailsDto(Groupx groupx) {
        return new GroupDetailsDto(
                groupx.getId().toString(),
                groupx.getName(),
                groupx.getDescription(),
                userMapper.getSimpleUser(groupx.getGroupLead())
        );
    }
}
