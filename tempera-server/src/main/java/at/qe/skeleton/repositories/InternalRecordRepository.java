package at.qe.skeleton.repositories;

import at.qe.skeleton.model.InternalRecord;
import at.qe.skeleton.model.Userx;

import java.time.LocalDateTime;
import java.util.Optional;

public interface InternalRecordRepository
    extends AbstractRepository<InternalRecord, Long> {

    Optional<InternalRecord> findByStartAndExternalRecordUser(LocalDateTime start, Userx user);

    Optional<InternalRecord> findById(Long id);
}
