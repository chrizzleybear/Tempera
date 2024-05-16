package at.qe.skeleton.rest.frontend.payload.response;

import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;

import java.util.List;

public record GetTimetableDataResponse(List<TimetableEntryDto> tableEntries) {}
