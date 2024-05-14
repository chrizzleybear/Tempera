package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.rest.frontend.dtos.ContributorAssignmentDto;
import at.qe.skeleton.rest.frontend.dtos.GroupAssignmentDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

  @Autowired ProjectService projectService;

  @GetMapping("/all")
  public ResponseEntity<List<Project>> getAllUsers() {
    List<Project> projects = projectService.getAllProjects().stream().toList();
    return ResponseEntity.ok(projects);
  }

  @PutMapping("/update")
  public ResponseEntity<Project> updateProject(@RequestBody SimpleProjectDto projectData) {
    Project updatedProject =
        projectService.updateProject(
            projectData.projectId(),
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

  @GetMapping("/load/{id}")
  public ResponseEntity<Project> getProject(@PathVariable Long id) {
    Project project = projectService.loadProject(id);
    return ResponseEntity.ok(project);
  }

  @GetMapping("/getGroups/{id}")
  public ResponseEntity<List<Group>> getGroups(@PathVariable Long id) {
    List<Group> groups = projectService.loadProject(id).getGroups();
    return ResponseEntity.ok(groups);
  }

  @PostMapping("/addGroup")
  public ResponseEntity<Void> addGroupToProject(
      @RequestBody GroupAssignmentDto groupAssignmentDto) {
    projectService.addGroupToProject(groupAssignmentDto.projectId(), groupAssignmentDto.groupId());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/deleteGroup/{projectId}/{groupId}")
  public ResponseEntity<Void> removeGroupFromProject(
      @PathVariable String projectId, @PathVariable String groupId) {
    projectService.deleteGroup(Long.parseLong(groupId), Long.parseLong(projectId));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/addContributor")
  public ResponseEntity<Project> addContributor(
      @RequestBody ContributorAssignmentDto contributorAssignmentDto) {
    projectService.addContributor(
        contributorAssignmentDto.projectId(), contributorAssignmentDto.contributorId());
    Project project = projectService.loadProject(contributorAssignmentDto.projectId());
    return ResponseEntity.ok(project);
  }

  @DeleteMapping("/deleteContributor/{projectId}/{contributorId}")
  public ResponseEntity<Project> removeContributor(
      @PathVariable String projectId, @PathVariable String contributorId) {
    projectService.deleteContributor(Long.parseLong(projectId), contributorId);
    Project project = projectService.loadProject(Long.parseLong(projectId));
    return ResponseEntity.ok(project);
  }

  @GetMapping("/allOfGroup/{groupId}")
  public ResponseEntity<List<Project>> getProjects(@PathVariable String groupId) {
    List<Project> projects = projectService.getProjectsForGroups(Long.parseLong(groupId));
    return ResponseEntity.ok(projects);
  }
}
