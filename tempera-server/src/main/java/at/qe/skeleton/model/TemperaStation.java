package at.qe.skeleton.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Entity
public class TemperaStation implements Persistable<String> {

  // we set id manually (has to be configurable from admin)
  @Id private String id;
  @OneToOne private Userx user;
  private boolean enabled;

  private boolean active;

  // We need to implement Persistable since we set Id manually
  // the following strategy for the isNew Method comes from spring documentation:
  // https://docs.spring.io/spring-data/jpa/reference/jpa/entity-persistence.html
  @Transient private boolean isNew = true;

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

  /**
   * direct creation of TemperaStations should be avoided, use {@link
   * at.qe.skeleton.services.TemperaStationService#createTemperaStation} instead
   */
  public TemperaStation(@NotNull String id, boolean enabled, Userx user, boolean active) {
    this.user = user;
    this.id = Objects.requireNonNull(id);
    this.enabled = enabled;
    this.active = active;
  }

  protected TemperaStation() {}

  public void setUser(Userx user) {
    this.user = user;
  }

  public Userx getUser() {
    return user;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TemperaStation that = (TemperaStation) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return this.id;
  }
}
