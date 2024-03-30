package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.enums.State;

import java.util.List;
import java.util.Optional;

public interface SuperiorTimeRecordRepository extends AbstractRepository<SuperiorTimeRecord, Long> {

    List<SuperiorTimeRecord> findAllByState(State state);

    Optional<SuperiorTimeRecord> findFirstByOrderByStartDesc();
}
