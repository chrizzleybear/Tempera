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
  public MeasurementDto mapToDto(List<Measurement> measurements)
      throws CouldNotFindEntityException {
    if (measurements == null) {
      throw new IllegalArgumentException("Measurements must not be null.");
    }
    if (measurements.size() != 4) {
      throw new IllegalArgumentException("Measurements must have exactly 4 entities.");
    }
    TemperaStation temperaStation = null;
    LocalDateTime timestamp = null;
    AccessPoint accessPoint = null;

    Measurement temperature = null;
    Measurement irradiance = null;
    Measurement humidity = null;
    Measurement nmvoc = null;

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

      switch (entity.getSensor().getSensorType()) {
        case TEMPERATURE:
          temperature = entity;
          break;
        case IRRADIANCE:
          irradiance = entity;
          break;
        case HUMIDITY:
          humidity = entity;
          break;
        case NMVOC:
          nmvoc = entity;
          break;
        default:
          throw new IllegalArgumentException(
              "Sensor %s must have a valid sensorType.".formatted(entity.getSensor()));
      }
    }
    if (temperature == null || irradiance == null || humidity == null || nmvoc == null) {
      throw new IllegalArgumentException("Measurements must have a temperature.");
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

    if (dto.timestamp() == null) {
      throw new IllegalArgumentException("Measurement DTO must have a timestamp.");
    }
    if (dto.temperature() == null) {
      throw new IllegalArgumentException("Measurement DTO must have a temperature.");
    }
    if (dto.humidity() == null) {
      throw new IllegalArgumentException("Measurement DTO must have a humidity.");
    }
    if (dto.irradiance() == null) {
      throw new IllegalArgumentException("Measurement DTO must have an irradiance.");
    }
    if (dto.nmvoc() == null) {
      throw new IllegalArgumentException("Measurement DTO must have an nmvoc.");
    }
    if (dto.tempera_station_id() == null) {
      throw new IllegalArgumentException("Measurement DTO must have a TemperaStation id.");
    }

    List<Sensor> sensors =
        (sensorService.findAllSensorsByTemperaStationId(dto.tempera_station_id()));
    if (sensors.size() != 4) {
      throw new IllegalArgumentException(
          "TemperaStation %s must have exactly 4 sensors.".formatted(dto.tempera_station_id()));
    }
    Measurement temperature = new Measurement();
    Measurement irradiance = new Measurement();
    Measurement humidity = new Measurement();
    Measurement nmvoc = new Measurement();

    for (Sensor sensor : sensors) {
      switch (sensor.getSensorType()) {
        case TEMPERATURE:
          temperature.setSensor(sensor);
          temperature.setValue(dto.temperature());
          temperature.setTimestamp(dto.timestamp());
          break;
        case IRRADIANCE:
          irradiance.setSensor(sensor);
          irradiance.setValue(dto.irradiance());
          irradiance.setTimestamp(dto.timestamp());
          break;
        case HUMIDITY:
          humidity.setSensor(sensor);
          humidity.setValue(dto.humidity());
          humidity.setTimestamp(dto.timestamp());
          break;
        case NMVOC:
          nmvoc.setSensor(sensor);
          nmvoc.setValue(dto.nmvoc());
          nmvoc.setTimestamp(dto.timestamp());
          break;
        default:
          throw new IllegalArgumentException("Sensor must have a valid unit.");
      }
    }
    List<Measurement> measurements = List.of(temperature, irradiance, humidity, nmvoc);
    for (Measurement entity : measurements) {
      if (entity.getSensor() == null) {
        throw new IllegalArgumentException(
            "Measurement entity %s must have a sensor.".formatted(entity));
      }
    }
    return measurements;
  }
}
