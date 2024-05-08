package at.qe.skeleton.repositories;

import at.qe.skeleton.model.ExternalRecord;
import at.qe.skeleton.model.ExternalRecordId;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExternalRecordRepository
    extends AbstractRepository<ExternalRecord, ExternalRecordId> {

  List<ExternalRecord> findAllByState(State state);

  //    @Query("SELECT sub from ExternalRecord.subordinateRecords sub where
  // ExternalRecord.id = :id")
  //    List<InternalRecord> findAllSubordinateRecordsById(Long id);

  List<ExternalRecord> findAllByUserAndEndIsNull(Userx user);

  Optional<ExternalRecord> findFirstByUserAndEndIsNull(Userx user);

  Optional<ExternalRecord> findByUserAndId_Start(Userx user, LocalDateTime start);

  boolean existsByUserAndId_Start(Userx user, LocalDateTime start);
}
