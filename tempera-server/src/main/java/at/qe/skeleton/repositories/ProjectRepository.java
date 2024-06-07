package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.dtos.SimpleProjectDbDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProjectRepository extends AbstractRepository<Project, Long> {

  Project findFirstByName(String name);

  Project findFirstByManager(Userx manager);

  void delete(Project project);

  Project findFirstById(Long id);

  public List<Project> findAllByManager_Username(String username);

  @Query(
      "select p from Project p join p.groupxProjects gxp join gxp.group g where g.groupLead.username = :username")
  public List<Project> findAllByGroupLead(String username);

  public List<Project> findAll();
}
