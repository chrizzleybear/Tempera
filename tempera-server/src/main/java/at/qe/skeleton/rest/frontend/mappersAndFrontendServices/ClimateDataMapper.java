package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.rest.frontend.dtos.ClimateDataDto;
import at.qe.skeleton.rest.frontend.dtos.ClimateMeasurementDto;
import at.qe.skeleton.rest.frontend.dtos.SensorDto;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ClimateDataMapper {

  public ClimateDataDto mapToDto(Sensor sensor, UUID accessPointUuid, List<Measurement> measurements) {
    if (sensor == null || accessPointUuid == null) {
      return null;
    }
      SensorDto sensorDto = new SensorDto(
              sensor.getId().toString(),
              sensor.getSensorType().toString(),
              sensor.getUnit().toString(),
              sensor.getTemperaStation().getId()
      );
    return new ClimateDataDto(
        accessPointUuid,
        sensorDto,
        measurements.stream()
            .map(
                measurement ->
                    new ClimateMeasurementDto(measurement.getId().getTimestamp(), measurement.getValue()))
            .toList());
  }
}
