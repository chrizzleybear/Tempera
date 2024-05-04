package at.qe.skeleton.rest.raspberry.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.rest.dtos.SuperiorTimeRecordDto;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.raspberry.dtos.SuperiorTimeRecordDto;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.stereotype.Service;

// got large parts of this class from Workshop 4
@Service
public class SuperiorTimeRecordMapper
    implements DTOMapper<SuperiorTimeRecord, SuperiorTimeRecordDto> {

  private final TimeRecordService timeRecordService;
  private final TemperaStationService temperaStationService;
  private final AccessPointService accessPointService;

  public SuperiorTimeRecordMapper(
      TimeRecordService timeRecordService,
      TemperaStationService temperaStationService,
      AccessPointService accessPointService) {
    this.accessPointService = accessPointService;
    this.timeRecordService = timeRecordService;
    this.temperaStationService = temperaStationService;
  }

  /**
   * returns a SuperiorTimeRecordDto from a SuperiorTimeRecord Entity. Auto_update will always be set to false.
   * @param entity
   * @return
   * @throws CouldNotFindEntityException
   */
  @Override
  public SuperiorTimeRecordDto mapToDto(SuperiorTimeRecord entity)
      throws CouldNotFindEntityException {
    if (entity == null) {
      return null;
    }
    Userx user = entity.getUser();
    TemperaStation temperaStation = temperaStationService.findByUser(user).orElseThrow(()-> new CouldNotFindEntityException("TemperaStation %s".formatted(user.getUsername())));
    AccessPoint accessPoint =
        accessPointService.getAccessPointByTemperaStationId(temperaStation.getId());
    return new SuperiorTimeRecordDto(
        accessPoint.getId(),
        temperaStation.getId(),
        entity.getStart(),
        entity.getDuration(),
        entity.getState(),
        false
        );
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
    TemperaStation temperaStation = temperaStationService.findById(dto.tempera_station_id());
    Userx user = temperaStation.getUser();
    SuperiorTimeRecord superiorTimeRecord;
    // auto_update == false heißt der vorherige TimeRecord ist abgeschlossen und es handelt sich um
    // einen neuen timerecord. Wenn also auto_update true ist, müssen wir nur die Zeit updaten.
    if (dto.auto_update()) {
      // todo: is this lazy loaded?

      superiorTimeRecord =
          timeRecordService
              .findSuperiorTimeRecordByStartAndUser(dto.start(), user)
              .orElseThrow(
                  () ->
                      new CouldNotFindEntityException(
                          "SuperiorTimeRecord %s has set auto_update to true but did not exist in db".formatted(dto.start())));
      superiorTimeRecord.setDuration(dto.duration());
    }
    else{
        superiorTimeRecord = new SuperiorTimeRecord(user, dto.start(), dto.duration(), null, dto.mode());
    }
    return superiorTimeRecord;
  }
}
