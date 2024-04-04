package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.TemperaStationIsNotEnabledException;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Accesspoint implements Persistable<UUID>, Serializable {

    // We need to implement Persistable since we set UUID manually
    // the following strategy for the isNew Method comes from spring documentation:
    // https://docs.spring.io/spring-data/jpa/reference/jpa/entity-persistence.html
    @Transient
    private boolean isNew = true;
    @Override
    public boolean isNew() {
        return isNew;
    }
    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Id
    private UUID id;
    @OneToMany
    private Set<TemperaStation> temperaStations;
    @OneToOne
    private Room room;
    private boolean enabled;

    public Accesspoint() {
        this.id = UUID.randomUUID();
        this.temperaStations = new HashSet<>();
    }

    public Set<TemperaStation> getTemperaStations() {
        return temperaStations;
    }

  /**
   * returns true if TemperaStation was not already part of the Set
   *
   * @param temperaStation to be added to this Accesspoint
   * @return true if this accesspoint did not already contain the specified temperaStation
   * @throws TemperaStationIsNotEnabledException if this TemperaStation is not enabled, but still adds the accesspoint
   * before
   */
  public boolean addTemperaStation(TemperaStation temperaStation) throws  TemperaStationIsNotEnabledException {
      if (!this.enabled){
          this.temperaStations.add(temperaStation);
          throw new TemperaStationIsNotEnabledException();
      }
        return this.temperaStations.add(temperaStation);
    }

    public boolean isEnabled() {
        return enabled;
    }

    //todo think about: what should we do, when Accesspoint gets disabled
    // -> do associated TemperaStations get disabled as well?
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accesspoint that = (Accesspoint) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString(){
      return id.toString();
    }

    @Override
    public UUID getId() {
        return id;
    }
}
