package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InconsistentObjectRelationException;
import at.qe.skeleton.exceptions.SubordinateTimeRecordOutOfBoundsException;
import at.qe.skeleton.exceptions.SuperiorTimeRecordOutOfBoundsException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.SubordinateTimeRecordRepository;
import at.qe.skeleton.repositories.SuperiorTimeRecordRepository;
import at.qe.skeleton.repositories.UserxRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import org.jboss.weld.exceptions.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@Scope("application")
public class TimeRecordService {

  private final Logger logger = Logger.getLogger("TimeRecordServiceLogger");
  private final SubordinateTimeRecordRepository subordinateTimeRecordRepository;
  private final SuperiorTimeRecordRepository superiorTimeRecordRepository;
  private final UserxRepository userxRepository;

  public TimeRecordService(
      SuperiorTimeRecordRepository superiorTimeRecordRepository,
      SubordinateTimeRecordRepository subordinateTimeRecordRepository,
      UserxRepository userxRepository) {
    this.superiorTimeRecordRepository = superiorTimeRecordRepository;
    this.subordinateTimeRecordRepository = subordinateTimeRecordRepository;
    this.userxRepository = userxRepository;
  }

  public SuperiorTimeRecord findSuperiorTimeRecordByUserAndStart(Userx user, LocalDateTime start)
      throws CouldNotFindEntityException {
    SuperiorTimeRecordId id = new SuperiorTimeRecordId(start, user.getUsername());
    return superiorTimeRecordRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException("SuperiorTimeRecord %s".formatted(id)));
  }

  /**
   * this method saves a new SuperiorTimeRecord and adds the start-Time of the new TimeRecord minus
   * 1sec as the End-Time to the SuperiorTimeRecord entity with the latest start datetime before the
   * current one. Furthermore the method instantiates a SubordinateTimeRecord with the identical
   * properties and adds this to the list of Subordinate TimeRecords stored in the
   * SuperiorTimeRecords. In case this is the first SuperiorTimeRecord that was saved for User of
   * the stored TemperaStation, the method just saves the new SuperiorTimeRecord and its
   * SubordinateTimeRecord to the database.
   *
   * @param newSuperiorTimeRecord
   * @return the SuperiorTimeRecord that was newly created.
   */
  @Transactional(rollbackFor = {SuperiorTimeRecordOutOfBoundsException.class, CouldNotFindEntityException.class})
  public SuperiorTimeRecord addRecord(SuperiorTimeRecord newSuperiorTimeRecord)
      throws CouldNotFindEntityException, IOException {
    if (newSuperiorTimeRecord.getStart() == null) {
      throw new NullPointerException(
          "SuperiorTimeRecord must have a Start Time when being added to db.");
    }
    Userx user = newSuperiorTimeRecord.getUser();
    finalizeOldTimeRecord(user, newSuperiorTimeRecord);
    return saveNewTimeRecord(newSuperiorTimeRecord, user);
  }

  public SuperiorTimeRecord saveNewTimeRecord(SuperiorTimeRecord superiorTimeRecord, Userx user) {
    SubordinateTimeRecord subordinateTimeRecord =
        new SubordinateTimeRecord(superiorTimeRecord.getStart());
    subordinateTimeRecord = subordinateTimeRecordRepository.save(subordinateTimeRecord);
    logger.info("saved new %s".formatted(subordinateTimeRecord.toString()));
    superiorTimeRecord.addSubordinateTimeRecord(subordinateTimeRecord);
    logger.info("Added new %s to new %s".formatted(subordinateTimeRecord, superiorTimeRecord));
    superiorTimeRecord = superiorTimeRecordRepository.save(superiorTimeRecord);
    logger.info("saved new %s".formatted(superiorTimeRecord.toString()));
    if (superiorTimeRecordRepository.findAllByUserAndEndIsNull(user).size() > 1) {
      throw new SubordinateTimeRecordOutOfBoundsException(
          "There are more than one SuperiorTimeRecords with no End for user %s".formatted(user));
    }
    return superiorTimeRecord;
  }

  /**
   * internal Method, that retrieves the SubordinateTimeRecord form the oldSuperiorTimeRecord and
   * sets the End for both of them to one second before the start of the newSuperiorTimeRecord.
   * After that it saves both old TimeRecord entities to the database.
   */
  public void finalizeOldTimeRecord(Userx user, SuperiorTimeRecord newSuperiorTimeRecord)
      throws IOException {
    Optional<SuperiorTimeRecord> oldSuperiorTimeRecordOptional =
        findLatestSuperiorTimeRecordByUser(user);
    if (oldSuperiorTimeRecordOptional.isEmpty()) {
      logger.info("No old SuperiorTimeRecord found for user %s".formatted(user));
      return;
    }
    SuperiorTimeRecord oldSuperiorTimeRecord = oldSuperiorTimeRecordOptional.get();
    logger.info("found old SuperiorTimeRecord %s".formatted(oldSuperiorTimeRecord));

    SuperiorTimeRecordId incomingId = newSuperiorTimeRecord.getId();
    if(oldSuperiorTimeRecord.getId().equals(incomingId)){
      logger.info("incoming SuperiorTimeRecord is the same as the old one. No need to finalize");
      return;
    }


    // fetch the subordinate Timerecord of this old superior TimeRecord
    SubordinateTimeRecord oldSubordinateTimeRecord =
        oldSuperiorTimeRecord.getSubordinateRecords().get(0);
    LocalDateTime oldEnd = newSuperiorTimeRecord.getStart().minusSeconds(1);
    LocalDateTime oldStart = oldSuperiorTimeRecord.getStart();
    long durationInSeconds = ChronoUnit.SECONDS.between(oldStart, oldEnd);
    if(durationInSeconds <= 0) throw new SuperiorTimeRecordOutOfBoundsException("the new TimeRecord starts before the old one.");

    // time setting
    oldSuperiorTimeRecord.setDuration(durationInSeconds);
    oldSuperiorTimeRecord.setEnd(oldEnd);
    oldSubordinateTimeRecord.setEnd(oldEnd);
    logger.info(
        "set End TimeStamp for %s and %s to %s"
            .formatted(oldSuperiorTimeRecord, oldSubordinateTimeRecord, oldEnd));

    // persisting the Entities
    subordinateTimeRecordRepository.save(oldSubordinateTimeRecord);
    logger.info("saved %s".formatted(oldSubordinateTimeRecord.toString()));
    superiorTimeRecordRepository.save(oldSuperiorTimeRecord);
    logger.info("saved %s".formatted(oldSuperiorTimeRecord.toString()));
  }

  public void delete(SuperiorTimeRecord superiorTimeRecord) {
    superiorTimeRecordRepository.delete(superiorTimeRecord);
  }

  private SubordinateTimeRecord saveSubordinateTimeRecord(
      SubordinateTimeRecord subordinateTimeRecord) {
    return this.subordinateTimeRecordRepository.save(subordinateTimeRecord);
  }

  public Optional<SuperiorTimeRecord> findLatestSuperiorTimeRecordByUser(Userx user) {
    return superiorTimeRecordRepository.findFirstByUserAndEndIsNull(user);
  }

  public Optional<SuperiorTimeRecord> findSuperiorTimeRecordByStartAndUser(
      LocalDateTime start, Userx user) {
    return superiorTimeRecordRepository.findByUserAndId_Start(user, start);
  }
}
