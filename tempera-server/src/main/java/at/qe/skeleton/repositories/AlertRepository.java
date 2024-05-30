package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Alert;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.Threshold;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends AbstractRepository<Alert, Long>{

    @Query("SELECT a FROM Alert a JOIN a.sensor s WHERE s = :sensor AND a.threshold = :threshold AND a.acknowledged = false")
    public Optional<Alert> findOpenAlertBySensorAndThreshold(@Param("sensor") Sensor sensor, @Param("threshold") Threshold threshold);


    @EntityGraph(attributePaths = {"sensor", "threshold"})
    @Query("SELECT a FROM Alert a JOIN a.sensor s JOIN s.temperaStation t join t.user u WHERE u.username = :username AND a.acknowledged = false")
    public List<Alert> findRelevantAlertsDetailedSensThresh(String username);

    @EntityGraph(attributePaths = {"sensor", "threshold"})
    @Query("SELECT a FROM Alert a JOIN a.sensor s JOIN s.temperaStation t join t.user u WHERE u.username = :username AND a.acknowledged = true ORDER BY a.firstIncident DESC LIMIT 1")
    public Optional<Alert> findLastAcknowledgedAlertBySensorAndThreshold(Sensor sensor, Threshold threshold);
}
