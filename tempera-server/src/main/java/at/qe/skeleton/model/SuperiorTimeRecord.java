package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.SubordinateTimeRecordOutOfBoundsException;
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
 * with a value and at the same time a SubordinateTimeRecord with the identical properties gets
 * initialized.
 */
@Entity
public class SuperiorTimeRecord {

  @EmbeddedId
  SuperiorTimeRecordId id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_end")
  private LocalDateTime end;

  @ManyToOne
          @MapsId("userName")
  Userx user;

  private long duration;

  @OneToMany @Fetch(FetchMode.JOIN) private List<SubordinateTimeRecord> subordinateRecords;

  @Enumerated(EnumType.STRING)
  private State state;

  protected SuperiorTimeRecord() {
    subordinateRecords = new ArrayList<>();
  }

  public SuperiorTimeRecord(
          @NotNull Userx user, @NotNull LocalDateTime start, long duration, LocalDateTime end, @NotNull State state) {
    this.user = Objects.requireNonNull(user);
    this.id = new SuperiorTimeRecordId(start, user.getUsername());
    this.end = end;
    this.state = Objects.requireNonNull(state);
    this.duration = duration;
    subordinateRecords = new ArrayList<>();
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

  public List<SubordinateTimeRecord> getSubordinateRecords() {
    return subordinateRecords;
  }

  public void addSubordinateTimeRecord(SubordinateTimeRecord subordinateTimeRecord) {
    if (subordinateTimeRecord == null) {
      throw new NullPointerException("SubordinateTimeRecord should not be null");
    }
    if (subordinateTimeRecord.getStart().isBefore(this.id.getStart())) {
      throw new SubordinateTimeRecordOutOfBoundsException(
          "subordinateTimeRecord should not start before its superiorTimeRecord.");
    }
    if (subordinateTimeRecord.getEnd() != null && (subordinateTimeRecord.getEnd().isAfter(this.end)
        || subordinateTimeRecord.getEnd().isAfter(LocalDateTime.now()))) {
      throw new SubordinateTimeRecordOutOfBoundsException(
          "subordinateTimeRecord should not end after its superiorTimeRecord. If SuperiorTimeRecord has not yet"
              + "ended, SubordinateTimeRecord should not extend beyond now.");
    }
    this.subordinateRecords.add(subordinateTimeRecord);
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public SuperiorTimeRecordId getId() {
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
    if (!(o instanceof SuperiorTimeRecord other)) {
      return false;
    }
    return other.id.equals(this.id);
  }

  public void setId(SuperiorTimeRecordId id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "[SuperiorTimeRecord start: %s, end: %s, state: %s]"
        .formatted(id.getStart().toString(), end == null ? "null" : end, state.toString());
  }
}
