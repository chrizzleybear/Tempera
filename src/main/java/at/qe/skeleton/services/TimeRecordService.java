package at.qe.skeleton.services;

import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.SuperiorTimeRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
@Component
@Scope("application")
public class TimeRecordService {

    private final Logger logger =Logger.getLogger("logger");

    private final SuperiorTimeRecordRepository superiorTimeRecordRepository;
    @Autowired
    public TimeRecordService (SuperiorTimeRecordRepository superiorTimeRecordRepository) {
        this.superiorTimeRecordRepository = superiorTimeRecordRepository;
    }

    public Optional<SuperiorTimeRecord> findSuperiorTimeRecordById(Long id) {
        return superiorTimeRecordRepository.findById(id);
    }

    public Optional<SuperiorTimeRecord> findLastSavedTimeRecord(){
        return superiorTimeRecordRepository.findFirstByOrderByStartDesc();
    }



    //todo: jetzt wird IMMER der letzte Time-Record-Endzeitpunkt verändert -> ist potentiell ein Risiko, sollte man vllt
    // einen Check einführen (nur ändern wenn Endzeitpunkt leer ist oder so)
    /**
     * this method saves a new SuperiorTimeRecord and adds the start-Time of the new TimeRecord as the End-Time to
     * the SuperiorTimeRecord entity with the latest start datetime before the current one.
     * @param newTimeRecord
     * @return the SuperiorTimeRecord that was newly created.
     */
    @Transactional
    public SuperiorTimeRecord addRecord(SuperiorTimeRecord newTimeRecord) {
        if (newTimeRecord.getStart() == null) {
            throw new NullPointerException("SuperiorTimeRecord must have a Start Time when being added to db.");
        }
        TemperaStation temperaStation = newTimeRecord.getTemperaStation();
        Optional<SuperiorTimeRecord> lastTimeRecordOptional = findLastTimeRecordByUser(temperaStation.getUser());
        if (lastTimeRecordOptional.isPresent()){
            SuperiorTimeRecord lastTimeRecord = lastTimeRecordOptional.get();
            lastTimeRecord.setEnd(newTimeRecord.getStart());
            superiorTimeRecordRepository.save(lastTimeRecord);
            logger.info("saved lastTimeRecord %s".formatted(lastTimeRecord.toString()));
        }
        logger.info("Saving newTimeRecord %s".formatted(newTimeRecord.toString()));
        return superiorTimeRecordRepository.save(newTimeRecord);
    }

    public void delete(SuperiorTimeRecord superiorTimeRecord) {
        superiorTimeRecordRepository.delete(superiorTimeRecord);
    }


    private Optional<SuperiorTimeRecord> findLastTimeRecordByUser(Userx user) {
        List<SuperiorTimeRecord> timeRecords = superiorTimeRecordRepository.findLastSavedByUser(user.getUsername());
        return timeRecords.isEmpty() ? Optional.empty() : Optional.of(timeRecords.get(0));
    }






}
