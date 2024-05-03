package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.rest.dtos.SuperiorTimeRecordDto;
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

  @Override
  public SuperiorTimeRecordDto mapToDto(SuperiorTimeRecord entity)
      throws CouldNotFindEntityException {
    if (entity == null) {
      return null;
    }
    TemperaStation temperaStation = entity.getTemperaStation();
    AccessPoint accessPoint =
        accessPointService.getAccessPointByTemperaStationId(temperaStation.getId());
    return new SuperiorTimeRecordDto(
        accessPoint.getId(),
        temperaStation.getId(),
        entity.getStart(),
        entity.getDuration(),
        entity.getState(),
        entity.getEnd() != null // if end is null, the SuperiorTimeRecord is still running
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
    // auto_update == true heißt der vorherige TimeRecord ist abgeschlossen und es handelt sich um
    // einen neuen timerecord. Wenn also auto_update false ist, müssen wir nur die Zeit updaten.
    if (!dto.auto_update()) {
      // todo: is this lazy loaded? and if so does it work?

      superiorTimeRecord =
          timeRecordService
              .findSuperiorTimeRecordByStartAndUser(dto.start(), user)
              .orElseThrow(
                  () ->
                      new CouldNotFindEntityException(
                          "SuperiorTimeRecord %s".formatted(dto.start())));
      superiorTimeRecord.setDuration(dto.duration());
    }
    else{
        superiorTimeRecord = new SuperiorTimeRecord(user, dto.start(), dto.duration(), null, dto.mode());
    }
    return superiorTimeRecord;
  }
}
