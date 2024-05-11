package at.qe.skeleton.rest.frontend.payload.response;

import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;

import java.util.List;

public record DashboardDataResponse(
    double temperature,
    double humidity,
    double irradiance,
    double nmvoc,
    Visibility visibility,
    State state,
    String stateTimestamp,
    // project can be null
    ProjectDto project,
    List<ColleagueStateDto> colleagueStates) {}
