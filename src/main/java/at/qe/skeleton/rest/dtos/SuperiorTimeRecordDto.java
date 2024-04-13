package at.qe.skeleton.rest.dtos;

import at.qe.skeleton.model.enums.State;

import java.time.LocalDateTime;

//todo: how should STRs be stored? when first coming in we dont know end time yet -> should they be updadet with different
// rest call? Or should the creation of a new rest call always send the end time of the previous one? should it just send the
// id of the previous one? because start time of new one == endtime of previous one...
public record SuperiorTimeRecordDto (Long Id, String stationId, LocalDateTime start, LocalDateTime end, State state){};
