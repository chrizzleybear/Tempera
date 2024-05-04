package at.qe.skeleton.rest.raspberry.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.repositories.SuperiorTimeRecordRepository;
import at.qe.skeleton.rest.raspberry.dtos.SuperiorTimeRecordDto;
import at.qe.skeleton.rest.raspberry.mappers.SuperiorTimeRecordMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

// code was written with workshop 5 code as template
@RestController
@RequestMapping("/rasp/api/time_record")
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

//  @GetMapping("/{id}")
//  private ResponseEntity<SuperiorTimeRecordDto> getTimeRecord(
//      @PathVariable Long id, @RequestParam UUID accessPointId) {
//    try {
//      if (!accessPointService.isEnabled(accessPointId)) {
//        throw new IllegalArgumentException(
//            "accessPoint %s is not enabled".formatted(accessPointId));
//      }
//      SuperiorTimeRecord entity = timeRecordService.findSuperiorTimeRecordById(id);
//      return ResponseEntity.ok(timeRecordMapper.mapToDto(entity));
//    } catch (Exception e) {
//      logger.log(
//          Level.SEVERE, "Could not map SuperiorTimeRecordDto to SuperiorTimeRecord entity", e);
//      return ResponseEntity.badRequest().build();
//    }
//  }

  @PostMapping("")
  private ResponseEntity<SuperiorTimeRecordDto> postTimeRecord(
      @RequestBody SuperiorTimeRecordDto timeRecordDto) {
    try {
      if (!accessPointService.isEnabled(timeRecordDto.access_point_id())) {
        throw new IllegalArgumentException(
            "accessPoint %s is not enabled".formatted(timeRecordDto.access_point_id()));
      }
      if (!temperaStationService.isEnabled(timeRecordDto.tempera_station_id())) {
        throw new IllegalArgumentException(
            "temperaStation %s is not enabled".formatted(timeRecordDto.tempera_station_id()));
      }
      logger.info("incoming time record: " + timeRecordDto + "\n");
      SuperiorTimeRecord entity =
          timeRecordService.addRecord(timeRecordMapper.mapFromDto(timeRecordDto));
      return ResponseEntity.status(201).body(timeRecordMapper.mapToDto(entity));
    } catch (CouldNotFindEntityException e) {
      logger.info(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
    catch (IOException e) {
      logger.info(e.getMessage());
      return ResponseEntity.badRequest().build();
    }

  }
}
