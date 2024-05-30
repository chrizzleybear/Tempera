package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.TemperaStationIsNotEnabledException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class AccessPoint implements Persistable<UUID>, Serializable {

  // We need to implement persistable since we set UUID manually
  // the following strategy for the isNew Method comes from spring documentation:
  // https://docs.spring.io/spring-data/jpa/reference/jpa/entity-persistence.html
  @Transient private boolean isNew = true;

  @Override
  public boolean isNew() {
    return isNew;
  }

  @PrePersist
  @PostLoad
  void markNotNew() {
    this.isNew = false;
  }

  @Id private UUID id;
  @OneToMany(mappedBy = "accessPoint") private Set<TemperaStation> temperaStations;
  @OneToOne() private Room room;
  private boolean enabled;
  private boolean isHealthy;

  public AccessPoint(@NotNull UUID id, @NotNull Room room, boolean enabled, boolean isHealthy) {
    this.id = Objects.requireNonNull(id, "id must not be null");
    this.room = Objects.requireNonNull(room, "room must not be null");
    this.temperaStations = new HashSet<>();
    this.enabled = enabled;
    this.isHealthy = isHealthy;
  }

  public AccessPoint() {
    this.temperaStations = new HashSet<>();
  }

  public Set<TemperaStation> getTemperaStations() {
    return temperaStations;
  }


  public boolean isHealthy() {
        return isHealthy;
  }

  public void setHealthy(boolean healthy) {
    isHealthy = healthy;
  }

  /**
   * returns true if TemperaStation was not already part of the Set. also sets this Accesspoint
   * as AccessPoint on the provided TemperaStation (bidirectional Relationship)
   *
   * @param temperaStation to be added to this AccessPoint
   * @return true if this accesspoint did not already contain the specified temperaStation
   * @throws TemperaStationIsNotEnabledException if this TemperaStation is not enabled, but still
   *     adds the accesspoint before
   */
  public boolean addTemperaStation(@NotNull TemperaStation temperaStation) {
    temperaStation.setAccessPoint(this);
    return this.temperaStations.add(temperaStation);
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public boolean isEnabled() {
    return enabled;
  }

  // todo think about: what should we do, when AccessPoint gets disabled
  // -> do associated TemperaStations get disabled as well?
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessPoint that = (AccessPoint) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return id.toString();
  }

  @Override
  public UUID getId() {
    return id;
  }
}
