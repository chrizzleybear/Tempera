package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.enums.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SuperiorTimeRecordRepository extends AbstractRepository<SuperiorTimeRecord, Long> {

    List<SuperiorTimeRecord> findAllByState(State state);

    Optional<SuperiorTimeRecord> findFirstByOrderByStartDesc();

    @Query("SELECT s FROM SuperiorTimeRecord s " +
            "JOIN s.temperaStation t " +
            "JOIN t.user u " +
            "WHERE u.username = :username " +
            "ORDER BY s.start DESC " +
            "Limit 1")
    List<SuperiorTimeRecord> findLastSavedByUser(@Param("username") String username);

}
