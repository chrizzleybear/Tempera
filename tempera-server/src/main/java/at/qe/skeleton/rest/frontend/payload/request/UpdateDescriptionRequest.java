package at.qe.skeleton.rest.frontend.payload.request;

import jakarta.validation.constraints.NotNull;

/**
 * Request payload for updating a description.
 *
 * @param entryId The id of the timetable entry to update
 * @param description The new description to assign to the timeRecord
 */
public record UpdateDescriptionRequest(@NotNull Long entryId, @NotNull String description) {}
