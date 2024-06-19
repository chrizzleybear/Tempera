package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Threshold;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface ThresholdRepository extends AbstractRepository<Threshold, Long> {
    List<Threshold> findAll();
    boolean existsById(Long thresholdID);

    @Query("SELECT t FROM Threshold t WHERE t.defaultThreshold = TRUE")
    List<Threshold> findDefaultThresholds();

    @Query("SELECT t FROM Userx u JOIN u.temperaStation ts JOIN ts.accessPoint ap JOIN ap.room r JOIN r.thresholds t WHERE u.username = :username")
    Set<Threshold> getThresholdsByUsername(String username);

}
