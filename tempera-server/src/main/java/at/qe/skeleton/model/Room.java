package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
public class Room implements Persistable<String>, Serializable {

  // We need to implement Persistable since we set UUID manually
  // the following strategy for the isNew Method comes from spring documentation:
  // https://docs.spring.io/spring-data/jpa/reference/jpa/entity-persistence.html
  @Transient private boolean isNew = true;

  @Override
  public String getId() {
    return roomId;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }

  @PrePersist
  @PostLoad
  void markNotNew() {
    this.isNew = false;
  }

  @Id private String roomId;

  @OneToMany private Set<Threshold> thresholds;

  /**
   * User can choose roomid but RoomService must ensure, that this id is not yet taken.
   *
   * @param roomId the id, the user assignes to this room. it should be unique.
   */
  public Room(String roomId) {
    this.roomId = roomId;
    this.thresholds = new HashSet<>();
  }

  protected Room() {}


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
   * @return true if this AccessPoint did not already contain a threshold with the same
   *     ThresholdType and SensorType.
   */
  public boolean addThreshold(Threshold threshold) {
    Optional<Threshold> existingThreshold =
        thresholds.stream()
            .filter(
                t ->
                    t.getSensorType() == threshold.getSensorType()
                        && t.getThresholdType() == threshold.getThresholdType())
            .findAny();

    this.thresholds.add(threshold);
    if (existingThreshold.isPresent()) {
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
