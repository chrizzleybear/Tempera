package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.rest.dtos.AccessPointDto;
import at.qe.skeleton.services.AccessPointService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class AccessPointMapper implements DTOMapper<AccessPoint, AccessPointDto> {

    private final AccessPointService accessPointService;

    public AccessPointMapper(AccessPointService accessPointService) {
        this.accessPointService = accessPointService;
    }

    @Override
    public AccessPointDto mapToDto(AccessPoint entity) throws CouldNotFindEntityException {
        if(entity == null) {
            throw new IllegalArgumentException("Cannot map AccessPoint to AccessPointDto, because AccessPoint is null.");
        }
        if(entity.getId() == null) {
            throw new CouldNotFindEntityException("Cannot map AccessPoint to AccessPointDto, because AccessPoint id is null.");
        }

        accessPointService.getAccessPointById()
        return null;
    }

    @Override
    public AccessPoint mapFromDto(AccessPointDto dto) throws CouldNotFindEntityException {
        return null;
    }
}
