package at.qe.skeleton.model;

import jakarta.persistence.*;

@Entity
public class Measurement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Sensor sensor;

    private double value;

    public Measurement (double value, Sensor sensor) {
        this.value = value;
        this.sensor = sensor;
    }

    // JPA needs a non-arg Constructor
    protected Measurement () {}

    public long getId() {
        return id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public double getValue() {
        return value;
    }


}
