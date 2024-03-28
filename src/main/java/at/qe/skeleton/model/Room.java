package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.TemperaStationIsNotEnabledException;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    @OneToMany
    private Set<Threshold> thresholds;

    public Room() {
        this.thresholds = new HashSet<>();
    }

    public int getRoomId() {
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
        return existingThreshold.isEmpty();
    }
}
