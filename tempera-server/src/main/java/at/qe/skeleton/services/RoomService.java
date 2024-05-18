package at.qe.skeleton.services;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.ThresholdTip;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.RoomRepository;
import at.qe.skeleton.repositories.ThresholdRepository;
import at.qe.skeleton.repositories.ThresholdTipRepository;
import at.qe.skeleton.rest.frontend.dtos.ThresholdUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Scope("application")
public class RoomService {

    private static final String ROOM_NOT_FOUND = "Room not found: ";
    private final RoomRepository roomRepository;
    private final ThresholdRepository thresholdRepository;
    private final ThresholdTipRepository thresholdTipRepository;



    @Autowired
    public RoomService(RoomRepository roomRepository, AccessPointRepository accessPointRepository, ThresholdRepository thresholdRepository, ThresholdTipRepository thresholdTipRepository) {
        this.roomRepository = roomRepository;
        this.thresholdRepository = thresholdRepository;
        this.thresholdTipRepository = thresholdTipRepository;
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
        room.setThresholds(initialiseThresholds());

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
    public Optional<Room> getRoomById(String roomId) {
        return roomRepository.findById(roomId);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findAll().stream().filter(room -> room.getAccessPoint() == null).toList();
    }

    @Transactional
    public Set<Threshold> initialiseThresholds() {
        Set<Threshold> thresholds = new HashSet<>();

        thresholds.add(createAndSaveThreshold(SensorType.TEMPERATURE, ThresholdType.UPPERBOUND_INFO, 0));
        thresholds.add(createAndSaveThreshold(SensorType.TEMPERATURE, ThresholdType.UPPERBOUND_WARNING, 0));
        thresholds.add(createAndSaveThreshold(SensorType.TEMPERATURE, ThresholdType.LOWERBOUND_INFO, 0));
        thresholds.add(createAndSaveThreshold(SensorType.TEMPERATURE, ThresholdType.LOWERBOUND_WARNING, 0));

        thresholds.add(createAndSaveThreshold(SensorType.HUMIDITY, ThresholdType.UPPERBOUND_INFO, 0));
        thresholds.add(createAndSaveThreshold(SensorType.HUMIDITY, ThresholdType.UPPERBOUND_WARNING, 0));
        thresholds.add(createAndSaveThreshold(SensorType.HUMIDITY, ThresholdType.LOWERBOUND_INFO, 0));
        thresholds.add(createAndSaveThreshold(SensorType.HUMIDITY, ThresholdType.LOWERBOUND_WARNING, 0));

        thresholds.add(createAndSaveThreshold(SensorType.IRRADIANCE, ThresholdType.UPPERBOUND_INFO, 0));
        thresholds.add(createAndSaveThreshold(SensorType.IRRADIANCE, ThresholdType.UPPERBOUND_WARNING, 0));
        thresholds.add(createAndSaveThreshold(SensorType.IRRADIANCE, ThresholdType.LOWERBOUND_INFO, 0));
        thresholds.add(createAndSaveThreshold(SensorType.IRRADIANCE, ThresholdType.LOWERBOUND_WARNING, 0));

        thresholds.add(createAndSaveThreshold(SensorType.NMVOC, ThresholdType.UPPERBOUND_INFO, 0));
        thresholds.add(createAndSaveThreshold(SensorType.NMVOC, ThresholdType.UPPERBOUND_WARNING, 0));
        thresholds.add(createAndSaveThreshold(SensorType.NMVOC, ThresholdType.LOWERBOUND_INFO, 0));
        thresholds.add(createAndSaveThreshold(SensorType.NMVOC, ThresholdType.LOWERBOUND_WARNING, 0));

        return thresholds;
    }

    private Threshold createAndSaveThreshold(SensorType sensorType, ThresholdType thresholdType, double value) {
        Threshold threshold = new Threshold();
        threshold.setSensorType(sensorType);
        threshold.setThresholdType(thresholdType);
        threshold.setValue(value);
        ThresholdTip tip = new ThresholdTip("Default tip for " + sensorType + " " + thresholdType);
        this.thresholdTipRepository.save(tip);
        threshold.setTip(tip);
        this.thresholdRepository.save(threshold);
        return threshold;
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
