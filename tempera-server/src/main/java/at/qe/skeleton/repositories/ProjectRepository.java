package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;

import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProjectRepository extends AbstractRepository<Project, Long> {

  Project findFirstByName(String name);

  Project findFirstByManager(Userx manager);

  void delete(Project project);

  Project findFirstById(Long id);

  public List<Project> findAllByManager_Username(String username);

  @Query(
      "select p from Project p join p.groupxProjects gxp join gxp.group g where g.groupLead.username = :username")
  public List<Project> findAllByGroupLead(String username);

  @Query("SELECT new at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto(CAST(p.id AS STRING), p.name, p.description, p.manager.username) FROM Project p  WHERE :username = p.manager.username")
  List<SimpleProjectDto> findAllSimpleProjectDtosByManager(@Param("username") String managerUsername);
  public List<Project> findAll();
}
