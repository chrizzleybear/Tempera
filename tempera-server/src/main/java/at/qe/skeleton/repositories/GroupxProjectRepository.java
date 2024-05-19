package at.qe.skeleton.repositories;

import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.GroupxProjectId;

import java.util.List;
import java.util.Optional;

public interface GroupxProjectRepository extends AbstractRepository<GroupxProject, GroupxProjectId> {

    public List<GroupxProject> findAllByProjectId(Long projectId);

    public Optional<GroupxProject> findByGroup_IdAndProject_Id(Long groupId, Long projectId);
}
