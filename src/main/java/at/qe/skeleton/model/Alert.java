package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.AlertType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Threshold threshold;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeStamp;

    private AlertType alertType;

    private double value;

    private String message;

    private boolean acknowledged;


    /**
     * Constructor for Alerts with AlertType Threshold_Warning
     *
     * @param alertType
     * @param threshold that was violated
     * @param value the actual value of the measurement, that violated the threshold.
     */
    public Alert(AlertType alertType, Threshold threshold, double value) {

        this.threshold = threshold;
        this.alertType = alertType;
        this.value = value;
    }

    /**
     * Constructor for Alerts with AlertType transmission_error or Data_anomalies.
     * @param alertType
     * @param message the message, that is supposed to be shown to user about the nature of the error or anomaly.
     */
    public Alert(AlertType alertType, String message){
        this.alertType = alertType;
        this.message = message;

    }
}
