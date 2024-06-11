package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import org.springframework.stereotype.Service;

@Service
public class GroupxProjectMapper {
    public SimpleGroupxProjectDto mapToDto(GroupxProject entity) {
        if (entity == null) {
            return null;
        }
        return new SimpleGroupxProjectDto(
                entity.getGroup().getId().toString(),
                entity.getGroup().getName(),
                entity.getProject().getId().toString(),
                entity.getProject().getName());
    }
}
