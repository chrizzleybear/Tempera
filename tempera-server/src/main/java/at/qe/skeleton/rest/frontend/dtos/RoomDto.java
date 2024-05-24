package at.qe.skeleton.rest.frontend.dtos;

import java.util.Set;

public record RoomDto(String roomId, Set<ThresholdDto> thresholds){
}
