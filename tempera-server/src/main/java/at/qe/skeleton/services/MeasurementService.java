package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorTemperaCompositeId;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@Scope("application")
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    public MeasurementService(TemperaStationRepository temperaStationRepository, MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
    }

    public Measurement loadMeasurement(Long id) throws CouldNotFindEntityException {
        return measurementRepository.findById(id).orElseThrow(() -> new CouldNotFindEntityException("Invalid Measurement ID: " + id));
    }

    public Measurement saveMeasurement(Measurement measurement) {
        return measurementRepository.save(measurement);
    }

    //delete
    public void deleteMeasurement(Measurement measurement) {
        measurementRepository.delete(measurement);
    }


    //todo: testen und informieren: wie setz ich das jetzt mit lazy loading um? will ja eine zusammenfassung der daten anzeigen aber vllt dauert es ewig alle zu laden?
    // brauchen wir auch ne umsetzung für all from sensor? und wo gehören diese Methoden hin? hier oder in den tempera bzw. sensor service?
    public List<Measurement> loadAllMeasurementsFromTempera() {
        return null;
    }



}
