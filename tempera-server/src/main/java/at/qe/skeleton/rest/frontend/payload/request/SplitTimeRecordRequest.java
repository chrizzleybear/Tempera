package at.qe.skeleton.rest.frontend.payload.request;

import jakarta.validation.constraints.NotNull;

/**
 * Request payload for splitting a time record.
 *
 * @param entryId The id of the timetable entry to split
 * @param splitTimestamp The timestamp to split the timeRecord at
 */
public record SplitTimeRecordRequest(@NotNull Long entryId, @NotNull String splitTimestamp) {}
