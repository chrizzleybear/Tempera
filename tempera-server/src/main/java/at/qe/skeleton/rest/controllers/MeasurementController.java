package at.qe.skeleton.rest.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.rest.mappers.MeasurementMapper;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.SensorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rasp/api/measurement")
public class MeasurementController {
  private final Logger logger = Logger.getLogger("MeasurementControllerLogger");
  private final MeasurementService measurementService;
  private final SensorService sensorService;
  private final MeasurementMapper measurementMapper;

  public MeasurementController(
      MeasurementService measurementService,
      SensorService sensorService,
      MeasurementMapper measurementMapper) {
    this.measurementService = measurementService;
    this.sensorService = sensorService;
    this.measurementMapper = measurementMapper;
  }

  @PostMapping("/create")
  public ResponseEntity<MeasurementDto> createMeasurement(@RequestBody MeasurementDto measurementDto) {
    try {
      Measurement entity = measurementMapper.mapFromDto(measurementDto);
      entity = measurementService.saveMeasurement(entity);
      return ResponseEntity.ok(measurementMapper.mapToDto(entity));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Could not map MeasurementDto to Measurement entity", e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<MeasurementDto> getMeasurement(@PathVariable Long id) {
    try {
      Measurement entity = measurementService.findMeasurementById(id);
      return ResponseEntity.ok(measurementMapper.mapToDto(entity));
    } catch (CouldNotFindEntityException e) {
      logger.log(Level.SEVERE, "Could not find Measurement with id: " + id, e);
      return ResponseEntity.notFound().build();
    }
  }
}
