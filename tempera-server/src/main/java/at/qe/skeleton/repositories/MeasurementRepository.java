package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementId;
import at.qe.skeleton.model.SensorId;

import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends AbstractRepository<Measurement, MeasurementId> {

  List<Measurement> findAllBySensorId(SensorId sensor_id);

  Optional<Measurement> findFirstBySensorIdOrderById_TimestampDesc(SensorId sensor_id);
}
