package at.qe.skeleton.rest.raspberry.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.ExternalRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.raspberry.dtos.ExternalRecordDto;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.stereotype.Service;

// got large parts of this class from Workshop 4
@Service
public class ExternalRecordMapper
    implements DTOMapper<ExternalRecord, ExternalRecordDto> {

  private final TimeRecordService timeRecordService;
  private final TemperaStationService temperaStationService;
  private final AccessPointService accessPointService;

  public ExternalRecordMapper(
      TimeRecordService timeRecordService,
      TemperaStationService temperaStationService,
      AccessPointService accessPointService) {
    this.accessPointService = accessPointService;
    this.timeRecordService = timeRecordService;
    this.temperaStationService = temperaStationService;
  }

  /**
   * returns a ExternalRecordDto from a ExternalRecord Entity. Auto_update will always be
   * set to false.
   *
   * @param entity
   * @return
   * @throws CouldNotFindEntityException
   */
  @Override
  public ExternalRecordDto mapToDto(ExternalRecord entity)
      throws CouldNotFindEntityException {
    if (entity == null) {
      return null;
    }
    Userx user = entity.getUser();
    TemperaStation temperaStation =
        temperaStationService
            .findByUser(user)
            .orElseThrow(
                () ->
                    new CouldNotFindEntityException(
                        "TemperaStation %s".formatted(user.getUsername())));
    AccessPoint accessPoint =
        accessPointService.getAccessPointByTemperaStationId(temperaStation.getId());
    return new ExternalRecordDto(
        accessPoint.getId(),
        temperaStation.getId(),
        entity.getStart(),
        entity.getDuration(),
        entity.getState(),
        false);
  }

  /**
   * This method maps a DTO (received via REST Controller) to an ExternalRecord Entity. If the
   * Controller receives a Rest-Call which includes an Id for the ExternalRecord, then it
   * searches the DB for this id. if it is not existent, it will throw an Exception. If the
   * ExternalRecord is new, it should therefor not include an Id.
   *
   * @param dto which will be mapped to an entity
   * @return ExternalRecord Entity
   * @throws CouldNotFindEntityException if it either cant find a ExternalRecord Entity with the
   *     given id (if given) or a TemperaStation with the provided StationId.
   */
  @Override
  public ExternalRecord mapFromDto(ExternalRecordDto dto)
      throws CouldNotFindEntityException {
    if (dto == null) {
      return null;
    }
    TemperaStation temperaStation = temperaStationService.findById(dto.tempera_station_id());
    Userx user = temperaStation.getUser();
    ExternalRecord externalRecord;
    if (dto.auto_update()) {
      // todo: is this lazy loaded?
      externalRecord =
          timeRecordService
              .findExternalRecordByStartAndUser(dto.start(), user)
              .orElseThrow(
                  () ->
                      new CouldNotFindEntityException(
                          "ExternalRecord %s has set auto_update to true but did not exist in db"
                              .formatted(dto.start())));
      externalRecord.setDuration(dto.duration());
    } else {
      externalRecord =
          new ExternalRecord(user, dto.start(), dto.duration(), null, dto.mode());
    }
    return externalRecord;
  }
}
