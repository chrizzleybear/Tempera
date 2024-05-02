package at.qe.skeleton.controllers;

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
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        Project updatedProject = projectService.updateProject(project);
        return ResponseEntity.ok(updatedProject);
    }

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Map<String, String> projectData) {
        Project createdProject = projectService.createProject(projectData.get("name"), projectData.get("description"), projectData.get("manager"));
        return ResponseEntity.ok(createdProject);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteProject(@PathVariable String name) {
        projectService.deleteProject(name);
        return ResponseEntity.ok("Project deleted");
    }
    @GetMapping("/load/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        Project project = projectService.loadProject(id);
        return ResponseEntity.ok(project);
    }

}
