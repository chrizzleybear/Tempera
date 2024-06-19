package at.qe.skeleton.rest.raspberry.controllers;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementId;
import at.qe.skeleton.rest.raspberry.dtos.MeasurementDto;
import at.qe.skeleton.rest.raspberry.mappers.MeasurementMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
  public ResponseEntity<MeasurementDto> createMeasurement(
      @RequestBody MeasurementDto measurementDto) {
    logger.info("incoming request: createMeasurement with body: %s".formatted(measurementDto));
    try {
      if (!accessPointService.isEnabled(measurementDto.access_point_id())) {
        logger.info("accessPoint %s is not enabled".formatted(measurementDto.access_point_id()));
        return ResponseEntity.status(403).build();
      }
      if (!temperaStationService.isEnabled(measurementDto.tempera_station_id())) {
        logger.info(
            "temperaStation %s is not enabled".formatted(measurementDto.tempera_station_id()));
        return ResponseEntity.status(403).build();
      }
      List<Measurement> entities = measurementMapper.mapFromDto(measurementDto);
      List<MeasurementId> entityIds = entities.stream().map(Measurement::getId).toList();
      entities.forEach(measurementService::saveMeasurement);
      measurementService.reviewForAlerts(entityIds, measurementDto.tempera_station_id());
      return ResponseEntity.ok(measurementMapper.mapToDto(entities));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Could not map MeasurementDto to Measurement entity", e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
