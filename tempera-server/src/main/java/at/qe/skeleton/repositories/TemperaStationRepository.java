package at.qe.skeleton.repositories;

import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.UserStateDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemperaStationRepository extends AbstractRepository<TemperaStation, String> {

  @Query("SELECT t FROM TemperaStation t WHERE t.enabled = :enabled")
  List<TemperaStation> findAllByEnabled(@Param("enabled") boolean enabled);

  Optional<TemperaStation> findFirstByUser(Userx user);

  Optional<TemperaStation> findById(String id);

  Optional<TemperaStation> findFirstByUser_Username(String username);

}
