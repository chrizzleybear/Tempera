package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.services.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Nodes.collect;

@Service
public class ProjectMapperService {
    private ProjectService projectService;


    public ProjectMapperService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<SimpleProjectDto> getAllSimpleProjects() {
        List<Project> projects = projectService.findAllProjects();
        return projects.stream().map(this::projectDtoMapper).collect(toList());
    }

  public ExtendedProjectDto loadProjectDetailed(String projectId)
      throws CouldNotFindEntityException {
    Project project =
        projectService
            .getProjectById(Long.parseLong(projectId))
            .orElseThrow(() -> new CouldNotFindEntityException("Project not found: " + projectId));
    SimpleProjectDto simpleProjectDto = projectDtoMapper(project);

    List<GroupxProject> groupxProjects =
        projectService.findAllGroupxProjectsByProjectId(Long.parseLong(projectId));
    List<SimpleGroupDto> connectedGroups =
        groupxProjects.stream().map(this::groupDtoMapper).toList();

    List<SimpleUserDto> contributors =
        projectService.findAllContributorsByProjectId(Long.parseLong(projectId));

    return new ExtendedProjectDto(simpleProjectDto, connectedGroups, contributors);
    }

    public List<SimpleGroupDto> getAllSimpleGroups(String projectId){
        List<GroupxProject> groupxProjects = projectService.findAllGroupxProjectsByProjectId(Long.valueOf(projectId));
        return groupxProjects.stream().map(this::groupDtoMapper).collect(toList());
    }

  private SimpleProjectDto projectDtoMapper(Project project) {
        return new SimpleProjectDto(
                project.getId().toString(),
                project.getName(),
                project.getDescription(),
                project.getManager().getUsername()
        );
    }

    private SimpleGroupDto groupDtoMapper(GroupxProject groupxProject) {
        return new SimpleGroupDto(
                groupxProject.getGroup().getId().toString(),
                groupxProject.getGroup().getName(),
                groupxProject.getGroup().getGroupLead().getUsername(),
                groupxProject.getGroup().getGroupLead().getUsername()
        );
    }



}
