package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorTemperaCompositeId;

import java.util.List;


public interface SensorRepository extends AbstractRepository<Sensor, SensorTemperaCompositeId>{

    List<Sensor> findAllByTemperaStationId(String temperaStationId);
}
