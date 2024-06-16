package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.DeletionResponseType;

public record DeletionResponseDto(DeletionResponseType responseType, String message) {}
