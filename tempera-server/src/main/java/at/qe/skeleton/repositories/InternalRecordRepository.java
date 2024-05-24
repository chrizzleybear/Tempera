package at.qe.skeleton.repositories;

import at.qe.skeleton.model.InternalRecord;
import at.qe.skeleton.model.Userx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InternalRecordRepository
    extends AbstractRepository<InternalRecord, Long> {

    Optional<InternalRecord> findByStartAndExternalRecordUser(LocalDateTime start, Userx user);

    Optional<InternalRecord> findById(Long id);

    @Query("SELECT i FROM InternalRecord i JOIN i.externalRecord e WHERE e.user = :user ORDER BY i.start DESC")
    List<InternalRecord> findAllByUserOrderByStartDesc(@Param("user") Userx user);

    Optional<InternalRecord> findByExternalRecord_EndIsNullAndExternalRecord_User(Userx user);

}
