package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.rest.frontend.dtos.AccessPointDto;
import at.qe.skeleton.rest.frontend.dtos.ClimateDataDto;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.ClimateDataMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.TemperaStationService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/climate_data", produces = "application/json")
public class ClimateDataController {

  private final MeasurementService measurementService;
  private final SensorService sensorService;
  private final AccessPointService accessPointService;
  private final TemperaStationService temperaStationService;
  private final ClimateDataMapper climateDataMapper;

  private static final Logger logger = Logger.getLogger("ClimateDataController");

  public ClimateDataController(
      ClimateDataMapper climateDataMapper,
      MeasurementService measurementService,
      SensorService sensorService,
      AccessPointService accessPointService,
      TemperaStationService temperaStationService) {
    this.climateDataMapper = climateDataMapper;
    this.measurementService = measurementService;
    this.sensorService = sensorService;
    this.accessPointService = accessPointService;
    this.temperaStationService = temperaStationService;
  }

  @GetMapping("/measurements/{accessPointUuid}/{temperaId}/{sensorType}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<ClimateDataDto> getMeasurementsBySensorType(
      @PathVariable UUID accessPointUuid,
      @PathVariable String temperaId,
      @PathVariable SensorType sensorType,
      Instant startDateTime,
      Instant endDateTime,
      Integer nElements) {
    try {
      AccessPoint accessPoint = accessPointService.getAccessPointById(accessPointUuid);
      boolean found = false;
      Set<TemperaStation> validStations = accessPoint.getTemperaStations();
      for (TemperaStation station : validStations) {
        if (Objects.equals(station.getId(), temperaId)) {
          found = true;
          break;
        }
      }
      if (!found) {
        final String msg =
            "No tempera station (%s) found for the selected access point (%s)"
                .formatted(temperaId, accessPointUuid);
        logger.warning(msg);
        return ResponseEntity.internalServerError().build();
      }
    } catch (CouldNotFindEntityException e) {
      final String msg =
          "No access point found under the provided UUID: %s".formatted(accessPointUuid);
      logger.warning(msg);
      return ResponseEntity.internalServerError().build();
    }
    Sensor sensor =
        sensorService.findAllSensorsByTemperaStationId(temperaId).stream()
            .filter(s -> s.getSensorType() == sensorType)
            .findFirst()
            .orElse(null);
    if (sensor == null) {
      String message = "Sensor of type '%s' not found.".formatted(sensorType);
      logger.warning(message);
      return ResponseEntity.ok().build();
    }
    MeasurementId start = new MeasurementId();
    start.setSensorId(sensor.getSensorId());
    start.setTimestamp(LocalDateTime.ofInstant(startDateTime, ZoneId.systemDefault()));
    MeasurementId end = new MeasurementId();
    end.setSensorId(sensor.getSensorId());
    end.setTimestamp(LocalDateTime.ofInstant(endDateTime, ZoneId.systemDefault()));
    List<Measurement> measurements =
        measurementService.getMeasurementsBetween(start, end).orElse(null);
    if (measurements == null || measurements.isEmpty()) {
      String message = "No measurements found for sensor %s".formatted(sensor);
      logger.warning(message);
      return ResponseEntity.ok().build();
    }

    if (startDateTime.isAfter(endDateTime)) {
      String message =
          "Start date time (%s) is after end date time (%s), when it is supposed to be before."
              .formatted(startDateTime, endDateTime);
      logger.warning(message);
      return ResponseEntity.ok().build();
    }

    if (measurements.size() > nElements) {
      int diff = measurements.size() - nElements;
      measurements = measurements.subList(diff, measurements.size());
    }

    ClimateDataDto climateDataDtos =
        climateDataMapper.mapToDto(sensor, accessPointUuid, measurements);
    String message = "Sending" + climateDataDtos.toString();
    logger.info(message);
    return ResponseEntity.ok(climateDataDtos);
  }

  @GetMapping("/measurements/access-points")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<AccessPointDto>> getEnabledAccessPoints() {
    return ResponseEntity.ok(
        this.accessPointService.getAllAccesspoints().stream()
            .filter(AccessPoint::isEnabled)
            .map(
                accessPoint -> {
                  if (accessPoint.getId() != null) {
                    return new AccessPointDto(
                        accessPoint.getId().toString(),
                        accessPoint.getRoom().toString(),
                        accessPoint.isEnabled(),
                        accessPoint.isHealthy());
                  }
                  return null;
                })
            .toList());
  }

  @GetMapping("/measurements/tempera-stations")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<TemperaStationDto>> getTemperaStations() {
    return ResponseEntity.ok(
        this.temperaStationService.getAllTemperaStations().stream()
            .filter(TemperaStationDto::enabled)
            .toList());
  }
}
