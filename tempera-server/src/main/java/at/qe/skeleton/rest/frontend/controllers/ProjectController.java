package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.ProjectMapperService;
import at.qe.skeleton.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

  @Autowired ProjectService projectService;
  @Autowired
  ProjectMapperService projectMapperService;
  private Logger logger = Logger.getLogger("ProjectController");

    @GetMapping("/all")
    public ResponseEntity<List<ProjectDetailsDto>> getAllProjects() {
      List<ProjectDetailsDto> projects = projectMapperService.getAllDetailedProjects();
      return ResponseEntity.ok(projects);
    }


  @PutMapping("/update")
  public ResponseEntity<SimpleProjectDto> updateProject(@RequestBody SimpleProjectDto projectData) {

    SimpleProjectDto updatedProject = projectMapperService.updateProject(projectData);
    //todo: add method to projectMapperService
    return ResponseEntity.ok(updatedProject);
  }

  @PostMapping("/create")
  public ResponseEntity<SimpleProjectDto> createProject(@RequestBody SimpleProjectDto projectData) {
    SimpleProjectDto createdProject = projectMapperService.createProject(projectData);
    return ResponseEntity.ok(createdProject);
  }

  //todo: was ist hier mit?
  @DeleteMapping("/delete/{projectId}")
  public ResponseEntity<String> deleteProject(@PathVariable String projectId) {
    projectService.deleteProject(Long.parseLong(projectId));
    return ResponseEntity.ok("Project deleted");
  }

  @GetMapping("/loadExtendedProject/{projectId}")
  public ResponseEntity<ExtendedProjectDto> getProjectDetailedById(@PathVariable String projectId) {
      try{
        ExtendedProjectDto projectDto = projectMapperService.loadExtendedProjectDto(Long.parseLong(projectId));
        return ResponseEntity.ok(projectDto);
      } catch (CouldNotFindEntityException e) {
        logger.warning(e.getMessage());
        return ResponseEntity.badRequest().build();
      }
  }

  @GetMapping("/loadExtendedGroup/{groupId}")
  public ResponseEntity<ExtendedGroupDto> getExtendedGroupById(@PathVariable String groupId) {
    try {
      ExtendedGroupDto groupDto = projectMapperService.loadExtendedGroupDto(Long.parseLong(groupId));
      return ResponseEntity.ok(groupDto);
    } catch (CouldNotFindEntityException e) {
      logger.warning(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  //ehemals getGroups
  @GetMapping("/getGroupsOfProject/{projectId}")
  public ResponseEntity<List<SimpleGroupDto>> getGroupsByProjectId(@PathVariable String projectId) {
    List<SimpleGroupDto> groups = projectMapperService.getAllSimpleGroups(projectId);
    return ResponseEntity.ok(groups);
  }

  @PostMapping("/addGroup")
  public ResponseEntity<ExtendedProjectDto> addGroupToProject(
      @RequestBody GroupAssignmentDto groupAssignmentDto) {
    try{
      ExtendedProjectDto extendedProjectDto = projectMapperService.addGroupToProject(groupAssignmentDto);
      return ResponseEntity.ok(extendedProjectDto);
    } catch (Exception e) {
      logger.warning(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/removeGroup/{projectId}/{groupId}")
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
  public ResponseEntity<ExtendedProjectDto> addContributßor(
      @RequestBody ContributorAssignmentDto contributorAssignmentDto) {
    try {
      System.out.println(contributorAssignmentDto);
    projectService.addContributor(contributorAssignmentDto.groupId(),
        contributorAssignmentDto.projectId(), contributorAssignmentDto.contributorId());
    ExtendedProjectDto extendedProjectDtoproject = projectMapperService.loadExtendedProjectDto(contributorAssignmentDto.projectId());
    return ResponseEntity.ok(extendedProjectDtoproject);
    } catch (Exception e) {
      logger.warning("caught exception in Controller: %s".formatted(e.getMessage()));
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/removeContributor/{projectId}/{groupId}/{contributorId}")
  public ResponseEntity<ExtendedProjectDto> removeContributor(
      @PathVariable String projectId, @PathVariable String groupId, @PathVariable String contributorId) throws CouldNotFindEntityException {
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
  public ResponseEntity<List<SimpleProjectDto>> getProjectsByGroupId(@PathVariable String groupId) {
    List<SimpleProjectDto> projects = projectMapperService.getSimpleProjectsByGroupId(Long.parseLong(groupId));
    return ResponseEntity.ok(projects);
  }

    @GetMapping("/contributors/{groupId}/{projectId}")
    public ResponseEntity<List<SimpleUserDto>> getContributors(@PathVariable String groupId, @PathVariable String projectId) {
      List<SimpleUserDto> contributors = projectMapperService.findAllContributorsByGroupIdAndProjectId(Long.parseLong(groupId), Long.parseLong(projectId));
      return ResponseEntity.ok(contributors);
}

}
