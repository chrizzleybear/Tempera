package at.qe.skeleton.rest.raspberry.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;

/**
 * Interface for mapping entities to DTOs. This interface should only be used when DTOs are
 * stateless and won´t be mapped back to entities. These dtos will also not be stored of course.
 */
public interface DTOMapperStateless<E, D> {
  D mapToDto(E entity) throws CouldNotFindEntityException;
}
