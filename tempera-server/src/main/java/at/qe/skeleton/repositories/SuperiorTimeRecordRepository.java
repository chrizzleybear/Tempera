package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SubordinateTimeRecord;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SuperiorTimeRecordRepository extends AbstractRepository<SuperiorTimeRecord, Long> {

    List<SuperiorTimeRecord> findAllByState(State state);

    Optional<SuperiorTimeRecord> findFirstByOrderByStartDesc();

    @Query("SELECT sub from SuperiorTimeRecord.subordinateRecords sub where SuperiorTimeRecord.id = :id")
    List<SubordinateTimeRecord> findAllSubordinateRecordsById(Long id);


    Optional<SuperiorTimeRecord> findFirstByUserOrderByStartDesc(Userx user);
    Optional<SuperiorTimeRecord> findByStartAndUser(LocalDateTime start, Userx user);

//    Optional<SuperiorTimeRecord> findFirstByUserxOrderByStartDesc(Userx user);
}
