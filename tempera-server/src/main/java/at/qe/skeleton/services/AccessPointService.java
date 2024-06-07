package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;
import at.qe.skeleton.rest.frontend.dtos.AccessPointDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Scope("application")
public class AccessPointService {
  private final AccessPointRepository accessPointRepository;
  private final TemperaStationService temperaStationService;
  private final TemperaStationRepository temperaStationRepository;
  private final RoomService roomService;
  private final AuditLogService auditLogService;

  public AccessPointService(
          AccessPointRepository accessPointRepository, TemperaStationService temperaStationService, TemperaStationRepository temperaStationRepository, RoomService roomService, AuditLogService auditLogService) {
    this.accessPointRepository = accessPointRepository;
    this.temperaStationService = temperaStationService;
    this.temperaStationRepository = temperaStationRepository;
    this.roomService = roomService;
    this.auditLogService = auditLogService;
  }

  public AccessPoint createAccessPoint(String id, String roomId, boolean enabled, boolean isHealthy) {

    // TO-DO: generate a proper UUID
    UUID uuid = UUID.fromString(id);

    if (accessPointRepository.existsById(uuid)) {
      throw new IllegalArgumentException("AccesspointId already in use: " + id);
    }
    Room room = roomService.getRoomById(roomId);
    AccessPoint a = new AccessPoint(uuid, room, enabled, isHealthy);
    return accessPointRepository.save(a);
  }

  public AccessPoint createAccessPoint(AccessPointDto accessPointDto) {
    return createAccessPoint(
            accessPointDto.id(),
            accessPointDto.room(),
            false,
            true
    );
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

  public AccessPoint getAccessPointByRoomId(String roomId) throws CouldNotFindEntityException{
    AccessPoint a = accessPointRepository.findByRoom(roomService.getRoomById(roomId))
            .orElseThrow(() -> new CouldNotFindEntityException("Could not find an accesspoint."));
    return a;
  }

  public List<AccessPoint> getAllAccesspoints() {
    return accessPointRepository.findAll();
  }

  public List<Room> getAvailableRooms() {
    List<Room> rooms = accessPointRepository.findAll().stream().map(AccessPoint::getRoom).toList();
    return roomService.getAllRooms().stream().filter(r -> !rooms.contains(r)).toList();
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

  public AccessPoint updateAccessPoint(AccessPointDto accessPointDto) throws IllegalArgumentException {
    AccessPoint a = accessPointRepository.findById(UUID.fromString(accessPointDto.id())).orElseThrow(() -> new IllegalArgumentException("Could not find AccessPoint."));
    a.setRoom(roomService.getRoomById(accessPointDto.room()));
    a.setEnabled(accessPointDto.enabled());
    return accessPointRepository.save(a);
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
