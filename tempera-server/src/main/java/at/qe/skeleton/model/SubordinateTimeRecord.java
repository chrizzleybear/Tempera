package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Basic unit to measure time serverside. We want to preserve the original timerecords measured by the TemperaStation
 * and at the same time allow Users to assign these subordinate timerecords to projects and divide them in smaller subunits.
 * As soon as a SuperiorTimeRecord is completed in the sense that a new one has started and a definitive end is certain,
 * a Subordinate TimeRecord with exactly the same characteristics as the SuperiorTimeRecord is initialized. A SubordinateTimeRecord
 * does not necessarily have a Project assigned.
 */
@Entity
public class SubordinateTimeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_end")
    private LocalDateTime end;

    //bidirectional one-to-many association
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project assignedProject;

    //todo: hier auch noch die entsprechende Gruppe, unter der dieser Time-Record eingeloggt wurde abspeichern,
    // dass wir in den management und Teamlead ansichten unabhängig von Usern sind (die theoretisch inzwischen gelöscht wurden)


    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SubordinateTimeRecord(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }
    protected SubordinateTimeRecord() {};

    @Override
    public int hashCode() {
        return start.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof SubordinateTimeRecord other)) {
            return false;
        }
        return other.start.equals(this.start);
    }

    @Override
    public String toString(){
        return "[SubordinateTimeRecord start: %s, end: %s, project: %s]"
                .formatted(start.toString(), end.toString(), assignedProject.toString());
    }
}
