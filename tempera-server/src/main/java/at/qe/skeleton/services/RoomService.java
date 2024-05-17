package at.qe.skeleton.services;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Scope("application")
public class RoomService {

    private static final String ROOM_NOT_FOUND = "Room not found: ";
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, AccessPointRepository accessPointRepository) {
        this.roomRepository = roomRepository;
    }
    @Transactional
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    @Transactional
    public Room createRoom(String roomId) {
        if (roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Room ID already in use: " + roomId);
        }
        return roomRepository.save(new Room(roomId));
    }
    @Transactional
    public void deleteRoom(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND + roomId));
        roomRepository.delete(room);
    }
    @Transactional
    public boolean addThresholdToRoom(String roomId, Threshold threshold){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND + roomId));
        room.addThreshold(threshold);
        return roomRepository.save(room) != null;
    }
    @Transactional
    public boolean removeThresholdFromRoom(String roomId, Threshold threshold){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND + roomId));
        if(room.getThresholds().contains(threshold)){
            room.getThresholds().remove(threshold);
            return roomRepository.save(room) != null;
        }
        return false;
    }
    public Optional<Room> getRoomById(String roomId) {
        return roomRepository.findById(roomId);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findAll().stream().filter(room -> room.getAccessPoint() == null).toList();
    }
}
