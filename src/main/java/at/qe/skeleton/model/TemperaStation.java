package at.qe.skeleton.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

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
}
