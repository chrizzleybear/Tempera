package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.dtos.AccessPointDto;
import at.qe.skeleton.services.AccessPointService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccessPointMapper implements DTOMapperStateless<AccessPoint, AccessPointDto> {

  /**
   * Maps an AccessPoint entity to an AccessPointDto.
   *
   * @param entity The AccessPoint entity to be mapped.
   * @return The resulting AccessPointDto which includes whether the AccessPoint is enabled and a List of enabled TemperaStations.
   * @throws CouldNotFindEntityException If the entity is null or has no id or TemperaStations.
   */
  @Override
  public AccessPointDto mapToDto(AccessPoint entity) throws CouldNotFindEntityException {
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

    return new AccessPointDto(
        entity.getId(),
        entity.isEnabled(),
        entity.getTemperaStations().stream().filter(TemperaStation::isEnabled).toList());
  }
}
