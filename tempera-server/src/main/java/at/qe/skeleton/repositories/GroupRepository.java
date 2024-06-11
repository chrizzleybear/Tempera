package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.Userx;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends AbstractRepository<Groupx, Long> {

    List<Groupx> findAll();

    List<Groupx> findByGroupLead(Userx groupLead);
    List<Groupx> findAllByMembersContains(Userx user);

    @Query("SELECT g FROM Groupx g JOIN g.groupxProjects gxp JOIN gxp.project p WHERE :manager = p.manager")
    List<Groupx> findAllByManager(Userx manager);
}