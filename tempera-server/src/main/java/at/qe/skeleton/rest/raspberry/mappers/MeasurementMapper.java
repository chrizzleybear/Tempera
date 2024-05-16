package at.qe.skeleton.rest.raspberry.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InconsistentObjectRelationException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.rest.raspberry.dtos.MeasurementDto;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.MeasurementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
      throws CouldNotFindEntityException, InconsistentObjectRelationException {
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
        throw new NullPointerException("Measurement entity must not be null.");
      }
      if (entity.getId() == null) {
        throw new IllegalArgumentException(
            "Measurement entity %s must have an id.".formatted(entity));
      }
      if (temperaStation == null) {
        temperaStation = entity.getSensor().getTemperaStation();
      }
      if (!temperaStation.equals(entity.getSensor().getTemperaStation())) {
        throw new InconsistentObjectRelationException(
            "All measurements must belong to the same TemperaStation.");
      }
      if (accessPoint == null) {
        accessPoint = accessPointService.getAccessPointByTemperaStationId(temperaStation.getId());
      }
      if (timestamp == null) {
        timestamp = entity.getId().getTimestamp();
      }
      if (!timestamp.equals(entity.getId().getTimestamp())) {
        throw new InconsistentObjectRelationException(
            "All measurements must have the same timestamp.");
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
  public List<Measurement> mapFromDto(MeasurementDto dto)
      throws CouldNotFindEntityException, InconsistentObjectRelationException {
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

    return getMeasurementList(dto, sensors);
  }

  /**
   * Helper method to create a list of measurements from a DTO and a list of sensors.
   *
   * @param dto
   * @param sensors
   * @return
   */
  private List<Measurement> getMeasurementList(MeasurementDto dto, List<Sensor> sensors)
      throws CouldNotFindEntityException, InconsistentObjectRelationException {
    if (sensors.size() != 4) {
      throw new IllegalArgumentException(
          "TemperaStation %s must have exactly 4 sensors.".formatted(dto.tempera_station_id()));
    }

    Measurement temperature = null;
    Measurement irradiance = null;
    Measurement humidity = null;
    Measurement nmvoc = null;

    if (dto.access_point_id()
        != accessPointService.getAccessPointByTemperaStationId(dto.tempera_station_id()).getId()) {
      throw new InconsistentObjectRelationException(
          "dto Accesspoint %s does not belong to TemperaStation %s."
              .formatted(dto.access_point_id(), dto.tempera_station_id()));
    }

    for (Sensor sensor : sensors) {
      switch (sensor.getSensorType()) {
        case TEMPERATURE:
          temperature = new Measurement(dto.temperature(), dto.timestamp(), sensor);
          break;
        case IRRADIANCE:
          irradiance = new Measurement(dto.irradiance(), dto.timestamp(), sensor);
          break;
        case HUMIDITY:
          humidity = new Measurement(dto.humidity(), dto.timestamp(), sensor);
          break;
        case NMVOC:
          nmvoc = new Measurement(dto.nmvoc(), dto.timestamp(), sensor);
          break;
      }
    }

    if (temperature == null || irradiance == null || humidity == null || nmvoc == null) {
      throw new IllegalArgumentException("All Measurements must be present.");
    }

    return List.of(temperature, irradiance, humidity, nmvoc);
  }
}
