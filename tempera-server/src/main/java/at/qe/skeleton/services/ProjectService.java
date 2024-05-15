package at.qe.skeleton.services;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.repositories.ProjectRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Scope("application")
public class ProjectService {

    private final ProjectRepository projectRepository;

public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getProjectsByManager(String username) {
        return projectRepository.findAllByManager_Username(username);
    }

    public List<Project> getProjectsByContributor(String username) {
        return projectRepository.findAllByContributors_Username(username);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    @PreAuthorize("isAuthenticated()")
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

}
