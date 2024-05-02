package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.State;

public record ColleagueStateDto(
        String name,
        String workplace,
        State state
) {}
