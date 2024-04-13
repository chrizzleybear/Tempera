package at.qe.skeleton.services;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.repositories.SensorRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("application")
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor findSensorById(Long id) {
        return sensorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Sensor ID: " + id));
    }

    public Sensor findAllSensorsByTemperaStationId(String temperaStationId) {
        return sensorRepository.findAllByTemperaStationId(temperaStationId);
    }

    public Sensor saveSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }
    public void deleteSensor(Sensor sensor) {
        sensorRepository.delete(sensor);
    }
}
