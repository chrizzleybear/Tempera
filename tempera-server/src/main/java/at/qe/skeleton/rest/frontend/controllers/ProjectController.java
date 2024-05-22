package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
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
    public ResponseEntity<List<SimpleProjectDto>> getAllProjects() {
      List<SimpleProjectDto> projects = projectMapperService.getAllSimpleProjects();
      return ResponseEntity.ok(projects);
    }


//  @GetMapping("/manager/all")
//  public ResponseEntity<List<GroupxProjectDto>> getAllProjectXGroups() {
//    List<GroupxProjectDto> groupxProjectDtos = projectMapperService.getAllGroupxProjectsAsManager();
//    return ResponseEntity.ok(groupxProjectDtos);
//  }
//
//    @GetMapping("/grouplead/all")
//    public ResponseEntity<List<GroupxProjectDto>> getAllGroupxProjectsAsGroupLead(@RequestParam("managerId") String managerId) {
//      List<GroupxProjectDto> groupxProjectDtos = projectMapperService.getAllGroupxProjectsAsGrouplead(managerId);
//      return ResponseEntity.ok(groupxProjectDtos);
//    }




  @PutMapping("/update")
  public ResponseEntity<Project> updateProject(@RequestBody SimpleProjectDto projectData) {
    Project updatedProject =
        projectService.updateProject(
            Long.parseLong(projectData.projectId()),
            projectData.name(),
            projectData.description(),
            projectData.manager());
    return ResponseEntity.ok(updatedProject);
  }

  @PostMapping("/create")
  public ResponseEntity<Project> createProject(@RequestBody SimpleProjectDto projectData) {
    Project createdProject =
        projectService.createProject(
            projectData.name(), projectData.description(), projectData.manager());
    return ResponseEntity.ok(createdProject);
  }

  @DeleteMapping("/delete/{projectId}")
  public ResponseEntity<String> deleteProject(@PathVariable String projectId) {
    projectService.deleteProject(Long.parseLong(projectId));
    return ResponseEntity.ok("Project deleted");
  }

  @GetMapping("/load/{projectId}")
  public ResponseEntity<ExtendedProjectDto> getProjectDetailedById(@PathVariable String projectId) {
      try{
        ExtendedProjectDto projectDto = projectMapperService.loadProjectDetailed(projectId);
        return ResponseEntity.ok(projectDto);
      } catch (CouldNotFindEntityException e) {
        logger.warning(e.getMessage());
        return ResponseEntity.badRequest().build();
      }

  }

  //ehemals getGroups
  @GetMapping("/getGroups/{id}")
  public ResponseEntity<List<SimpleGroupDto>> getGroupsByProjectId(@PathVariable String projectId) {
    List<SimpleGroupDto> groups = projectMapperService.getAllSimpleGroups(projectId);
    return ResponseEntity.ok(groups);
  }

  //todo: hier weiter machen

  @PostMapping("/addGroup")
  public ResponseEntity<Void> addGroupToProject(
      @RequestBody GroupAssignmentDto groupAssignmentDto) {
    try{
      projectService.addGroupToProject(groupAssignmentDto.projectId(), groupAssignmentDto.groupId());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      logger.warning(e.getMessage());
      return ResponseEntity.badRequest().build();
    }

  }

  @DeleteMapping("/deleteGroup/{projectId}/{groupId}")
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

  //todo: different return type? we do have a extendedProjectDto

  @PostMapping("/addContributor")
  public ResponseEntity<Project> addContributor(
      @RequestBody ContributorAssignmentDto contributorAssignmentDto) {
    try {
    projectService.addContributor(contributorAssignmentDto.groupId(),
        contributorAssignmentDto.projectId(), contributorAssignmentDto.contributorId());
    Project project = projectService.loadProject(contributorAssignmentDto.projectId());
    return ResponseEntity.ok(project);
    } catch (Exception e) {
      logger.warning("caught exception in Controller: %s".formatted(e.getMessage()));
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/deleteContributor/{projectId}/{contributorId}")
  public ResponseEntity<Project> removeContributor(
      @RequestBody ContributorAssignmentDto dto) {
    try{
      projectService.removeContributor(dto.groupId(), dto.projectId(), dto.contributorId());
      Project project = projectService.loadProject(dto.projectId());
      return ResponseEntity.ok(project);
    } catch (Exception e) {
      logger.warning("caught exception in Controller: %s".formatted(e.getMessage()));
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/allOfGroup/{groupId}")
  public ResponseEntity<List<Project>> getProjectsByGroupId(@PathVariable String groupId) {
    List<Project> projects = projectService.getProjectsByGroupId(Long.parseLong(groupId));
    return ResponseEntity.ok(projects);
  }
}
