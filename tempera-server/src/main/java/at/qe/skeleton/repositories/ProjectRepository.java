package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends AbstractRepository<Project, Long>{

    Project findFirstByName(String name);
    Project findFirstByManager(Userx manager);
    void delete(Project project);
    Project findFirstById(Long id);
    public List<Project> findAllByManager_Username(String username);

    public List<Project> findAll();
}
