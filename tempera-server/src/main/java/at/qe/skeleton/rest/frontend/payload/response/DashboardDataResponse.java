package at.qe.skeleton.rest.frontend.payload.response;

import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DashboardDataResponse(
    @NotNull Double temperature,
    @NotNull Double humidity,
    @NotNull Double irradiance,
    @NotNull Double nmvoc,
    @NotNull Visibility visibility,
    State state,
    String stateTimestamp,
    // project can be null
    SimpleGroupxProjectDto defaultProject,
    List<SimpleGroupxProjectDto> availableProjects,
    List<ColleagueStateDto> colleagueStates) {}
