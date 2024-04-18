package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorTemperaCompositeId;

public interface SensorRepository extends AbstractRepository<Sensor, SensorTemperaCompositeId>{

    Sensor findAllByTemperaStationId(String temperaStationId);
}
