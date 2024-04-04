package at.qe.skeleton.repositories;

import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TemperaStationRepository extends AbstractRepository<TemperaStation, String> {

    @Query("SELECT t FROM TemperaStation t WHERE t.enabled = :enabled")
    List<TemperaStation> findAllByEnabled(@Param("enabled") boolean enabled);

}
