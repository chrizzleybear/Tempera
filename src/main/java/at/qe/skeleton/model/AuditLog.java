package at.qe.skeleton.model;

import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime localDateTime;

    private String message;

    private LogAffectedType affectedType;

    @ManyToOne
    private Userx triggeringUser;

    private LogEvent event;
}
