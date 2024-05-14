package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends AbstractRepository<Project, Long>{

    Project findFirstByName(String name);
    Project findFirstByManager(Userx manager);
    void delete(Project project);
    Project findFirstById(Long id);

    @Query("SELECT p FROM Project p JOIN p.groups g WHERE g.id = :groupId")
    List<Project> findByGroupId(Long groupId);

}
