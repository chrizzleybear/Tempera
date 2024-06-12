package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementId;
import at.qe.skeleton.model.SensorId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

public interface MeasurementRepository extends AbstractRepository<Measurement, MeasurementId> {


  @EntityGraph(value = "Measurement.detail", type = EntityGraph.EntityGraphType.FETCH)
  @Query("SELECT m FROM Measurement m WHERE m.id = :measurementId")
  Optional<Measurement> findByIdDetailed(MeasurementId measurementId);

  Optional<Measurement> findFirstBySensorIdOrderById_TimestampDesc(SensorId sensorId);

  Optional<List<Measurement>> findTop100BySensorIdOrderById_TimestampAsc(SensorId sensorId);
}
