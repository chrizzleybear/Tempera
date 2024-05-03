package at.qe.skeleton.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.MapsId;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class SuperiorTimeRecordId implements Serializable {
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start;

    private String userName;

    public LocalDateTime getStart() {
        return start;
    }

    public SuperiorTimeRecordId(LocalDateTime start, String userName) {
        this.start = start;
        this.userName = userName;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
