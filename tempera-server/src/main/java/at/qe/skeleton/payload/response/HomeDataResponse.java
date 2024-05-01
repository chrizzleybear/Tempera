package at.qe.skeleton.payload.response;

import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.payload.ColleagueStateDto;
import at.qe.skeleton.payload.ProjectDto;

import java.time.LocalDateTime;
import java.util.List;


public record HomeDataResponse(
        double temperature,
        double humidity,
        double irradiance,
        double nmvoc,
        Visibility visibility,
        State state,
        LocalDateTime stateTimestamp,
        // project can be null
        ProjectDto project,
        List<ColleagueStateDto> colleagueStates) {}
