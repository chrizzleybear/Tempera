package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.State;

import java.util.List;

public record ColleagueStateDto(
    String name, String workplace, State state, Boolean isVisible, List<String> groupOverlap) {}
