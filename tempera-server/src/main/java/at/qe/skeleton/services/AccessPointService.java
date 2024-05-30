package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Scope("application")
public class AccessPointService {
  private final AccessPointRepository accessPointRepository;
  private final TemperaStationService temperaStationService;
  private final TemperaStationRepository temperaStationRepository;
  private final AuditLogService auditLogService;

  public AccessPointService(
          AccessPointRepository accessPointRepository, TemperaStationService temperaStationService, TemperaStationRepository temperaStationRepository, AuditLogService auditLogService) {
    this.accessPointRepository = accessPointRepository;
    this.temperaStationService = temperaStationService;
    this.temperaStationRepository = temperaStationRepository;
    this.auditLogService = auditLogService;
  }

  public AccessPoint getAccessPointById(UUID id) throws CouldNotFindEntityException {
    if (id == null) {
      throw new IllegalArgumentException("AccessPoint id is missing.");
    }
    return accessPointRepository
        .findById(id)
        .orElseThrow(
            () -> new CouldNotFindEntityException("AccessPoint %s".formatted(id.toString())));
  }

  public AccessPoint getAccessPointByTemperaStationId(String temperaStationId)
      throws CouldNotFindEntityException {
    TemperaStation temperaStation = temperaStationService.findById(temperaStationId);
    return accessPointRepository
        .findFirstByTemperaStationsContains(temperaStation)
        .orElseThrow(
            () ->
                new CouldNotFindEntityException(
                    "AccessPoint containing TemperaStation %s".formatted(temperaStationId)));
  }

  /**
   * Checks whether the access point with the passed on ID is enabled or not.
   *
   * @param id of the access point in question
   * @throws CouldNotFindEntityException if there is no AccessPoint with that id in the DB.
   */
  public boolean isEnabled(UUID id) throws CouldNotFindEntityException {
    AccessPoint accessPoint = this.getAccessPointById(id);
    return accessPoint.isEnabled();
  }


  public void delete(AccessPoint accessPoint) {
    var tempStations = accessPoint.getTemperaStations();
    tempStations.stream().forEach(t -> t.setAccessPoint(null));
    auditLogService.logEvent(LogEvent.DELETE, LogAffectedType.ACCESS_POINT,
            "Accesspoint of room " + accessPoint.getRoom() + " was deleted.");
    accessPointRepository.delete(accessPoint);
  }

  /**
   *
   * @param temperaStationId
   * @param connectionStatus
   * @return
   * @throws CouldNotFindEntityException
   */
  public TemperaStation updateStationConnectionStatus(String temperaStationId, boolean connectionStatus) throws CouldNotFindEntityException {
    Optional<TemperaStation> queryStation =
        temperaStationRepository
            .findById(temperaStationId);

    if (queryStation.isEmpty()){
      throw new CouldNotFindEntityException(
          "Tempera station %s not found. Can't update connection status."
              .formatted(temperaStationId));
    }

    TemperaStation station = queryStation.get();
    station.setIsHealthy(connectionStatus);
    auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.ACCESS_POINT,
            "Connection status to station " + temperaStationId + "was updated to " + connectionStatus + ".");
    return temperaStationService.save(station);
  }
}
