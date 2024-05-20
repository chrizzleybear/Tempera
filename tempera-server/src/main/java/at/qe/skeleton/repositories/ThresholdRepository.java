package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Threshold;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ThresholdRepository extends AbstractRepository<Threshold, Long> {
    List<Threshold> findAll();
    boolean existsById(Long thresholdID);

    @Query("SELECT * FROM Threshold WHERE Threshold.defaultThreshold = TRUE")
    List<Threshold> findDefaultThresholds();
}
