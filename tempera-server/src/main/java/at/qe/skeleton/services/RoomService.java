package at.qe.skeleton.services;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.RoomRepository;
import at.qe.skeleton.repositories.ThresholdRepository;
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


    @Autowired
    public RoomService(RoomRepository roomRepository, AccessPointRepository accessPointRepository, ThresholdRepository thresholdRepository) {
        this.roomRepository = roomRepository;
        this.thresholdRepository = thresholdRepository;
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

    public Set<Threshold> initialiseThresholds(){
        Set<Threshold> thresholds = new HashSet<>();
        Threshold temperatureHighInfo = new Threshold();
        temperatureHighInfo.setSensorType(SensorType.TEMPERATURE);
        temperatureHighInfo.setThresholdType(ThresholdType.UPPERBOUND_INFO);
        temperatureHighInfo.setValue(0);
        this.thresholdRepository.save(temperatureHighInfo);
        thresholds.add(temperatureHighInfo);
        Threshold temperatureHighWarning = new Threshold();
        temperatureHighWarning.setSensorType(SensorType.TEMPERATURE);
        temperatureHighWarning.setThresholdType(ThresholdType.UPPERBOUND_WARNING);
        temperatureHighWarning.setValue(0);
        this.thresholdRepository.save(temperatureHighWarning);
        thresholds.add(temperatureHighWarning);
        Threshold temperatureLowInfo = new Threshold();
        temperatureLowInfo.setSensorType(SensorType.TEMPERATURE);
        temperatureLowInfo.setThresholdType(ThresholdType.LOWERBOUND_INFO);
        temperatureLowInfo.setValue(0);
        this.thresholdRepository.save(temperatureLowInfo);
        thresholds.add(temperatureLowInfo);
        Threshold temperatureLowWarning = new Threshold();
        temperatureLowWarning.setSensorType(SensorType.TEMPERATURE);
        temperatureLowWarning.setThresholdType(ThresholdType.LOWERBOUND_WARNING);
        temperatureLowWarning.setValue(0);
        this.thresholdRepository.save(temperatureLowWarning);
        thresholds.add(temperatureLowWarning);
        Threshold humidityHighInfo = new Threshold();
        humidityHighInfo.setSensorType(SensorType.HUMIDITY);
        humidityHighInfo.setThresholdType(ThresholdType.UPPERBOUND_INFO);
        humidityHighInfo.setValue(0);
        this.thresholdRepository.save(humidityHighInfo);
        thresholds.add(humidityHighInfo);
        Threshold humidityHighWarning = new Threshold();
        humidityHighWarning.setSensorType(SensorType.HUMIDITY);
        humidityHighWarning.setThresholdType(ThresholdType.UPPERBOUND_WARNING);
        humidityHighWarning.setValue(0);
        this.thresholdRepository.save(humidityHighWarning);
        thresholds.add(humidityHighWarning);
        Threshold humidityLowInfo = new Threshold();
        humidityLowInfo.setSensorType(SensorType.HUMIDITY);
        humidityLowInfo.setThresholdType(ThresholdType.LOWERBOUND_INFO);
        humidityLowInfo.setValue(0);
        this.thresholdRepository.save(humidityLowInfo);
        thresholds.add(humidityLowInfo);
        Threshold humidityLowWarning = new Threshold();
        humidityLowWarning.setSensorType(SensorType.HUMIDITY);
        humidityLowWarning.setThresholdType(ThresholdType.LOWERBOUND_WARNING);
        humidityLowWarning.setValue(0);
        this.thresholdRepository.save(humidityLowWarning);
        thresholds.add(humidityLowWarning);
        Threshold irradianceHighInfo = new Threshold();
        irradianceHighInfo.setSensorType(SensorType.IRRADIANCE);
        irradianceHighInfo.setThresholdType(ThresholdType.UPPERBOUND_INFO);
        irradianceHighInfo.setValue(0);
        this.thresholdRepository.save(irradianceHighInfo);
        thresholds.add(irradianceHighInfo);
        Threshold irradianceHighWarning = new Threshold();
        irradianceHighWarning.setSensorType(SensorType.IRRADIANCE);
        irradianceHighWarning.setThresholdType(ThresholdType.UPPERBOUND_WARNING);
        irradianceHighWarning.setValue(0);
        this.thresholdRepository.save(irradianceHighWarning);
        thresholds.add(irradianceHighWarning);
        Threshold irradianceLowInfo = new Threshold();
        irradianceLowInfo.setSensorType(SensorType.IRRADIANCE);
        irradianceLowInfo.setThresholdType(ThresholdType.LOWERBOUND_INFO);
        irradianceLowInfo.setValue(0);
        this.thresholdRepository.save(irradianceLowInfo);
        thresholds.add(irradianceLowInfo);
        Threshold irradianceLowWarning = new Threshold();
        irradianceLowWarning.setSensorType(SensorType.IRRADIANCE);
        irradianceLowWarning.setThresholdType(ThresholdType.LOWERBOUND_WARNING);
        irradianceLowWarning.setValue(0);
        this.thresholdRepository.save(irradianceLowWarning);
        thresholds.add(irradianceLowWarning);
        Threshold nmvocHighInfo = new Threshold();
        nmvocHighInfo.setSensorType(SensorType.NMVOC);
        nmvocHighInfo.setThresholdType(ThresholdType.UPPERBOUND_INFO);
        nmvocHighInfo.setValue(0);
        this.thresholdRepository.save(nmvocHighInfo);
        thresholds.add(nmvocHighInfo);
        Threshold nmvocHighWarning = new Threshold();
        nmvocHighWarning.setSensorType(SensorType.NMVOC);
        nmvocHighWarning.setThresholdType(ThresholdType.UPPERBOUND_WARNING);
        nmvocHighWarning.setValue(0);
        this.thresholdRepository.save(nmvocHighWarning);
        thresholds.add(nmvocHighWarning);
        Threshold nmvocLowInfo = new Threshold();
        nmvocLowInfo.setSensorType(SensorType.NMVOC);
        nmvocLowInfo.setThresholdType(ThresholdType.LOWERBOUND_INFO);
        nmvocLowInfo.setValue(0);
        this.thresholdRepository.save(nmvocLowInfo);
        thresholds.add(nmvocLowInfo);
        Threshold nmvocLowWarning = new Threshold();
        nmvocLowWarning.setSensorType(SensorType.NMVOC);
        nmvocLowWarning.setThresholdType(ThresholdType.LOWERBOUND_WARNING);
        nmvocLowWarning.setValue(0);
        this.thresholdRepository.save(nmvocLowWarning);
        thresholds.add(nmvocLowWarning);
        return thresholds;
    }

    public Threshold updateThreshold(Threshold threshold) {
        Threshold updateThreshold = thresholdRepository.findById(threshold.getId()).orElseThrow(() -> new IllegalArgumentException("Threshold not found"));
        updateThreshold.setValue(threshold.getValue());
        return thresholdRepository.save(updateThreshold);
    }
}
