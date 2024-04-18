package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorTemperaCompositeId;

import java.util.List;

public interface MeasurementRepository extends AbstractRepository<Measurement, Long>{

    List<Measurement> findAllBySensorId(SensorTemperaCompositeId sensor_id);

    Measurement findFirstBySensorIdOrderByTimestampDesc(SensorTemperaCompositeId sensor_id);
}
