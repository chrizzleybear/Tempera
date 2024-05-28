package at.qe.skeleton.repositories;

import at.qe.skeleton.model.InternalRecord;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.dtos.TimeTableRecordDBDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InternalRecordRepository
    extends AbstractRepository<InternalRecord, Long> {

    Optional<InternalRecord> findByStartAndExternalRecordUser(LocalDateTime start, Userx user);

    Optional<InternalRecord> findById(Long id);

    @Query("SELECT new at.qe.skeleton.model.dtos.TimeTableRecordDBDto(i.id, i.start, i.end, gxp.project.id, e.state, i.Description) FROM InternalRecord i JOIN i.externalRecord e LEFT JOIN i.groupxProject gxp WHERE e.user.username = :username")
    Set<TimeTableRecordDBDto> getTimeTableRecordDBDtoByUser(@Param("username") String username);

    Optional<InternalRecord> findByExternalRecord_EndIsNullAndExternalRecord_User(Userx user);



}
