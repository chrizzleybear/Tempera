package at.qe.skeleton.rest.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.rest.mappers.MeasurementMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.TemperaStationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rasp/api/measurement")
public class MeasurementController {
  private final Logger logger = Logger.getLogger("MeasurementControllerLogger");
  private final MeasurementService measurementService;
  private final MeasurementMapper measurementMapper;
  private final AccessPointService accessPointService;
  private final TemperaStationService temperaStationService;

  public MeasurementController(
      MeasurementService measurementService,
      MeasurementMapper measurementMapper,
      AccessPointService accessPointService,
      TemperaStationService temperaStationService) {
    this.accessPointService = accessPointService;
    this.measurementService = measurementService;
    this.measurementMapper = measurementMapper;
    this.temperaStationService = temperaStationService;
  }

  @PostMapping("")
  public ResponseEntity<MeasurementDto> createMeasurement(@RequestBody MeasurementDto measurementDto) {
    try {
      if (!accessPointService.isEnabled(measurementDto.access_point_id())){
        throw new IllegalArgumentException("accessPoint %s is not enabled".formatted(measurementDto.access_point_id()));
      }
      if (!temperaStationService.isEnabled(measurementDto.tempera_station_id())){
        throw new IllegalArgumentException("temperaStation %s is not enabled".formatted(measurementDto.tempera_station_id()));
      }
      List<Measurement> entities = measurementMapper.mapFromDto(measurementDto);
      entities.forEach(measurementService::saveMeasurement);
      return ResponseEntity.ok(measurementMapper.mapToDto(entities));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Could not map MeasurementDto to Measurement entity", e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  //todo: seems like we dont need put, get or delete methods for this controller - is that a problem?
}
