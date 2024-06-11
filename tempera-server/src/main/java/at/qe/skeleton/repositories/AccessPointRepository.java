package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.TemperaStation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccessPointRepository extends AbstractRepository<AccessPoint, UUID> {

  /**
   * Finds the first AccessPoint that contains the specified TemperaStation. There should also be
   * maximum one AccessPoint that contains the specified TemperaStation.
   *
   * @param temperaStation the TemperaStation to search for
   * @return an Optional of the first AccessPoint that contains the specified TemperaStation
   */
  public Optional<AccessPoint> findFirstByTemperaStationsContains(TemperaStation temperaStation);

  public List<AccessPoint> findAllByEnabledTrue();
  public Optional<AccessPoint> findByRoom(Room room);
  boolean existsById(UUID id);
}
