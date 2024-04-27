package at.qe.skeleton.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * seems like this is a Value Object (in DDD lingo)
 */

@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="measurement_value")
    private double value;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    private Sensor sensor;

    protected Measurement() {
    }

    //todo: sanity checks for the value of the measurement?? temperature can't be 1000 degrees

    public Measurement(@NotNull double value, @NotNull LocalDateTime timestamp, @NotNull Sensor sensor) {
        this.value = value;
        this.timestamp = timestamp;
        this.sensor = sensor;
    }



    // können wir ein Measurement ohne sensor bauen? wo bauren wir measurements?

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString(){
        return "Measurement{id: %s,sensor: %s,value: %s}\n".formatted(id, sensor, value);
    }
}
