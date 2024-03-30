package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SubordinateTimeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime end;

    //bidirectional one-to-many association
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project assignedProject;

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
