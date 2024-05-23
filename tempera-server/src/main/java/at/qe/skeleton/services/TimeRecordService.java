package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InternalRecordOutOfBoundsException;
import at.qe.skeleton.exceptions.ExternalRecordOutOfBoundsException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.InternalRecordRepository;
import at.qe.skeleton.repositories.ExternalRecordRepository;
import at.qe.skeleton.repositories.UserxRepository;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@Scope("application")
public class TimeRecordService {

  private final Logger logger = Logger.getLogger("TimeRecordServiceLogger");
  private final InternalRecordRepository internalRecordRepository;
  private final ExternalRecordRepository externalRecordRepository;
  private final UserxRepository userxRepository;

  public TimeRecordService(
      ExternalRecordRepository externalRecordRepository,
      InternalRecordRepository internalRecordRepository,
      UserxRepository userxRepository) {
    this.externalRecordRepository = externalRecordRepository;
    this.internalRecordRepository = internalRecordRepository;
    this.userxRepository = userxRepository;
  }

  public ExternalRecord findSuperiorTimeRecordByUserAndStart(Userx user, LocalDateTime start)
      throws CouldNotFindEntityException {
    ExternalRecordId id = new ExternalRecordId(start, user.getUsername());
    return externalRecordRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException("ExternalRecord %s".formatted(id)));
  }

  /**
   * this method saves a new ExternalRecord and adds the start-Time of the new TimeRecord minus 1sec
   * as the End-Time to the ExternalRecord entity with the latest start datetime before the current
   * one. Furthermore the method instantiates a InternalRecord with the identical properties and
   * adds this to the list of Subordinate TimeRecords stored in the SuperiorTimeRecords. In case
   * this is the first ExternalRecord that was saved for User of the stored TemperaStation, the
   * method just saves the new ExternalRecord and its InternalRecord to the database.
   *
   * @param newExternalRecord
   * @return the ExternalRecord that was newly created.
   */
  @Transactional(
      rollbackFor = {ExternalRecordOutOfBoundsException.class, CouldNotFindEntityException.class})
  public ExternalRecord addRecord(ExternalRecord newExternalRecord)
      throws CouldNotFindEntityException, IOException {
    if (newExternalRecord.getStart() == null) {
      throw new NullPointerException(
          "ExternalRecord must have a Start Time when being added to db.");
    }
    Userx user = newExternalRecord.getUser();
    finalizeOldTimeRecord(user, newExternalRecord);
    return saveNewTimeRecord(newExternalRecord, user);
  }

  public ExternalRecord saveNewTimeRecord(ExternalRecord externalRecord, Userx user) {
    LocalDateTime start = externalRecord.getStart();

    externalRecord.getId().setStart(start);
    InternalRecord internalRecord = new InternalRecord(start);

    // setting the externalRecord for the internalRecord and vice versa.
    // if the record already exists in the db we should not save it again.
    if (findInternalRecordByStartAndUser(start, user).isPresent()) {
      externalRecord.addInternalRecord(findInternalRecordByStartAndUser(start, user).get());
    } else {
      externalRecord.addInternalRecord(internalRecord);
    }
    // saving only the external Record is sufficient since it cascades to the internalRecord
    externalRecord = externalRecordRepository.save(externalRecord);
    logger.info("saved  %s and %s".formatted(externalRecord.toString(), internalRecord.toString()));

    externalRecordRepository.findById(externalRecord.getId());
    if (externalRecordRepository.findAllByUserAndEndIsNull(user).size() > 1) {
      throw new InternalRecordOutOfBoundsException(
          "There are more than one SuperiorTimeRecords with no End for user %s".formatted(user));
    }
    return externalRecord;
  }

  /**
   * internal Method, that retrieves the InternalRecord form the oldSuperiorTimeRecord and sets the
   * End for both of them to one second before the start of the newExternalRecord. After that it
   * saves both old TimeRecord entities to the database.
   */
  public void finalizeOldTimeRecord(Userx user, ExternalRecord newExternalRecord)
      throws IOException {
    Optional<ExternalRecord> oldExternalRecordOptional = findLatestExternalRecordByUser(user);
    if (oldExternalRecordOptional.isEmpty()) {
      logger.info("No old ExternalRecord with null End found for user %s".formatted(user));
      return;
    }
    ExternalRecord oldExternalRecord = oldExternalRecordOptional.get();
    logger.info("found old ExternalRecord %s".formatted(oldExternalRecord));

    ExternalRecordId incomingId = newExternalRecord.getId();
    if (oldExternalRecord.getId().equals(incomingId)) {
      logger.info("incoming ExternalRecord is the same as the old one. No need to finalize");
      return;
    }

    // fetch the subordinate Timerecord of this old superior TimeRecord
    InternalRecord oldInternalRecord = oldExternalRecord.getInternalRecords().get(0);
    LocalDateTime oldEnd = newExternalRecord.getStart().minus(Duration.ofMillis(10));
    LocalDateTime oldStart = oldExternalRecord.getStart();
    long durationInSeconds = ChronoUnit.SECONDS.between(oldStart, oldEnd);
    if (durationInSeconds < 0) {
      throw new ExternalRecordOutOfBoundsException(
          "the old timeRecord seems to have negative duration - start: %s, end:%s"
              .formatted(oldStart.toString(), oldEnd.toString()));
    }
    // time setting
    oldExternalRecord.setDuration(durationInSeconds);
    oldExternalRecord.setEnd(oldEnd);
    oldInternalRecord.setEnd(oldEnd);
    logger.info(
        "set End TimeStamp for %s and %s to %s"
            .formatted(oldExternalRecord, oldInternalRecord, oldEnd));

    // persisting the Entities
    internalRecordRepository.save(oldInternalRecord);
    logger.info("saved %s".formatted(oldInternalRecord.toString()));
    externalRecordRepository.save(oldExternalRecord);
    logger.info("saved %s".formatted(oldExternalRecord.toString()));
  }

  public void delete(ExternalRecord externalRecord) {
    externalRecordRepository.delete(externalRecord);
  }

  public InternalRecord saveInternalRecord(InternalRecord internalRecord) {
    return this.internalRecordRepository.save(internalRecord);
  }

  public Optional<ExternalRecord> findLatestExternalRecordByUser(Userx user) {
    return externalRecordRepository.findFirstByUserAndEndIsNull(user);
  }

  public Optional<InternalRecord> findLatestInternalRecordByUser(Userx user) {
    return internalRecordRepository.findByExternalRecord_EndIsNullAndExternalRecord_User(user);
  }

  public Optional<ExternalRecord> findExternalRecordByStartAndUser(
      LocalDateTime start, Userx user) {
    return externalRecordRepository.findByUserAndId_Start(user, start);
  }

  public Optional<InternalRecord> findInternalRecordByStartAndUser(
      LocalDateTime start, Userx user) {
    return internalRecordRepository.findByStartAndExternalRecordUser(start, user);
  }

  public List<InternalRecord> getInternalRecordsForUser(Userx user) {
    return internalRecordRepository.findAllByUserOrderByStartDesc(user);
  }

  public Optional<InternalRecord> findInternalRecordById(Long id) {
    return internalRecordRepository.findById(id);
  }

  public List<ExternalRecord> findAllExternalTimeRecordsByUser(Userx user) {
    return externalRecordRepository.findAllByUser(user);
  }
}
