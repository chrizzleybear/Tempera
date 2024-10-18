package at.qe.skeleton.repositories;

import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import at.qe.skeleton.rest.frontend.dtos.UserStateDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TemperaStationRepository extends AbstractRepository<TemperaStation, String> {

  @Query("SELECT t FROM TemperaStation t WHERE t.enabled = :enabled")
  List<TemperaStation> findAllByEnabled(@Param("enabled") boolean enabled);

  @Query("select new at.qe.skeleton.rest.frontend.dtos.TemperaStationDto(temp.id, temp.user.username, temp.enabled, temp.isHealthy, CAST (temp.accessPoint.id as string)) from TemperaStation temp where temp.user.username = :username")
  Optional<TemperaStationDto> getTemperaStationDtoByUsername(@Param("username") String username);

  @Query("SELECT thresh FROM TemperaStation temp JOIN temp.accessPoint ap JOIN ap.room r JOIN r.thresholds thresh WHERE temp.id = :temperaId")
  Set<Threshold> getThresholdsByTemperaId(@Param("temperaId") String temperaId);

  Optional<TemperaStation> findFirstByUser(Userx user);

  Optional<TemperaStation> findById(String id);

  Optional<TemperaStation> findFirstByUser_Username(String username);

}
