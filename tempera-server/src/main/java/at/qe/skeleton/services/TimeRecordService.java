package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.SubordinateTimeRecord;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.SubordinateTimeRecordRepository;
import at.qe.skeleton.repositories.SuperiorTimeRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@Scope("application")
public class TimeRecordService {

  private final Logger logger = Logger.getLogger("logger");
  private final SubordinateTimeRecordRepository subordinateTimeRecordRepository;
  private final SuperiorTimeRecordRepository superiorTimeRecordRepository;

  @Autowired
  public TimeRecordService(
      SuperiorTimeRecordRepository superiorTimeRecordRepository,
      SubordinateTimeRecordRepository subordinateTimeRecordRepository) {
    this.superiorTimeRecordRepository = superiorTimeRecordRepository;
    this.subordinateTimeRecordRepository = subordinateTimeRecordRepository;
  }

  public SuperiorTimeRecord findSuperiorTimeRecordById(Long id) throws CouldNotFindEntityException {
    return superiorTimeRecordRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException("SuperiorTimeRecord %s".formatted(id)));
  }

  public Optional<SuperiorTimeRecord> findLastSavedTimeRecord() {
    return superiorTimeRecordRepository.findFirstByOrderByStartDesc();
  }

  // todo: subordinate bereits parallel zum neuen Superior TR anlegen, da user bereits im aktuellen
  // TR ein Projekt zuordnen kann
  /**
   * this method saves a new SuperiorTimeRecord and adds the start-Time of the new TimeRecord minus
   * 1sec as the End-Time to the SuperiorTimeRecord entity with the latest start datetime before the
   * current one. Furthermore the method instantiates a SubordinateTimeRecord with the identical
   * properties and adds this to the list of Subordinate TimeRecords stored in the
   * SuperiorTimeRecords.
   *
   * @param newSuperiorTimeRecord
   * @return the SuperiorTimeRecord that was newly created.
   */
  @Transactional
  public SuperiorTimeRecord addRecord(SuperiorTimeRecord newSuperiorTimeRecord) {
    if (newSuperiorTimeRecord.getStart() == null) {
      throw new NullPointerException(
          "SuperiorTimeRecord must have a Start Time when being added to db.");
    }

    TemperaStation temperaStation = newSuperiorTimeRecord.getTemperaStation();
    Optional<SuperiorTimeRecord> lastTimeRecordOptional =
        findLastTimeRecordByUser(temperaStation.getUser());
    if (lastTimeRecordOptional.isPresent()) {
      SuperiorTimeRecord lastTimeRecord = lastTimeRecordOptional.get();

      LocalDateTime oldStart = lastTimeRecord.getStart();
      LocalDateTime oldEnd = newSuperiorTimeRecord.getStart().minusSeconds(1);

      SubordinateTimeRecord subordinateTimeRecord = new SubordinateTimeRecord(oldStart, oldEnd);
      saveSubordinateTimeRecord(subordinateTimeRecord);
      logger.info("saved SubordinateTimeRecord %s".formatted(subordinateTimeRecord));

      lastTimeRecord.setEnd(oldEnd);
      lastTimeRecord.addSubordinateTimeRecord(subordinateTimeRecord);
      logger.info(
          "added SubordinateTimeRecord %s to SuperiorTimeRecord %s"
              .formatted(subordinateTimeRecord, lastTimeRecord));
      superiorTimeRecordRepository.save(lastTimeRecord);
      logger.info("saved last SuperiorTimeRecord %s".formatted(lastTimeRecord.toString()));
    }

    newSuperiorTimeRecord = superiorTimeRecordRepository.save(newSuperiorTimeRecord);
    logger.info("Saved new SuperiorTimeRecord %s".formatted(newSuperiorTimeRecord));

    return newSuperiorTimeRecord;
  }

  public void delete(SuperiorTimeRecord superiorTimeRecord) {
    superiorTimeRecordRepository.delete(superiorTimeRecord);
  }

  private SubordinateTimeRecord saveSubordinateTimeRecord(
      SubordinateTimeRecord subordinateTimeRecord) {
    return this.subordinateTimeRecordRepository.save(subordinateTimeRecord);
  }

  private Optional<SuperiorTimeRecord> findLastTimeRecordByUser(Userx user) {
    List<SuperiorTimeRecord> timeRecords =
        superiorTimeRecordRepository.findLastSavedByUser(user.getUsername());
    return timeRecords.isEmpty() ? Optional.empty() : Optional.of(timeRecords.get(0));
  }
}
