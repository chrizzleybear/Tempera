package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorId;

import java.util.List;

public interface SensorRepository extends AbstractRepository<Sensor, SensorId> {

  List<Sensor> findAllByTemperaStationId(String temperaStationId);
}
