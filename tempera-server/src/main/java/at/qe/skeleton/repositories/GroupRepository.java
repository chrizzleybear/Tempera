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

    @Query("SELECT g FROM Groupx as g join g.members as members join g.projects as projects WHERE members.username = :username AND projects.id = :id")
    List<Groupx> findGroupByMemberAndProjectId(@Param("username") String username, @Param("id") Long id);
}