package at.qe.skeleton.rest.raspberry.mappers;

import java.util.List;

/**
 * Interface for mapping between entity and dto objects. This interface is used for mapping multiple
 * entities to a single dto and vice versa.
 *
 * @param <e> the entity type
 * @param <d> the dto type
 */
public interface DTOMultiMapper<e, d> {
  d mapToDto(List<e> entities) throws Exception;

  List<e> mapFromDto(d dto) throws Exception;
}
