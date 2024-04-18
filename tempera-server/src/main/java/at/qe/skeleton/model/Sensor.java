package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.SensorType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TemperaStation temperaStation;

    private SensorType sensorType;

    private String unit;

    @OneToMany
    private List<Measurement> measurementList;

    public Sensor(SensorType sensorType, String unit) {
        this.sensorType = sensorType;
        this.unit = unit;
        this.measurementList = new ArrayList<>();
    }
    protected Sensor() {}

    public TemperaStation getTemperaStation() {
        return temperaStation;
    }

    public List<Measurement> getMeasurementList() {
        return measurementList;
    }

    public long getId() {
        return id;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void addMeasurement(Measurement measurement){
        this.measurementList.add(measurement);
    }
}
