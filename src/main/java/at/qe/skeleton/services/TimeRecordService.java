package at.qe.skeleton.services;

import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.repositories.SuperiorTimeRecordRepository;
import org.jboss.jdeparser.FormatPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    public SuperiorTimeRecord save(SuperiorTimeRecord superiorTimeRecord) {
        logger.info("Saving superiorTimeRecord %s".formatted(superiorTimeRecord.toString()));
        return superiorTimeRecordRepository.save(superiorTimeRecord);
    }

    public void delete(SuperiorTimeRecord superiorTimeRecord) {
        superiorTimeRecordRepository.delete(superiorTimeRecord);
    }


}
