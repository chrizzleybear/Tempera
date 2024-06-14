package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.services.GroupService;
import at.qe.skeleton.services.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupMapperService {

    private final GroupService groupService;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final GroupxProjectMapper groupxProjectMapper;

    public GroupMapperService(
            GroupService groupService, UserMapper userMapper, ProjectService projectService, GroupxProjectMapper groupxProjectMapper) {
    this.userMapper = userMapper;
    this.groupService = groupService;
    this.projectService = projectService;
    this.groupxProjectMapper = groupxProjectMapper;
    }

    public List<SimpleGroupDto> getSimpleGroupDtosByGroupLead(String groupLeadI) {
        List<Groupx> groups = groupService.getGroupsByGroupLead(groupLeadI);
        return groups.stream().map(this::mapToSimpleGroupDto).toList();
    }

    @Transactional
    public GroupDetailsDto getGroupDetailsDto(Long groupId) {
        Groupx group = groupService.getGroup(groupId);
        return new GroupDetailsDto(
                group.getId().toString(),
                group.getName(),
                group.getDescription(),
                userMapper.getSimpleUser(group.getGroupLead()
        ));
    }

    public SimpleGroupDto getSimpleGroupDto(String groupId) {
        Groupx group = groupService.getGroup(Long.parseLong(groupId));
        return mapToSimpleGroupDto(group);
    }

    public ExtendedGroupDto loadExtendedGroupDto(Long groupId){
        Groupx group =
                groupService
                        .getGroupDetailedGroupLead(groupId);
        GroupDetailsDto groupDetailsDto = this.mapToGroupDetailsDto(group);
        List<GroupxProject> groupxProjects = projectService.getGroupxProjectsByGroupId(groupId);
        List<SimpleProjectDto> simpleProjectDtos = groupxProjects.stream().filter(GroupxProject::isActive).map(groupxProjectMapper::mapToSimpleProjectDto).toList();
        List<Userx> groupMembers = groupService.getMembers(groupId);
        Set<SimpleUserDto> groupMembersDto = groupMembers.stream().map(userMapper::getSimpleUser).collect(Collectors.toSet());
        return new ExtendedGroupDto(groupDetailsDto, simpleProjectDtos, groupMembersDto);
    }

    public SimpleGroupDto updateGroup(SimpleGroupDto group) {
        Groupx groupx = groupService.updateGroup(Long.valueOf(group.id()), group.name(), group.description(), group.groupLead());
        return mapToSimpleGroupDto(groupx);
    }

    public SimpleGroupDto createGroup(SimpleGroupDto group) {
        Groupx groupx = groupService.createGroup(group.name(), group.description(), group.groupLead());
        return mapToSimpleGroupDto(groupx);
    }

    public SimpleGroupDto mapToSimpleGroupDto(GroupxProject groupxProject) {
        return mapToSimpleGroupDto(groupxProject.getGroup());
    }

    public SimpleGroupDto mapToSimpleGroupDto(Groupx group) {
        return new SimpleGroupDto(
                group.getId().toString(),
                group.isActive(),
                group.getName(),
                group.getDescription(),
                group.getGroupLead().getUsername()
        );
    }

    public GroupDetailsDto mapToGroupDetailsDto (Groupx group) {
        return new GroupDetailsDto(
                group.getId().toString(),
                group.getName(),
                group.getDescription(),
                userMapper.getSimpleUser(group.getGroupLead())
        );
    }

}
