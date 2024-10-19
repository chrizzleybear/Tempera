package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("application")
public class TemperaStationMapper {

    @Autowired
    TemperaStationService temperaStationService;


    public TemperaStationDto getTemperaStationDto(String username) throws CouldNotFindEntityException {
        return temperaStationService.getDtoByUsername(username).orElseThrow(
                () -> new CouldNotFindEntityException("Could not find the Tempera Station of %s".formatted(username)));
    }

}
