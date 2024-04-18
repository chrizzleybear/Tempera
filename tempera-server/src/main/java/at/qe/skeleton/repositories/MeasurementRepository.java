package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorTemperaCompositeId;

import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends AbstractRepository<Measurement, Long>{

    List<Measurement> findAllBySensorId(SensorTemperaCompositeId sensor_id);

    Optional<Measurement> findFirstBySensorIdOrderByTimestampDesc(SensorTemperaCompositeId sensor_id);
}
