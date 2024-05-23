package at.qe.skeleton.repositories;

import at.qe.skeleton.model.ExternalRecord;
import at.qe.skeleton.model.ExternalRecordId;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.rest.frontend.dtos.UserStateDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  @Query("SELECT DISTINCT e.user.username, e.state FROM ExternalRecord e WHERE e.user IN :users")
  List<UserStateDto> findUserStatesByUserList(@Param("users") List<Userx> users);

  boolean existsByUserAndId_Start(Userx user, LocalDateTime start);

  void deleteAllByUser(Userx userx);

  List<ExternalRecord> findAllByUser(Userx user);
}
