package at.qe.skeleton.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class ExternalRecordId implements Serializable {
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime start;

  private String userName;

  public LocalDateTime getStart() {
    return start;
  }

  public ExternalRecordId(LocalDateTime start, String userName) {
    this.start = start;
    this.userName = userName;
  }

  protected ExternalRecordId() {}

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (!(o instanceof ExternalRecordId other)) return false;

    if (!Objects.equals(start, other.start)) return false;
    return Objects.equals(userName, other.getUserName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, userName);
  }
}
