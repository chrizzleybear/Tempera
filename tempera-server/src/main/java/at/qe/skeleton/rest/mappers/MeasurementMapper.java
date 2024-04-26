package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.MeasurementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static at.qe.skeleton.model.enums.SensorType.*;

/** Mapper for {@link Measurement} entities. */
@Service
public class MeasurementMapper implements DTOMultiMapper<Measurement, MeasurementDto> {
  private final MeasurementService measurementService;
  private final SensorService sensorService;
  private final AccessPointService accessPointService;

  public MeasurementMapper(
      MeasurementService measurementService,
      SensorService sensorService,
      AccessPointService accessPointService) {
    this.measurementService = measurementService;
    this.sensorService = sensorService;
    this.accessPointService = accessPointService;
  }

  @Override
  public MeasurementDto mapToDto(
      Measurement temperature, Measurement irradiance, Measurement humidity, Measurement nmvoc)
      throws CouldNotFindEntityException {
    List<Measurement> measurements = List.of(temperature, irradiance, humidity, nmvoc);
    TemperaStation temperaStation = null;
    LocalDateTime timestamp = null;
    AccessPoint accessPoint = null;
    for (Measurement entity : measurements) {
      if (entity == null) {
        throw new IllegalArgumentException(
            "Measurement entity %s must not be null.".formatted(entity));
      }
      if (entity.getId() == null) {
        throw new IllegalArgumentException(
            "Measurement entity %s must have an id.".formatted(entity));
      }
      if (entity.getTimestamp() == null) {
        throw new IllegalArgumentException(
            "Measurement entity %s must have a timestamp.".formatted(entity));
      }
      if (entity.getSensor() == null) {
        throw new IllegalArgumentException(
            "Measurement entity %s must have a sensor.".formatted(entity));
      }
      if (entity.getSensor().getTemperaStation() == null) {
        throw new IllegalArgumentException(
            "Measurement entity's %s sensor must have a TemperaStation."
                .formatted(entity.getSensor().getTemperaStation()));
      }
      if (temperaStation == null) {
        temperaStation = entity.getSensor().getTemperaStation();
      }
      if (!temperaStation.equals(entity.getSensor().getTemperaStation())) {
        throw new IllegalArgumentException(
            "All measurements must belong to the same TemperaStation.");
      }
      if (accessPoint == null) {
        accessPoint = accessPointService.getAccessPointByTemperaStationId(temperaStation.getId());
      }
      if (timestamp == null) {
        timestamp = entity.getTimestamp();
      }
      if (!timestamp.equals(entity.getTimestamp())) {
        throw new IllegalArgumentException("All measurements must have the same timestamp.");
      }
    }
    return new MeasurementDto(
        accessPoint.getId(),
        temperaStation.getId(),
        timestamp,
        temperature.getValue(),
        irradiance.getValue(),
        humidity.getValue(),
        nmvoc.getValue());
  }

  @Override
  public List<Measurement> mapFromDto(MeasurementDto dto) throws CouldNotFindEntityException {
    if (dto == null) {
      throw new IllegalArgumentException("Measurement DTO must not be null.");
    }
    List<Sensor> sensors = (sensorService.findAllSensorsByTemperaStationId(dto.tempera_station_id()));
    if (sensors.size() != 4) {
      throw new IllegalArgumentException(
          "TemperaStation %s must have exactly 4 sensors.".formatted(dto.tempera_station_id()));
    }
    Measurement temperature = new Measurement();
    Measurement irradiance = new Measurement();
    Measurement humidity = new Measurement();
    Measurement nmvoc = new Measurement();
    for (Sensor sensor : sensors) {
      if (sensor == null) {
        throw new IllegalArgumentException("Sensor must not be null.");
      }
      if (sensor.getUnit() == null) {
        throw new IllegalArgumentException("Sensor must have a unit.");
      }
      switch (sensor.getSensorType()) {
        case TEMPERATURE:
          //todo: continue...
          temperature.
          break;
        case IRRADIANCE:
          sensor.setValue(dto.irradiance());
          break;
        case HUMIDITY:
          sensor.setValue(dto.humidity());
          break;
        case NMVOC:
          sensor.setValue(dto.nmvoc());
          break;
        default:
          throw new IllegalArgumentException("Sensor must have a valid unit.");
      }
    }
    Measurement measurement = new Measurement();
    measurement.setTimestamp(dto.timestamp());
  }
}
