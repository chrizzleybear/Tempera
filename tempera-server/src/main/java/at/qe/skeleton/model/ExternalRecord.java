package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.InternalRecordOutOfBoundsException;
import at.qe.skeleton.model.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The basic unit to measure Time Blocks in the TemperaStation. The End field remains null until
 * TemperaStation messages that a new TimeRecord has started. At this point the End will be filled
 * with a value and at the same time a InternalRecord with the identical properties gets
 * initialized.
 */
@Entity
public class ExternalRecord {

  @EmbeddedId
  ExternalRecordId id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_end")
  private LocalDateTime end;

  @ManyToOne
  @MapsId("userName")
  Userx user;

  private long duration;

  @OneToMany(mappedBy = "externalRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<InternalRecord> internalRecords;

  @Enumerated(EnumType.STRING)
  private State state;

  protected ExternalRecord() {
    internalRecords = new ArrayList<>();
  }

  public ExternalRecord(
      @NotNull Userx user,
      @NotNull LocalDateTime start,
      long duration,
      LocalDateTime end,
      @NotNull State state) {
    this.user = Objects.requireNonNull(user);
    this.id = new ExternalRecordId(start, user.getUsername());
    this.end = end;
    this.state = Objects.requireNonNull(state);
    this.duration = duration;
    internalRecords = new ArrayList<>();
  }

  public long getDuration() {
    return duration;
  }

  public Userx getUser() {
    return user;
  }

  public void setUser(Userx user) {
    this.user = user;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public LocalDateTime getStart() {
    return id.getStart();
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  public List<InternalRecord> getInternalRecords() {
    return internalRecords;
  }

  /**
   * adds the given InternalRecord to the list of InternalRecords. At the same time
   * InternalRecord's externalRecord field will be set to this ExternalRecord.
   * @param internalRecord
   */
  public void addInternalRecord(InternalRecord internalRecord) {
    if (internalRecord == null) {
      throw new NullPointerException("InternalRecord should not be null");
    }
    if (internalRecord.getStart().isBefore(this.id.getStart())) {
      throw new InternalRecordOutOfBoundsException(
          "internalRecord should not start before its superiorTimeRecord.");
    }
    this.internalRecords.add(internalRecord);
    internalRecord.setExternalRecord(this);
  }

  public void removeInternalRecord(InternalRecord internalRecord) {
    this.internalRecords.remove(internalRecord);
    internalRecord.setExternalRecord(null);
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public ExternalRecordId getId() {
    return id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof ExternalRecord other)) {
      return false;
    }
    return other.id.equals(this.id);
  }

  public void setId(ExternalRecordId id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "[ExternalRecord start: %s, end: %s, state: %s]"
        .formatted(id.getStart().toString(), end == null ? "null" : end, state.toString());
  }
}
