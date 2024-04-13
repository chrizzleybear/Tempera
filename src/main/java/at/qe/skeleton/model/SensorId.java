package at.qe.skeleton.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
//todo finish the empedded id key
public class SensorId implements Serializable {
    private Long id;
    private String temperaStationId;

    public SensorId(Long id, String temperaStationId) {
        this.id = id;
        this.temperaStationId = temperaStationId;
    }
    public SensorId(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemperaStationId() {
        return temperaStationId;
    }

    public void setTemperaStationId(String temperaStationId) {
        this.temperaStationId = temperaStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorId other)) return false;
        return id.equals(other.id) && temperaStationId.equals(other.temperaStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, temperaStationId);
    }
}
