package at.qe.skeleton.services;

import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.repositories.TemperaStationRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Logger;

@Component
@Scope("application")
public class TemperaStationService {

    //todo: now these logging events are just for debugging and checking if it works -
    // should be replaced with the real auditLog.

    private final Logger logger =Logger.getLogger("logger");

    private TemperaStationRepository temperaStationRepository;

    public TemperaStationService(TemperaStationRepository temperaStationRepository) {
        this.temperaStationRepository = temperaStationRepository;
    }

    public Optional<TemperaStation> findById(String id){
        logger.info("trying to find TemperaStation with Id: %s".formatted(id));
        return temperaStationRepository.findById(id);
    }

    public TemperaStation save(TemperaStation temperaStation){
        logger.info("trying to save temperastation %s".formatted(temperaStation.toString()));
        return temperaStationRepository.save(temperaStation);
    }

    public void delete(TemperaStation temperaStation){
        logger.info("trying to delete temperaStation %s".formatted(temperaStation.toString()));
    }
}
