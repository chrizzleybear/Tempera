package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.State;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SuperiorTimeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime end;

    private State state;
}
