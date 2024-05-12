package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Project;

import java.util.List;

public interface ProjectRepository extends AbstractRepository<Project, Long>{
    public List<Project> findAllByManager_Username(String username);

    public List<Project> findAllByContributors_Username(String username);
}
