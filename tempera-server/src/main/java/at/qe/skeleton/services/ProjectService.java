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
    public Project updateProject(Long id, String name, String description) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project with ID " + id + " not found"));
        project.setName(name);
        project.setDescription(description);
        return projectRepository.save(project);
    }

    @Transactional
    public void addContributorToProject(Long groupId, Long id) {
        if (groupId == null) {
            throw new NullPointerException("Contributor can not be null");
        }
        Project project = projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        project.addContributor(group);
        projectRepository.save(project);
    }

    public Project loadProject(Long id) {
        return projectRepository.findFirstById(id);
    }

    public Project updateProject(Project project) {
        Project updatedProject = projectRepository.findFirstByName(project.getName());
        updatedProject.setDescription(project.getDescription());
        updatedProject.setManager(project.getManager());
        return projectRepository.save(project);
    }

    public void deleteProject(String name) {
        Project project = projectRepository.findFirstByName(name);
        projectRepository.delete(project);
    }

}