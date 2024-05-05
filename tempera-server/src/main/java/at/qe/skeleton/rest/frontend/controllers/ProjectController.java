package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllUsers() {
        List<Project> projects = projectService.getAllProjects().stream().toList();
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/update")
    public ResponseEntity<Project> updateProject(@RequestBody Map<String, String> projectData) {
        Project updatedProject = projectService.updateProject(projectData.get("id"), projectData.get("name"), projectData.get("description"), projectData.get("manager"));
        return ResponseEntity.ok(updatedProject);
    }

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Map<String, String> projectData) {
        Project createdProject = projectService.createProject(projectData.get("name"), projectData.get("description"), projectData.get("manager"));
        return ResponseEntity.ok(createdProject);
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable String projectId) {
        projectService.deleteProject(Long.parseLong(projectId));
        return ResponseEntity.ok("Project deleted");
    }
    @GetMapping("/load/{id}")
    public ResponseEntity<Project> getProject(@PathVariable String id) {
        Project project = projectService.loadProject(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/getGroups/{id}")
    public ResponseEntity<List<Group>> getGroups(@PathVariable String id) {
        List<Group> groups = projectService.loadProject(id).getGroups();
        return ResponseEntity.ok(groups);
    }
    @PostMapping("/addGroup")
    public ResponseEntity<Void> addGroupToProject(@RequestBody Map<String, String> projectData) {
        projectService.addGroupToProject(Long.parseLong(projectData.get("groupId")), Long.parseLong(projectData.get("projectId")));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteGroup/{projectId}/{groupId}")
    public ResponseEntity<Void> removeGroupFromProject(@PathVariable String projectId, @PathVariable String groupId) {
        projectService.deleteGroup(Long.parseLong(groupId), Long.parseLong(projectId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addContributor")
    public ResponseEntity<Void> addContributor(@RequestBody Map<String, String> projectData) {
        if(projectData.get("projectId") == null || projectData.get("contributorId") == null) {
            System.out.println("ProjectId: " + projectData.get("projectId"));
            throw new NullPointerException("ProjectId and ContributorId can not be null");
        }
        projectService.addContributor(Long.parseLong(projectData.get("projectId")), projectData.get("contributorId"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allOfGroup/{groupId}")
    public ResponseEntity<List<Project>> getProjects(@PathVariable String groupId) {
        System.out.println("groupId: " + groupId);
        List<Project> projects = projectService.getProjectsForGroups(Long.parseLong(groupId));
        return ResponseEntity.ok(projects);
    }

}
