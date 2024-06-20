package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.GroupMapperService;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.ProjectMapperService;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.ProjectService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

 private final ProjectService projectService;
 private final ProjectMapperService projectMapperService;
  private final Logger logger = Logger.getLogger("ProjectController");

  public ProjectController(ProjectService projectService, ProjectMapperService projectMapperService) {
    this.projectService = projectService;
    this.projectMapperService = projectMapperService;
  }

  @GetMapping("/all")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<SimpleProjectDto>> getAllSimpleProjects() {
      List<SimpleProjectDto> projects = projectMapperService.getAllSimpleProjects();
      return ResponseEntity.ok(projects);
    }

  @GetMapping("/allDetailed")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<List<ProjectDetailsDto>> getAllDetailedProjects() {
    List<ProjectDetailsDto> projects = projectMapperService.getAllDetailedProjects();
    return ResponseEntity.ok(projects);
  }

@PutMapping("/reactivate/{projectId}")
@PreAuthorize("hasAuthority('MANAGER')")
public ResponseEntity<SimpleProjectDto> reactivateProject(@PathVariable String projectId) {
      SimpleProjectDto reactivatedProject = projectMapperService.reactivateProject(projectId);
  return ResponseEntity.ok(reactivatedProject);
}



  @PutMapping("/update")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<SimpleProjectDto> updateProject(@RequestBody SimpleProjectDto projectData) {

    SimpleProjectDto updatedProject = projectMapperService.updateProject(projectData);
    return ResponseEntity.ok(updatedProject);
  }

  @PostMapping("/create")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<SimpleProjectDto> createProject(@RequestBody SimpleProjectDto projectData) {
    SimpleProjectDto createdProject = projectMapperService.createProject(projectData);
    return ResponseEntity.ok(createdProject);
  }

  @DeleteMapping("/delete/{projectId}")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<MessageResponse> deleteProject(@PathVariable String projectId) {
    projectService.deleteProject(Long.parseLong(projectId));
    return ResponseEntity.ok().body(new MessageResponse("Project deleted successfully!"));
  }

  @GetMapping("/loadExtendedProject/{projectId}")
  @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('GROUPLEAD')")
  public ResponseEntity<ExtendedProjectDto> getProjectDetailedById(@PathVariable String projectId) {
      try{
        ExtendedProjectDto projectDto = projectMapperService.loadExtendedProjectDto(Long.parseLong(projectId));
        return ResponseEntity.ok(projectDto);
      } catch (CouldNotFindEntityException e) {
        logger.warning(e.getMessage());
        return ResponseEntity.badRequest().build();
      }
  }

    @GetMapping("/loadSimpleProject/{projectId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<SimpleProjectDto> getProjectSimpleById(@PathVariable String projectId) {
        try{
            SimpleProjectDto projectDto = projectMapperService.loadSimpleProjectDto(projectId);
            return ResponseEntity.ok(projectDto);
        } catch (CouldNotFindEntityException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


  @GetMapping("/getActiveGroupsOfProject/{projectId}")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<List<SimpleGroupDto>> getActiveGroupsByProjectId(@PathVariable String projectId) {
    List<SimpleGroupDto> activeGroups = projectMapperService.getAllActiveSimpleGroups(projectId);
    return ResponseEntity.ok(activeGroups);
  }

  @GetMapping("/getDeactivatedGroupsOfProject/{projectId}")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<List<SimpleGroupDto>> getDeactivatedGroupsByProjectId(@PathVariable String projectId) {
    List<SimpleGroupDto> deactivatedGroups = projectMapperService.getAllDeactivatedSimpleGroups(projectId);
    return ResponseEntity.ok(deactivatedGroups);
  }

  @PostMapping("/addGroup")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<ExtendedProjectDto> addGroupToProject(
      @RequestBody minimalGxpDto minimalGxpDto) {
    try{
      ExtendedProjectDto extendedProjectDto = projectMapperService.addGroupToProject(minimalGxpDto);
      return ResponseEntity.ok(extendedProjectDto);
    } catch (Exception e) {
      logger.warning(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/removeGroup/{projectId}/{groupId}")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<Void> removeGroupFromProject(
      @PathVariable String projectId, @PathVariable String groupId){
    try {
      projectService.removeGroupFromProject(Long.parseLong(groupId), Long.parseLong(projectId));
      return ResponseEntity.ok().build();
    } catch (CouldNotFindEntityException e) {
      logger.warning(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/addContributor")
  @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('GROUPLEAD')")
  public ResponseEntity<ExtendedProjectDto> addContributor(
      @RequestBody ContributorAssignmentDto contributorAssignmentDto) {
    try {
      logger.info("addContributor called");
    projectService.addContributor(Long.parseLong(contributorAssignmentDto.groupId()),
        Long.parseLong(contributorAssignmentDto.projectId()), contributorAssignmentDto.contributorId());
    ExtendedProjectDto extendedProjectDtoproject = projectMapperService.loadExtendedProjectDto(Long.parseLong(contributorAssignmentDto.projectId()));
    return ResponseEntity.ok(extendedProjectDtoproject);
    } catch (Exception e) {
      logger.warning("caught exception in Controller: %s".formatted(e.getMessage()));
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/removeContributor/{projectId}/{groupId}/{contributorId}")
  @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('GROUPLEAD')")
  public ResponseEntity<ExtendedProjectDto> removeContributor(
      @PathVariable String projectId, @PathVariable String groupId, @PathVariable String contributorId) {
    try {
      projectService.removeContributor(Long.parseLong(groupId), Long.parseLong(projectId), contributorId);
      ExtendedProjectDto extendedProjectDto = projectMapperService.loadExtendedProjectDto(Long.parseLong(projectId));
      return ResponseEntity.ok(extendedProjectDto);
    } catch (Exception e) {
      logger.warning("caught exception in Controller: %s".formatted(e.getMessage()));
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/projectsOfGroup/{groupId}")
  @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('GROUPLEAD')")
  public ResponseEntity<List<SimpleProjectDto>> getProjectsByGroupId(@PathVariable String groupId) {
    List<SimpleProjectDto> projects = projectMapperService.getSimpleProjectsByGroupId(Long.parseLong(groupId));
    return ResponseEntity.ok(projects);
  }

    @GetMapping("/contributors/{groupId}/{projectId}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('GROUPLEAD')")
    public ResponseEntity<List<SimpleUserDto>> getContributors(@PathVariable String groupId, @PathVariable String projectId) {
      List<SimpleUserDto> contributors = projectMapperService.findAllContributorsByGroupIdAndProjectId(Long.parseLong(groupId), Long.parseLong(projectId));
      return ResponseEntity.ok(contributors);
}

}
