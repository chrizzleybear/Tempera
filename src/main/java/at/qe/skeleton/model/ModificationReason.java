package at.qe.skeleton.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ModificationReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String reason;

    public ModificationReason(String reason) {
        this.reason = reason;
    }
    public ModificationReason() {}

    public String getReason() {
        return reason;
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
        if (o.getClass() != ModificationReason.class) {
            return false;
        }
        ModificationReason other = (ModificationReason) o;
        return other.reason.equals(this.reason);
    }

    @Override
    public String toString(){
        return this.reason;
    }
}
