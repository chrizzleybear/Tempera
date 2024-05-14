package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.model.Userx;

import java.util.List;

public interface GroupRepository extends AbstractRepository<Group, Long> {

    List<Group> findAll();

    List<Group> findByGroupLead(Userx groupLead);

}