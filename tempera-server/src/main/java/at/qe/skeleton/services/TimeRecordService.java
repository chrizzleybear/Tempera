package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.SubordinateTimeRecordOutOfBoundsException;
import at.qe.skeleton.model.SubordinateTimeRecord;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.SubordinateTimeRecordRepository;
import at.qe.skeleton.repositories.SuperiorTimeRecordRepository;
import jakarta.transaction.Transactional;
import org.jboss.weld.exceptions.IllegalArgumentException;
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
  public SuperiorTimeRecord addRecord(SuperiorTimeRecord newSuperiorTimeRecord)
      throws CouldNotFindEntityException {
    if (newSuperiorTimeRecord.getStart() == null) {
      throw new NullPointerException(
          "SuperiorTimeRecord must have a Start Time when being added to db.");
    }

    TemperaStation temperaStation = newSuperiorTimeRecord.getTemperaStation();
    Optional<SuperiorTimeRecord> oldSuperiorTimeRecordOptional =
        findLastTimeRecordByUser(temperaStation.getUser());
    if (oldSuperiorTimeRecordOptional.isEmpty()) {
      throw new CouldNotFindEntityException("old Superior Time Record could not be found in DB");
    }
    SuperiorTimeRecord oldSuperiorTimeRecord = oldSuperiorTimeRecordOptional.get();
    if (oldSuperiorTimeRecord.getSubordinateRecords().size() != 1) {
      throw new IllegalArgumentException(
          "There seem to be %d number of SubordinateTimeRecords stored in SuperiorTimeRecord %s while we expect only one"
              .formatted(
                  oldSuperiorTimeRecord.getSubordinateRecords().size(), oldSuperiorTimeRecord));
    }
    SubordinateTimeRecord oldSubordinateTimeRecord =
        oldSuperiorTimeRecord.getSubordinateRecords().get(0);
    if (oldSubordinateTimeRecord.getStart() != oldSuperiorTimeRecord.getStart()) {
      throw new SubordinateTimeRecordOutOfBoundsException(
          "Start of %s does not match start of %s"
              .formatted(oldSubordinateTimeRecord.getStart(), oldSuperiorTimeRecord.getStart()));
    }

    LocalDateTime oldStart = oldSuperiorTimeRecord.getStart();
    LocalDateTime oldEnd = newSuperiorTimeRecord.getStart().minusSeconds(1);
    LocalDateTime newStart = newSuperiorTimeRecord.getStart();
    oldSuperiorTimeRecord.setEnd(oldEnd);
    oldSubordinateTimeRecord.setEnd(oldEnd);
    logger.info(
            "set End TimeStamp for %s and %s".formatted(oldSuperiorTimeRecord, oldSubordinateTimeRecord));

    subordinateTimeRecordRepository.save(oldSubordinateTimeRecord);
    logger.info("saved %s".formatted(oldSubordinateTimeRecord.toString()));
    superiorTimeRecordRepository.save(oldSuperiorTimeRecord);
    logger.info("saved %s".formatted(oldSuperiorTimeRecord.toString()));

    SubordinateTimeRecord newSubordinateTimeRecord = new SubordinateTimeRecord(newStart);
    newSubordinateTimeRecord = subordinateTimeRecordRepository.save(newSubordinateTimeRecord);
    newSuperiorTimeRecord.addSubordinateTimeRecord(newSubordinateTimeRecord);
    logger.info("Added subordinate %s to superior %s".formatted(newSubordinateTimeRecord, newSuperiorTimeRecord));
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
