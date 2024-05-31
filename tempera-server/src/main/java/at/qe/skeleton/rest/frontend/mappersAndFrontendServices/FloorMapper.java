package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.rest.frontend.dtos.FloorComponent;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorMapper {

    private final RoomService roomService;
    private final AccessPointService accessPointService;

    public FloorMapper(RoomService roomService, AccessPointService accessPointService) {
        this.roomService = roomService;
        this.accessPointService = accessPointService;
    }

    public FloorComponent mapToFloorComponent(String roomId) {
        try {
            AccessPoint accessPoint = accessPointService.getAccessPointByRoomId(roomId);
            return new FloorComponent(roomId, accessPoint.getId().toString(), accessPoint.isHealthy());
        } catch (CouldNotFindEntityException e) {
            return new FloorComponent(roomId, null, false);
        }
    }

    public List<FloorComponent> getAllFloorComponents() {
        List<Room> rooms = roomService.getAllRooms();
        return rooms.stream().map(room -> mapToFloorComponent(room.getId())).toList();
    }


}
