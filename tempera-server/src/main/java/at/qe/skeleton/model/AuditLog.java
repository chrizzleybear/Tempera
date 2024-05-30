package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime timeStamp;

  @ManyToOne
  private Userx triggeringUser;

  private LogEvent actionType;

  private LogAffectedType affectedType;

  private String message;

  /**
   * For creating AuditLogs only this Constructor should be used.
   *
   * @param message The message to store in the Log-Entry - should contain some information on what
   *     exactly happened.
   * @param affectedType The Class that was affected by the action which is being logged.
   * @param triggeringUser The Userx that initiated the Action that is being logged.
   * @param actionType A categorization of the Action that is being logged.
   */
  public AuditLog(Userx triggeringUser, LogEvent actionType, LogAffectedType affectedType, String message) {
    this.triggeringUser = triggeringUser;
    this.timeStamp = LocalDateTime.now();
    this.actionType = actionType;
    this.affectedType = affectedType;
    this.message = message;
  }

  public long getId() {
    return id;
  }

  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  public String getMessage() {
    return message;
  }

  public LogAffectedType getAffectedType() {
    return affectedType;
  }

  public LogEvent getActionType() {
    return actionType;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof AuditLog other)) {
      return false;
    }
    return Objects.equals(other.getId(), this.getId());
  }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
