package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class TemperaStation {

    // wir wählen UUID, weil sie nicht nur innerhalb eines DBS sondern weltweit einmalig sind.
    // Daher ist eine eindeutige identifikation Problemlos möglich.
    @Id
    private UUID id;
    @OneToOne
    private Userx user;
    private boolean enabled;
    @OneToMany
    private List<SuperiorTimeRecord> superiorTimeRecords;

    public TemperaStation () {
        this.id = UUID.randomUUID();
    }

    public void setUser (Userx user) {
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

    public List<SuperiorTimeRecord> getSuperiorTimeRecords() {
        return superiorTimeRecords;
    }

    public void addSuperiorTimeRecord(SuperiorTimeRecord superiorTimeRecord) {
        if (superiorTimeRecord == null) {
            throw new NullPointerException("superiorTimeRecord should not be null");
        }
        this.superiorTimeRecords.add(superiorTimeRecord);
    }
}
