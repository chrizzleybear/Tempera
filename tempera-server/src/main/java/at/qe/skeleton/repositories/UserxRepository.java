package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link Userx} entities.
 *
 * <p>This class is part of the skeleton project provided for students of the course "Software
 * Engineering" offered by the University of Inn  public void addProject(Project project) {
 *     if (project == null) {
 *       throw new IllegalArgumentException("Project must not be null to be added to group %s".formatted(name));
 *     }
 *     this.projects.add(project);
 *     project.getGroups().add(this);
 *   }
 *
 *   public void removeProject(Project project) {
 *     if (project == null) {
 *       throw new IllegalArgumentException("Project must not be null to be removed from group %s".formatted(name));
 *     }
 *     this.projects.remove(project);
 *     project.getGroups().remove(this);
 *   }sbruck.
 */
public interface UserxRepository extends AbstractRepository<Userx, String> {

  Userx findFirstByUsername(String username);

  Optional<Userx> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  List<Userx> findByUsernameContaining(String username);

  @Query("SELECT u FROM Userx u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
  List<Userx> findByWholeNameConcat(@Param("wholeName") String wholeName);

  @Query("SELECT u FROM Userx u WHERE :role MEMBER OF u.roles")
  List<Userx> findByRole(@Param("role") UserxRole role);

}
