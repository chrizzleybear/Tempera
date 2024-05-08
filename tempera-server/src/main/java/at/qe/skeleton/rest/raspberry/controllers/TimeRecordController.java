package at.qe.skeleton.rest.raspberry.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.ExternalRecord;
import at.qe.skeleton.rest.raspberry.dtos.ExternalRecordDto;
import at.qe.skeleton.rest.raspberry.mappers.ExternalRecordMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Logger;

// code was written with workshop 5 code as template
@RestController
@RequestMapping("/rasp/api/time_record")
public class TimeRecordController {
  private final TimeRecordService timeRecordService;
  private final ExternalRecordMapper timeRecordMapper;
  private final AccessPointService accessPointService;
  private final TemperaStationService temperaStationService;
  private final Logger logger = Logger.getLogger("TimeRecordControllerLogger");

  public TimeRecordController(
      TimeRecordService timeRecordService,
      ExternalRecordMapper timeRecordMapper,
      AccessPointService accessPointService,
      TemperaStationService temperaStationService) {
    this.timeRecordService = timeRecordService;
    this.timeRecordMapper = timeRecordMapper;
    this.accessPointService = accessPointService;
    this.temperaStationService = temperaStationService;
  }

  //  @GetMapping("/{id}")
  //  private ResponseEntity<ExternalRecordDto> getTimeRecord(
  //      @PathVariable Long id, @RequestParam UUID accessPointId) {
  //    try {
  //      if (!accessPointService.isEnabled(accessPointId)) {
  //        throw new IllegalArgumentException(
  //            "accessPoint %s is not enabled".formatted(accessPointId));
  //      }
  //      ExternalRecord entity = timeRecordService.findSuperiorTimeRecordById(id);
  //      return ResponseEntity.ok(timeRecordMapper.mapToDto(entity));
  //    } catch (Exception e) {
  //      logger.log(
  //          Level.SEVERE, "Could not map ExternalRecordDto to ExternalRecord entity", e);
  //      return ResponseEntity.badRequest().build();
  //    }
  //  }

  @PostMapping("")
  private ResponseEntity<ExternalRecordDto> postTimeRecord(
      @RequestBody ExternalRecordDto timeRecordDto) {
    try {
      if (!accessPointService.isEnabled(timeRecordDto.access_point_id())) {
        throw new IllegalArgumentException(
            "accessPoint %s is not enabled".formatted(timeRecordDto.access_point_id()));
      }
      if (!temperaStationService.isEnabled(timeRecordDto.tempera_station_id())) {
        throw new IllegalArgumentException(
            "temperaStation %s is not enabled".formatted(timeRecordDto.tempera_station_id()));
      }
      logger.info("\nincoming time record: " + timeRecordDto + ":");
      ExternalRecord entity =
          timeRecordService.addRecord(timeRecordMapper.mapFromDto(timeRecordDto));
      return ResponseEntity.status(201).body(timeRecordMapper.mapToDto(entity));
    } catch (CouldNotFindEntityException e) {
      logger.info(e.getMessage());
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      logger.info(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }
}
