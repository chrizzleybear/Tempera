package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorTemperaCompositeId;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service for handling measurements. Since there is a lot of overlap with SensorService,
 * this Service will handle the measurements and the sensors.
 */
@Component
@Scope("application")
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    public MeasurementService(TemperaStationRepository temperaStationRepository, MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
    }

    public Measurement findMeasurementById(Long id) throws CouldNotFindEntityException {
        return measurementRepository.findById(id).orElseThrow(() -> new CouldNotFindEntityException("Invalid Measurement ID: " + id));
    }

    //todo: find out: what about authorizations?
    public Measurement findMostRecentBySensorIdAndStationId(Long sensorId, String stationId) throws CouldNotFindEntityException{
        SensorTemperaCompositeId sensorTemperaCompositeId = new SensorTemperaCompositeId();
        sensorTemperaCompositeId.setSensorId(sensorId);
        sensorTemperaCompositeId.setTemperaStationId(stationId);

        return measurementRepository.findFirstBySensorIdOrderByTimestampDesc(sensorTemperaCompositeId)
                .orElseThrow(() -> new CouldNotFindEntityException("No measurement found for sensorId: " + sensorId + " and stationId: " + stationId));
    }

    //save
    public Measurement saveMeasurement(Measurement measurement) {
        return measurementRepository.save(measurement);
    }

    //delete
    public void deleteMeasurement(Measurement measurement) {
        measurementRepository.delete(measurement);
    }




}
