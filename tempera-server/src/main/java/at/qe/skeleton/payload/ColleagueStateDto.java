package at.qe.skeleton.payload;

import at.qe.skeleton.model.enums.State;

public record ColleagueStateDto(
        String name,
        String workplace,
        State state
) {}
