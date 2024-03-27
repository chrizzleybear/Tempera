package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.TemperaStationIsNotEnabledException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Accesspoint {

    @Id
    private UUID id;
    @OneToMany
    private Set<TemperaStation> temperaStations;
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
