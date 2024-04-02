package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.SubordinateTimeRecordOutOfBoundsException;
import at.qe.skeleton.model.enums.State;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class SuperiorTimeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_end")
    private LocalDateTime end;

    @ManyToOne
    private TemperaStation temperaStation;

    @OneToMany
    private List<SubordinateTimeRecord> subordinateRecords;

    private State state;

    public SuperiorTimeRecord() {};
    public SuperiorTimeRecord(TemperaStation temperaStation, LocalDateTime start, LocalDateTime end, State state) {
        this.temperaStation = temperaStation;
        this.start = start;
        this.end = end;
        this.state = state;
    };

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public List<SubordinateTimeRecord> getSubordinateRecords() {
        return subordinateRecords;
    }

    public void addSubordinateTimeRecord(SubordinateTimeRecord subordinateTimeRecord) {
        if (subordinateTimeRecord == null) {
            throw new NullPointerException("SubordinateTimeRecord should not be null");
        }
        if(subordinateTimeRecord.getStart().isBefore(this.start)){
            throw new SubordinateTimeRecordOutOfBoundsException
                    ("subordinateTimeRecord should not start before its superiorTimeRecord.");
        }
        if(subordinateTimeRecord.getEnd().isAfter(this.end) && subordinateTimeRecord.getEnd().isAfter(LocalDateTime.now())){
            throw new SubordinateTimeRecordOutOfBoundsException(
                    "subordinateTimeRecord should not end after its superiorTimeRecord. If SuperiorTimeRecord has not yet" +
                            "ended, SubordinateTimeRecord should not extend beyond now."
            );
        }
        this.subordinateRecords.add(subordinateTimeRecord);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public TemperaStation getTemperaStation() {
        return temperaStation;
    }

    @Override
    public int hashCode() {
        return start.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof SuperiorTimeRecord other)) {
            return false;
        }
        return other.start.equals(this.start);
    }

    @Override
    public String toString(){
        return "[SuperiorTimeRecord start: %s, end: %s, state: %s]".formatted(start.toString(), end.toString(), state.toString());
    }
}
