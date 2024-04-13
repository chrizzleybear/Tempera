package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.MeasurementService;

public class MeasurementMapper implements DTOMapper<Measurement, MeasurementDto> {
  private final MeasurementService measurementService;
  private final TemperaStationService temperaStationService;

  public MeasurementMapper(
      MeasurementService measurementService, TemperaStationService temperaStationService) {
    this.measurementService = measurementService;
    this.temperaStationService = temperaStationService;
  }

  @Override
  public MeasurementDto mapToDto(Measurement entity) {
    if (entity == null) {
      return null;
    }
    if (entity.getId() != null) {
        throw new IllegalArgumentException("Measurement entity must have an id.");
    }
    return null;
  }

  @Override
  public Measurement mapFromDto(MeasurementDto dto) throws CouldNotFindEntityException {
    if (dto == null) {
      return null;
    }
    if (dto.id() != null) {
      measurementService.findMeasurementById(dto.id());
    }
    return null;
  }
}
