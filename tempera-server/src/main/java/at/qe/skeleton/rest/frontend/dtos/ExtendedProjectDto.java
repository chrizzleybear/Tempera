package at.qe.skeleton.rest.frontend.dtos;

import java.util.List;

/**
 * Data transfer object for a project with extended information.
 *
 * @param manager
 * @param simpleProjectDto
 * @param activeGroups Groups that are currently active in the project
 * @param deactivatedGroups Groups that have formerly been active in the project
 * @param contributors active Contributors (users that might have been active through deactivated Groups are not included)
 */
public record ExtendedProjectDto(SimpleUserDto manager, SimpleProjectDto simpleProjectDto, List<SimpleGroupDto> activeGroups, List<SimpleGroupDto> deactivatedGroups, List<SimpleUserDto> contributors) {}
