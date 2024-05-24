package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import at.qe.skeleton.services.GroupService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMapperService {

    private final GroupService groupService;

    public GroupMapperService(GroupService groupService) {
        this.groupService = groupService;
    }

    public List<SimpleGroupDto> getSimpleGroupDtosByGroupLead(String groupLeadI) {
        List<Groupx> groups = groupService.getGroupsByGroupLead(groupLeadI);
        return groups.stream().map(this::groupDtoMapper).toList();
    }

    public SimpleGroupDto updateGroup(SimpleGroupDto group) {
        Groupx groupx = groupService.updateGroup(Long.valueOf(group.id()), group.name(), group.description(), group.groupLead());
        return groupDtoMapper(groupx);
    }

    public SimpleGroupDto createGroup(SimpleGroupDto group) {
        Groupx groupx = groupService.createGroup(group.name(), group.description(), group.groupLead());
        return groupDtoMapper(groupx);
    }



    public SimpleGroupDto groupDtoMapper(GroupxProject groupxProject) {
        return groupDtoMapper(groupxProject.getGroup());
    }

    public SimpleGroupDto groupDtoMapper(Groupx groupx) {
        return new SimpleGroupDto(
                groupx.getId().toString(),
                groupx.getName(),
                groupx.getDescription(),
                groupx.getGroupLead().getUsername()
        );
    }

}
