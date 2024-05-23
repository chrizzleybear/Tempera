package at.qe.skeleton.services;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Threshold;
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


    private final RoomRepository roomRepository;

    @Autowired private ThresholdRepository thresholdRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
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
        Room room = new Room(roomId);
        // when a room is created, the default thresholds are added
        for (Threshold t : thresholdRepository.findDefaultThresholds()) {
            room.addThreshold(t);
        }
        return roomRepository.save(room);
    }
    @Transactional
    public void deleteRoom(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        roomRepository.delete(room);
    }
    @Transactional
    public boolean addThresholdToRoom(String roomId, Threshold threshold){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        room.addThreshold(threshold);
        return roomRepository.save(room) != null;
    }
    @Transactional
    public boolean removeThresholdFromRoom(String roomId, Threshold threshold){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
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
    //two way binding ->delete
    public AccessPoint getAccesspoint(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND + roomId));
        return accessPointRepository.findByRoom(room).orElseThrow(() -> new IllegalArgumentException("AccessPoint not found"));
    }

    //TODO: Save modification reason in AuditLog
    @Transactional
    public Threshold updateThreshold(ThresholdUpdateDto dto) {
        Threshold updateThreshold = thresholdRepository.findById(dto.threshold().id()).orElseThrow(() -> new IllegalArgumentException("Threshold not found"));
        updateThreshold.setValue(dto.threshold().value());
        String reason = dto.reason();
        return thresholdRepository.save(updateThreshold);
    }
    @Transactional
    public ThresholdTip updateThresholdTip(ThresholdTip tip) {
        ThresholdTip updateTip = thresholdTipRepository.findById(tip.getId()).orElseThrow(() -> new IllegalArgumentException("Tip not found"));
        updateTip.setTip(tip.getTip());
        return thresholdTipRepository.save(updateTip);
    }
}
