package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;

//took this code from Workshop 4
public interface DTOMapper<E, D> {
    D mapToDto(E entity);
    E mapFromDto(D dto) throws CouldNotFindEntityException;
}
