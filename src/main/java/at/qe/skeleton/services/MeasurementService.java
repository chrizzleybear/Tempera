package at.qe.skeleton.services;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;

/**
 * Service for handling measurements. Since there is a lot of overlap with SensorService,
 * this Service will handle the measurements and the sensors.
 */
public class MeasurementService {
    private final TemperaStationRepository temperaStationRepository;
    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    public MeasurementService(TemperaStationRepository temperaStationRepository, MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.temperaStationRepository = temperaStationRepository;
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    //todo: find out: what about authorizations?
    public Measurement findMostRecentBySensorId(Long sensorId) {
        return measurementRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
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
