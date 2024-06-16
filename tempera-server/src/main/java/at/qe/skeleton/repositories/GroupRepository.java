package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GroupRepository extends AbstractRepository<Groupx, Long> {

    List<Groupx> findAll();

    List<Groupx> findByGroupLead(Userx groupLead);
    List<Groupx> findAllByMembersContains(Userx user);

    @Query("SELECT g FROM Groupx g JOIN g.groupxProjects gxp JOIN gxp.project p WHERE :manager = p.manager")
    List<Groupx> findAllByManager(Userx manager);

    @Query("SELECT new at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto(CAST(g.id AS STRING), g.name, g.description, g.groupLead.username) FROM Groupx g WHERE :username = g.groupLead.username")
    List<SimpleGroupDto> findAllSimpleGroupDtosByGroupLead(@Param("username") String groupLeadUsername);
}