package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.MeasurementService;

public class MeasurementMapper implements DTOMapper<Measurement, MeasurementDto> {
    private final MeasurementService measurementService;
    private final TemperaStationService temperaStationService;

    public MeasurementMapper(MeasurementService measurementService, TemperaStationService temperaStationService) {
        this.measurementService = measurementService;
        this.temperaStationService = temperaStationService;
    }
}
