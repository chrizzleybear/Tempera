package at.qe.skeleton.services;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.ProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private GroupRepository groupRepository;


    @Transactional
    public Project createProject(String name, String description, String manager) {
        Userx managerUser = userxRepository.findById(manager).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setManager(managerUser);
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Project updateProject(String id, String name, String description, String manager) {
        Project project = projectRepository.findById(Long.parseLong(id)).orElseThrow(() -> new IllegalArgumentException("Project with ID " + id + " not found"));
        project.setName(name);
        project.setDescription(description);
        project.setManager(userxRepository.findById(manager).orElseThrow(() -> new IllegalArgumentException("User not found")));
        return projectRepository.save(project);
    }

    @Transactional
    public void addGroupToProject(Long groupId, Long projectId) {
        if (groupId == null) {
            throw new NullPointerException("Contributor can not be null");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        project.addGroup(group);
        projectRepository.save(project);
    }

    public Project loadProject(String id) {
        Long idNumber = Long.parseLong(id);
        return projectRepository.findFirstById(idNumber);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findFirstById(projectId);
        projectRepository.delete(project);
    }

    public void deleteGroup(Long groupId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        project.removeGroup(group);
        projectRepository.save(project);
    }
}