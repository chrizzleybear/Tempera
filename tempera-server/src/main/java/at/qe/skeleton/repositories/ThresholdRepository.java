package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.enums.SensorType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ThresholdRepository extends AbstractRepository<Threshold, Long> {
    List<Threshold> findAll();
    boolean existsById(Long thresholdID);

    @Query("SELECT t FROM Threshold t WHERE t.defaultThreshold = TRUE")
    List<Threshold> findDefaultThresholds();

}
