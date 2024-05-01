package at.qe.skeleton.services;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.ProjectRepository;
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
    private UserxService userxService;

    @Transactional
    public Project createProject(Project project) {
        if(projectRepository.findFirstByName(project.getName()) != null) {
            throw new IllegalArgumentException("Project with name " + project.getName() + " already exists");
        }
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
    public void addContributorToProject(Long id, Userx contributor) {
        if (contributor == null) {
            throw new NullPointerException("Contributor can not be null");
        }
        Project project = projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project with ID " + id + " not found"));
        project.addContributor(contributor);
        projectRepository.save(project);
    }

    public Project loadProject(String id) {
        return projectRepository.findFirstByName(id);
    }

    public Project updateProject(Project project) {
        Project updatedProject = projectRepository.findFirstByName(project.getName());
        updatedProject.setDescription(project.getDescription());
        updatedProject.setManager(project.getManager());
        return projectRepository.save(project);
    }

    public void deleteProject(String id) {
        Project project = projectRepository.findFirstByName(id);
        projectRepository.delete(project);
    }

    public Project createProject1() {
        Project project = new Project();
        project.setName("name");
        project.setDescription("description");
        project.setManager(userxService.loadUser("admin"));
        return projectRepository.save(project);
    }
}