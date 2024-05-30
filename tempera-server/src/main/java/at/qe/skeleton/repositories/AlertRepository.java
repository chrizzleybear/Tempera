package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Alert;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.enums.ThresholdType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface AlertRepository extends AbstractRepository<Alert, Long>{

    @Query("SELECT a FROM Alert a JOIN a.sensor s WHERE s = :sensor AND a.threshold = :threshold AND a.acknowledged = false")
    public Optional<Alert> findOpenAlertBySensorAndThresholdType(@Param("sensor") Sensor sensor, @Param("threshold") Threshold threshold);

}
