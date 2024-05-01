package at.qe.skeleton.payload.response;

import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;

public record HomeDataDTO(
    double temperature,
    double humidity,
    double irradiance,
    double nmvoc,
    Visibility visibility,
    State state) {}
