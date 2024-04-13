package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;

import java.util.List;

public interface MeasurementRepository extends AbstractRepository<Measurement, Long>{

    List<Measurement> findAllBySensorId(Long sensorId);

    Measurement findFirstBySensorIdOrderByTimestampDesc(Long sensorId);
}
