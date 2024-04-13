package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Sensor;

public interface SensorRepository extends AbstractRepository<Sensor, Long>{

    Sensor findAllByTemperaStationId(String temperaStationId);
}
