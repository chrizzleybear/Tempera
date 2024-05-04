package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SubordinateTimeRecord;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.SuperiorTimeRecordId;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SuperiorTimeRecordRepository
    extends AbstractRepository<SuperiorTimeRecord, SuperiorTimeRecordId> {

  List<SuperiorTimeRecord> findAllByState(State state);

  //    @Query("SELECT sub from SuperiorTimeRecord.subordinateRecords sub where
  // SuperiorTimeRecord.id = :id")
  //    List<SubordinateTimeRecord> findAllSubordinateRecordsById(Long id);

  List<SuperiorTimeRecord> findAllByUserAndEndIsNull(Userx user);

  Optional<SuperiorTimeRecord> findFirstByUserAndEndIsNull(Userx user);

  Optional<SuperiorTimeRecord> findByUserAndId_Start(Userx user, LocalDateTime start);

  boolean existsByUserAndId_Start(Userx user, LocalDateTime start);
}
