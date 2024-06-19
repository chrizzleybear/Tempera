package at.qe.skeleton.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Persistable;

import java.util.List;
import java.util.Objects;

@Entity
public class TemperaStation implements Persistable<String> {

  @Id
  private String id;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Userx user;

  private boolean enabled;

  private boolean isHealthy;

  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private AccessPoint accessPoint;

  @OneToMany(mappedBy = "temperaStation", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Sensor> sensors;

  // We need to implement Persistable since we set Id manually
  // the following strategy for the isNew Method comes from spring documentation:
  // https://docs.spring.io/spring-data/jpa/reference/jpa/entity-persistence.html
  @Transient private boolean isNew = true;

  /**
   * Do not use, for testing purposes only.
   * Also, direct creation of TemperaStations should be avoided, use {@link
   * at.qe.skeleton.services.TemperaStationService#createTemperaStation} instead
   *
   * @param id
   * @param enabled
   * @param user
   */
  @Deprecated
  public TemperaStation(String id, boolean enabled, Userx user) {
  }

  /**
   * Do not use, a proper id will only be set using the service method.
   * {@link at.qe.skeleton.services.TemperaStationService#createTemperaStation}
   */
  @Deprecated
  public TemperaStation() {
  }

  /**
   * direct creation of TemperaStations should be avoided, use {@link
   * at.qe.skeleton.services.TemperaStationService#createTemperaStation} instead
   */
  public TemperaStation(@NotNull String id, boolean enabled, Userx user, boolean isHealthy) {
    this.user = user;
    this.id = Objects.requireNonNull(id);
    this.enabled = enabled;
    this.isHealthy = isHealthy;
  }

  @Override
  public String getId() {
    return id;
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

  public void setUser(Userx user) {
    this.user = user;
  }

  public Userx getUser() {
    return user;
  }

  public AccessPoint getAccessPoint() {
    return accessPoint;
  }

  public void addSensor(Sensor sensor) {
    sensors.add(sensor);
    sensor.setTemperaStation(this);
  }

  public void removeSensor(Sensor sensor) {
    sensors.remove(sensor);
    sensor.setTemperaStation(null);
  }

  public List<Sensor> getSensors() {
    return sensors;
  }


  /**
   * Beware of bidirectional relationship between AccessPoint and TemperaStation.
   * Best to use the addTemperaStation in AccessPoint Entity..
   * @param accessPoint
   */
  public void setAccessPoint(AccessPoint accessPoint) {
    this.accessPoint = accessPoint;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isHealthy() {
        return isHealthy;
    }

  public void setIsHealthy(boolean active) {
        this.isHealthy = active;
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TemperaStation that = (TemperaStation) o;
    return Objects.equals(id, that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "[Tempera station: id=%s; user=%s; enabled=%s; isHealthy=%s]"
        .formatted(this.id, this.user.getUsername(), this.enabled, this.isHealthy);
  }
}
