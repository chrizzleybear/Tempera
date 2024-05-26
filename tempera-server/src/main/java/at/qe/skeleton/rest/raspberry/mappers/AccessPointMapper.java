package at.qe.skeleton.rest.raspberry.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.raspberry.dtos.AccessPointAllowedDto;
import org.springframework.stereotype.Service;

@Service
public class AccessPointMapper implements DTOMapperStateless<AccessPoint, AccessPointAllowedDto> {

  /**
   * Maps an AccessPoint entity to an AccessPointDto.
   *
   * @param entity The AccessPoint entity to be mapped.
   * @return The resulting AccessPointDto which includes whether the AccessPoint is enabled and a
   *     List of enabled TemperaStations.
   * @throws CouldNotFindEntityException If the entity is null or has no id or TemperaStations.
   */
  @Override
  public AccessPointAllowedDto mapToDto(AccessPoint entity) throws CouldNotFindEntityException {
    if (entity == null) {
      throw new IllegalArgumentException(
          "Cannot map AccessPoint to AccessPointDto, because AccessPoint is null.");
    }
    if (entity.getId() == null || entity.getId().toString().isEmpty()) {
      throw new CouldNotFindEntityException(
          "Cannot map AccessPoint to AccessPointDto, because AccessPoint id is null or empty.");
    }
    if (entity.getTemperaStations() == null) {
      throw new CouldNotFindEntityException(
          "Cannot map AccessPoint to AccessPointDto, because TemperaStations are null.");
    }

    return new AccessPointAllowedDto(
        entity.getId(),
        entity.isEnabled(),
        entity.getTemperaStations().stream()
            .filter(TemperaStation::isEnabled)
            .map(TemperaStation::getId)
            .toList());
  }
}
