package at.qe.skeleton.rest.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.repositories.SuperiorTimeRecordRepository;
import at.qe.skeleton.rest.dtos.SuperiorTimeRecordDto;
import at.qe.skeleton.rest.mappers.SuperiorTimeRecordMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

// code was written with workshop 5 code as template
@RestController
@RequestMapping("/rasp/api/timerecord")
public class TimeRecordController {
  private final TimeRecordService timeRecordService;
  private final SuperiorTimeRecordMapper timeRecordMapper;
  private final AccessPointService accessPointService;
  private final TemperaStationService temperaStationService;
  private final Logger logger = Logger.getLogger("TimeRecordControllerLogger");

  public TimeRecordController(
      TimeRecordService timeRecordService,
      SuperiorTimeRecordMapper timeRecordMapper,
      AccessPointService accessPointService,
      TemperaStationService temperaStationService) {
    this.timeRecordService = timeRecordService;
    this.timeRecordMapper = timeRecordMapper;
    this.accessPointService = accessPointService;
    this.temperaStationService = temperaStationService;
  }

  @GetMapping("/{id}")
  private ResponseEntity<SuperiorTimeRecordDto> getTimeRecord(
      @PathVariable Long id, @RequestParam UUID accessPointId) {
    try {
      if (!accessPointService.isEnabled(accessPointId)) {
        throw new IllegalArgumentException(
            "accessPoint %s is not enabled".formatted(accessPointId));
      }
      SuperiorTimeRecord entity = timeRecordService.findSuperiorTimeRecordById(id);
      return ResponseEntity.ok(timeRecordMapper.mapToDto(entity));
    } catch (Exception e) {
      logger.log(
          Level.SEVERE, "Could not map SuperiorTimeRecordDto to SuperiorTimeRecord entity", e);
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("")
  private ResponseEntity<SuperiorTimeRecordDto> postTimeRecord(
      @RequestBody SuperiorTimeRecordDto timeRecordDto) {
    try {
      if (!accessPointService.isEnabled(timeRecordDto.accessPointId())) {
        throw new IllegalArgumentException(
            "accessPoint %s is not enabled".formatted(timeRecordDto.accessPointId()));
      }
      if (!temperaStationService.isEnabled(timeRecordDto.stationId())) {
        throw new IllegalArgumentException(
            "temperaStation %s is not enabled".formatted(timeRecordDto.stationId()));
      }
      SuperiorTimeRecord entity =
          timeRecordService.addRecord(timeRecordMapper.mapFromDto(timeRecordDto));
      return ResponseEntity.status(201).body(timeRecordMapper.mapToDto(entity));
    } catch (CouldNotFindEntityException e) {
      return ResponseEntity.badRequest().build();
    }
  }

}
