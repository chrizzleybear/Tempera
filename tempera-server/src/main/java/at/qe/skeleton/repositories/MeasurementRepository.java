package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementId;
import at.qe.skeleton.model.SensorTemperaCompositeId;

import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends AbstractRepository<Measurement, MeasurementId> {

  List<Measurement> findAllBySensorId(SensorTemperaCompositeId sensor_id);

  Optional<Measurement> findFirstBySensorIdOrderById_TimestampDesc(SensorTemperaCompositeId sensor_id);
}
