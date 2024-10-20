package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
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

@Component
@Scope("application")
public class RoomService {

    private static final String ROOM_NOT_FOUND = "Room not found: ";
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;

    @Autowired private ThresholdRepository thresholdRepository;

    @Autowired private ThresholdTipRepository thresholdTipRepository;

    @Autowired private AccessPointRepository accessPointRepository;

    @Autowired private AuditLogService auditLogService;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Used for testing purposes.
     *
     * @param roomRepository
     * @param thresholdRepository
     * @param thresholdTipRepository
     * @param accessPointRepository
     * @param auditLogService
     */
    public RoomService(RoomRepository roomRepository, ThresholdRepository thresholdRepository, ThresholdTipRepository thresholdTipRepository, AccessPointRepository accessPointRepository, AuditLogService auditLogService) {
        this.roomRepository = roomRepository;
        this.thresholdRepository = thresholdRepository;
        this.thresholdTipRepository = thresholdTipRepository;
        this.accessPointRepository = accessPointRepository;
        this.auditLogService = auditLogService;
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
        auditLogService.logEvent(LogEvent.CREATE, LogAffectedType.ROOM,
                "Room " + roomId + " was created with default thresholds.");
        return roomRepository.save(room);
    }
    @Transactional
    public void deleteRoom(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND + roomId));
        auditLogService.logEvent(LogEvent.DELETE, LogAffectedType.ROOM,
                "Room " + roomId + " was deleted.");
        roomRepository.delete(room);
    }
    @Transactional
    public boolean addThresholdToRoom(String roomId, Threshold threshold){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND + roomId));
        room.addThreshold(threshold);
        auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.ROOM,
                "Threshold " + threshold.getThresholdType() + " was added to room " + roomId + ".");
        return roomRepository.save(room) != null;
    }
    @Transactional
    public boolean removeThresholdFromRoom(String roomId, Threshold threshold){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND + roomId));
        if(room.getThresholds().contains(threshold)){
            room.getThresholds().remove(threshold);
            auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.ROOM,
                    "Threshold " + threshold.getThresholdType() + " was removed from room " + roomId + ".");
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

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional
    public Threshold updateThreshold(ThresholdUpdateDto dto) {
        Threshold updateThreshold = thresholdRepository.findById(dto.threshold().id()).orElseThrow(() -> new IllegalArgumentException("Threshold not found"));
        updateThreshold.setValue(dto.threshold().value());
        String reason = dto.reason();
        // TO-DO: add info for which station/room the threshold has been edited
        auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.THRESHOLD,
                "Threshold " + updateThreshold.getThresholdType() + " was updated. Reason: " +  reason);
        return thresholdRepository.save(updateThreshold);
    }
}
