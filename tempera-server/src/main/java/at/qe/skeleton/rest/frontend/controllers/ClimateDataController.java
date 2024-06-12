package at.qe.skeleton.rest.frontend.controllers;


import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.rest.frontend.dtos.ClimateDataDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.ClimateDataMapper;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.SensorService;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/climate_data", produces = "application/json")
public class ClimateDataController {

    private final
    MeasurementService measurementService;
    private final SensorService sensorService;
    private final
    ClimateDataMapper climateDataMapper;

    private static final Logger logger = Logger.getLogger("ClimateDataController");

    public ClimateDataController(ClimateDataMapper climateDataMapper, MeasurementService measurementService, SensorService sensorService) {
        this.climateDataMapper = climateDataMapper;
        this.measurementService = measurementService;
        this.sensorService = sensorService;
    }

    @GetMapping("/measurements/{accessPointUuid}/{temperaId}/{sensorType}")
  public ResponseEntity<ClimateDataDto> getMeasurementsBySensorType(
      @PathVariable UUID accessPointUuid,
      @PathVariable String temperaId,
      @PathVariable SensorType sensorType) {
    Sensor sensor =
        sensorService.findAllSensorsByTemperaStationId(temperaId).stream()
            .filter(s -> s.getSensorType() == sensorType)
            .findFirst()
            .orElse(null);
    if (sensor == null){
        String message = "Sensor of type '%s' not found.".formatted(sensorType);
        logger.warning(message);
        return ResponseEntity.ok().build();
    }
    List<Measurement> measurements = measurementService.find100LatestMeasurementsBySensor(sensor).orElse(null);
    if (measurements == null){
        String message = "No measurements found for sensor %s".formatted(sensor);
        logger.warning(message);
        return ResponseEntity.ok().build();
    }
    ClimateDataDto climateDataDtos = climateDataMapper.mapToDto(sensor, accessPointUuid, measurements);
    String message = "Sending" + climateDataDtos.toString();
    logger.info(message);
    return ResponseEntity.ok(climateDataDtos);
  }
}
