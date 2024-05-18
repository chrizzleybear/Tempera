package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class InternalRecordId implements Serializable {
    private LocalDateTime start;
    private ExternalRecord externalRecord;

    public InternalRecordId(LocalDateTime start, ExternalRecord externalRecord) {
        this.start = start;
        this.externalRecord = externalRecord;
    }

    public InternalRecordId() {
    }

    public LocalDateTime getStart() {
        return start;
    }

    public ExternalRecord getExternalRecord() {
        return externalRecord;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, externalRecord);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof InternalRecordId other)) {
            return false;
        }
        return other.start.equals(this.start) && other.externalRecord.equals(this.externalRecord);
    }
}
