package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TemperaStationMapper {

    @Autowired
    TemperaStationService temperaStationService;



    public TemperaStationDto getTemperaStationDtoByUsername(String username) throws CouldNotFindEntityException{

        Optional<TemperaStation> tempOpt = temperaStationService.findByUsername(username);
        TemperaStationDto dto;
        if(tempOpt.isPresent()) {
             dto = mapTemperaStationToDto(tempOpt.get());
        } else {
            throw new CouldNotFindEntityException("temperaStation Could not be found");
        }
        return dto;
    }


    /**
     * The problem with this kind of db-retrieval and mapping is that i need to load
     * way more information then i need (e.g. the AccessPoint Object) while also
     * exposing myself to the risk, that i cant load the AccessPoint because the entity is
     * detached or so.
     * @param temperaStation
     * @return
     */
    private TemperaStationDto mapTemperaStationToDto(TemperaStation temperaStation) {
        return new TemperaStationDto(
                temperaStation.getId(),
                temperaStation.getUser().getUsername(),
                temperaStation.isEnabled(),
                temperaStation.isHealthy(),
                temperaStation.getAccessPoint().getId().toString());
    }

}
