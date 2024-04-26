package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.MeasurementService;
import org.springframework.stereotype.Service;

/**
 * Mapper for {@link Measurement} entities.
 */
@Service
public class MeasurementMapper implements DTOMapper<Measurement, MeasurementDto> {
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
  public MeasurementDto mapToDto(Measurement entity) throws CouldNotFindEntityException{
    if (entity == null) {
      throw new IllegalArgumentException("Measurement entity must not be null.");
    }
    if (entity.getId() == null) {
      throw new IllegalArgumentException("Measurement entity must have an id.");
    }
    if (entity.getTimestamp() == null) {
      throw new IllegalArgumentException("Measurement entity must have a timestamp.");
    }
    if (entity.getSensor() == null) {
      throw new IllegalArgumentException("Measurement entity must have a sensor.");
    }
    if (entity.getSensor().getTemperaStation() == null) {
      throw new IllegalArgumentException("Measurement entity's sensor must have a TemperaStation.");
    }
    Sensor sensor = entity.getSensor();
    TemperaStation temperaStation = sensor.getTemperaStation();
    AccessPoint accesspoint = accessPointService.getAccessPointByTemperaStationId(temperaStation.getId());
    return new MeasurementDto(
        entity.getId(),
        sensor.getId().getSensorId(),
        temperaStation.getId(),
        accesspoint.getId(),
        entity.getValue(),
        entity.getSensor().getUnit(),
        entity.getTimestamp());
  }

  @Override
  public Measurement mapFromDto(MeasurementDto dto) throws CouldNotFindEntityException {
    if (dto == null) {
      throw new IllegalArgumentException("Measurement DTO must not be null.");
    }
    Measurement measurement;
    SensorTemperaCompositeId sensorTemperaCompositeId= new SensorTemperaCompositeId();
    sensorTemperaCompositeId.setSensorId(dto.sensorId());
    sensorTemperaCompositeId.setTemperaStationId(dto.stationId());
    Sensor sensor = sensorService.findSensorById(sensorTemperaCompositeId);
    if (dto.id() != null) {
      measurement = measurementService.findMeasurementById(dto.id());
      measurement.setSensor(sensor);
      measurement.setTimestamp(dto.timestamp());
      measurement.setValue(dto.value());
      return measurement;
    }
    measurement = new Measurement(dto.value(), sensor);
    return measurement;
  }
}
