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


    private static final String USER_NOT_FOUND = "User not found";
    private static final String PROJECT_NOT_FOUND = "Project not found";
    private static final String GROUP_NOT_FOUND = "Group not found";



    @Transactional
    public Project createProject(String name, String description, String manager) {
        Userx managerUser = userxRepository.findById(manager).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
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
    public Project updateProject(Long id, String name, String description, String manager) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project with ID " + id + " not found"));
        project.setName(name);
        project.setDescription(description);
        project.setManager(userxRepository.findById(manager).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND)));
        return projectRepository.save(project);
    }

    @Transactional
    public void addGroupToProject(Long projectId, Long groupId) {
        if (groupId == null) {
            throw new NullPointerException("Contributor can not be null");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException(PROJECT_NOT_FOUND));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(GROUP_NOT_FOUND));
        project.addGroup(group);
        projectRepository.save(project);
    }

    public Project loadProject(Long projectId) {
        return projectRepository.findFirstById(projectId);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findFirstById(projectId);
        projectRepository.delete(project);
    }

    public void deleteGroup(Long groupId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException(PROJECT_NOT_FOUND));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(GROUP_NOT_FOUND));
        project.removeGroup(group);
        projectRepository.save(project);
    }

    @Transactional
    public void addContributor(Long projectId, String contributorId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException(PROJECT_NOT_FOUND));
        Userx contributor = userxRepository.findById(contributorId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        if(project.getContributors().contains(contributor)){
            throw new IllegalArgumentException("User is already a contributor");
        }
        project.addContributor(contributor);
        projectRepository.save(project);
    }
    public List<Project> getProjectsForGroups(Long groupId) {
        if(groupRepository.findById(groupId).isEmpty()){
            throw new IllegalArgumentException(GROUP_NOT_FOUND);
        }
        return projectRepository.findByGroupId(groupId);
    }

    @Transactional
    public void deleteContributor(Long projectId, String contributorId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException(PROJECT_NOT_FOUND));
        Userx contributor = userxRepository.findById(contributorId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        project.removeContributor(contributor);
        projectRepository.save(project);
    }
}