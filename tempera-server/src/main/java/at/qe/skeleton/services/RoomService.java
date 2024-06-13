package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.RoomRepository;
import at.qe.skeleton.repositories.ThresholdRepository;
import at.qe.skeleton.repositories.ThresholdTipRepository;
import at.qe.skeleton.rest.frontend.dtos.ThresholdUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;

    @Autowired private ThresholdRepository thresholdRepository;

    @Autowired private ThresholdTipRepository thresholdTipRepository;

    @Autowired private AccessPointRepository accessPointRepository;

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
            Threshold threshold = new Threshold(t.getSensorType(), t.getThresholdType(), t.getValue(), t.getModificationReason(), t.getTip());
            this.thresholdRepository.save(threshold);
            room.addThreshold(threshold);
        }
        return roomRepository.save(room);
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
    public Room getRoomById(String roomId) throws IllegalArgumentException {
        return roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND + roomId));
    }

    public List<Room> getAvailableRooms() {
        List<Room> rooms = roomRepository.findAll().stream().filter(room -> room.getAccessPoint() == null).toList();
        log.info("Available rooms: {}", rooms);
        return rooms;
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
        Threshold threshold = thresholdRepository.findById(tip.getId()).orElseThrow(() -> new IllegalArgumentException("Threshold not found"));
        ThresholdTip updateTip= threshold.getTip();
        updateTip.setTip(tip.getTip());
        return thresholdTipRepository.save(updateTip);
    }
}
