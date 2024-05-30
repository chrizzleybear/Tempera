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
  private ZonedDateTime timeStamp;

  @ManyToOne private Userx triggeringUser;

  private LogEvent actionType;

  private LogAffectedType affectedType;

  private String message;

  public AuditLog(Userx triggeringUser, LogEvent actionType, LogAffectedType affectedType, String message) {
    this.timeStamp = ZonedDateTime.now();
    this.triggeringUser = triggeringUser;
    this.actionType = actionType;
    this.affectedType = affectedType;
    this.message = message;
  }

  /**
   * For creating AuditLogs only this Constructor should be used.
   *
   * @param message The message to store in the Log-Entry - should contain some information on what
   *     exactly happened.
   * @param affectedType The Class that was affected by the action which is being logged.
   * @param triggeringUser The Userx that initiated the Action that is being logged.
   * @param event A categorization of the Action that is being logged.
   */


  protected AuditLog() {}

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

  public Userx getTriggeringUser() {
    return triggeringUser;
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
