package at.qe.skeleton.rest.frontend.payload.response;

import at.qe.skeleton.rest.frontend.dtos.AccumulatedTimeDto;

import java.util.List;

public record AccumulatedTimeResponse(List<AccumulatedTimeDto> accumulatedTimes) {}
