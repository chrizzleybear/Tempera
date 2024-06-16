package at.qe.skeleton.rest.frontend.payload.response;

import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.FrontendMeasurementDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DashboardDataResponse(
    @NotNull FrontendMeasurementDto temperature,
    @NotNull FrontendMeasurementDto humidity,
    @NotNull FrontendMeasurementDto irradiance,
    @NotNull FrontendMeasurementDto nmvoc,
    @NotNull Visibility visibility,
    State state,
    String stateTimestamp,
    // project can be null
    SimpleGroupxProjectDto project,
    SimpleGroupxProjectDto defaultProject,
    List<SimpleGroupxProjectDto> availableProjects,
    List<ColleagueStateDto> colleagueStates) {}
