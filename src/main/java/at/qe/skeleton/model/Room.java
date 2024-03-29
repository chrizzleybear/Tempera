package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.TemperaStationIsNotEnabledException;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
public class Room {
    @Id
    private String roomId;

    @OneToMany
    private Set<Threshold> thresholds;

    /**
     * User can choose roomid but RoomService must ensure, that this id is not yet taken.
     * @param roomId the id, the user assignes to this room. it should be unique.
     */
    public Room(String roomId) {
        this.roomId = roomId;
        this.thresholds = new HashSet<>();
    }

    protected Room() {};

    public String getRoomId() {
        return roomId;
    }

    public Set<Threshold> getThresholds() {
        return thresholds;
    }

    /**
     * returns true if TemperaStation was not already part of the Set
     *
     * @param threshold to be added to this Room
     * @return true if this Accesspoint did not already contain a threshold
     * with the same ThresholdType and SensorType.
     *
     */
    public boolean addThreshold(Threshold threshold) {
        Optional<Threshold> existingThreshold =
                thresholds
                        .stream()
                        .filter(t -> t.getSensorType()==threshold.getSensorType() && t.getThresholdType() == threshold.getThresholdType())
                        .findAny();

        this.thresholds.add(threshold);
        if (existingThreshold.isPresent()){
            thresholds.remove(existingThreshold.get());
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomId, room.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }

    @Override
    public String toString() {
        return this.roomId;
    }
}
