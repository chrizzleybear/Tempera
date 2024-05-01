package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;

public interface ProjectRepository extends AbstractRepository<Project, Long>{

    Project findFirstByName(String name);
    Project findFirstByManager(Userx manager);
    void delete(Project project);

}
