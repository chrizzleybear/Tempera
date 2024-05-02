package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.repositories.AccessPointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Scope("application")
public class AccessPointService {
  private final AccessPointRepository accessPointRepository;
  private final TemperaStationService temperaStationService;

  public AccessPointService(
      AccessPointRepository accessPointRepository, TemperaStationService temperaStationService) {
    this.accessPointRepository = accessPointRepository;
    this.temperaStationService = temperaStationService;
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
}
