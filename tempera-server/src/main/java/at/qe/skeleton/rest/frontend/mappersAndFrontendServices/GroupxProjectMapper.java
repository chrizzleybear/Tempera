package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.rest.frontend.dtos.GroupxProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleUserDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GroupxProjectMapper {
    private final UserMapper userMapper;

    public GroupxProjectMapper(UserMapper userMapper) {
        this.userMapper = userMapper;

    }

    public GroupxProjectDto groupxProjectDtoMapper(GroupxProject groupxProject) {
        Project project = groupxProject.getProject();
        Groupx group = groupxProject.getGroup();
        SimpleGroupDto simpleGroupDto = new SimpleGroupDto(group.getId().toString(), group.isActive(), group.getName(), group.getDescription(), group.getGroupLead().getUsername());
        SimpleProjectDto simpleProjectDto = new SimpleProjectDto(project.getId().toString(), project.isActive(), project.getName(), project.getDescription(), project.getManager().getUsername());
        SimpleUserDto managerDetails = userMapper.getSimpleUser(project.getManager());
        List<SimpleUserDto> contributors = groupxProject.getContributors().stream().map(userMapper::getSimpleUser).toList();
        return new GroupxProjectDto(
                simpleGroupDto,
                simpleProjectDto,
                managerDetails,
                contributors,
                groupxProject.isActive()
        );
    }
}
