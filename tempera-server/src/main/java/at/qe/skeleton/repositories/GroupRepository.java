package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Group;

import java.util.List;

public interface GroupRepository extends AbstractRepository<Group, Long> {

    List<Group> findAll();



}