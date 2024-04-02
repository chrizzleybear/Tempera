package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.dtos.SuperiorTimeRecordDto;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.stereotype.Service;

// got large parts of this class from Workshop 4
@Service
public class SuperiorTimeRecordMapper
    implements DTOMapper<SuperiorTimeRecord, SuperiorTimeRecordDto> {

  private final TimeRecordService timeRecordService;
  private final TemperaStationService temperaStationService;

  public SuperiorTimeRecordMapper(
      TimeRecordService timeRecordService, TemperaStationService temperaStationService) {
    this.timeRecordService = timeRecordService;
    this.temperaStationService = temperaStationService;
  }

  @Override
  public SuperiorTimeRecordDto mapToDto(SuperiorTimeRecord entity) {
    if (entity == null) {
      return null;
    }
    return new SuperiorTimeRecordDto(
        entity.getId(),
        entity.getTemperaStation().getId(),
        entity.getStart(),
        entity.getEnd(),
        entity.getState());
  }

  /**
   * This method maps a DTO (received via REST Controller) to an SuperiorTimeRecord Entity. If the
   * Controller receives a Rest-Call which includes an Id for the SuperiorTimeRecord, then it
   * searches the DB for this id. if it is not existent, it will throw an Exception. If the
   * SuperiorTimeRecord is new, it should therefor not include an Id.
   *
   * @param dto which will be mapped to an entity
   * @return SuperiorTimeRecord Entity
   * @throws CouldNotFindEntityException if it either cant find a SuperiorTimeRecord Entity with the
   *     given id (if given) or a TemperaStation with the provided StationId.
   */
  @Override
  public SuperiorTimeRecord mapFromDto(SuperiorTimeRecordDto dto)
      throws CouldNotFindEntityException {
    if (dto == null) {
      return null;
    }
    SuperiorTimeRecord entity = null;
    if (dto.Id() != null) {
      entity =
          timeRecordService
              .findSuperiorTimeRecordById(dto.Id())
              .orElseThrow(
                  () ->
                      new CouldNotFindEntityException(
                          "could not find SuperiorTimeRecord with the id %d".formatted(dto.Id())));
      return entity;
    }

    // todo: is it better to just store the id in the SuperiorTimeRecord? because now i have to
    // search for the
    // tempera station here.. dont know if this is the best place..
    TemperaStation temperaStation =
        temperaStationService
            .findById(dto.stationId())
            .orElseThrow(
                () ->
                    new CouldNotFindEntityException(
                        "could not find TemperaStation with Id %s".formatted(dto.stationId())));
    return new SuperiorTimeRecord(temperaStation, dto.start(), dto.end(), dto.state());
  }
}
