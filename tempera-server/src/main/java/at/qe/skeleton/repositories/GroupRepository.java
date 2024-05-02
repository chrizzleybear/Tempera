package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Group;

public interface GroupRepository extends AbstractRepository<Group, Long> {

    Group findFirstByName(String name);
    void delete(Group group);
    Group findFirstById(Long id);

}
