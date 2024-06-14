package at.qe.skeleton.rest.frontend.dtos;

import jakarta.validation.constraints.NotNull;

public record MemberAssigmentDto (@NotNull String groupId, @NotNull String memberId){
}
