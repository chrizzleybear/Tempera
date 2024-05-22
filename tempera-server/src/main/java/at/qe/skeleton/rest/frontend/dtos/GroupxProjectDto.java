package at.qe.skeleton.rest.frontend.dtos;

import java.util.List;

public record GroupxProjectDto(GroupDto groupDto, ProjectDto projectDto, List<MemberDto> members) {}
