package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Modification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeStamp;

    public Modification(String reason) {
        timeStamp = LocalDateTime.now();
        this.reason = reason;
    }

    public Modification() {}

    public String getReason() {
        return reason;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int hashCode() {
        return this.reason.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Modification other)) {
            return false;
        }
        return other.reason.equals(this.reason);
    }

    @Override
    public String toString(){
        return this.reason;
    }
}
