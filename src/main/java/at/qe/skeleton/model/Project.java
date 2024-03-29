package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;

    //bidirectional one-to-many association
    @OneToMany(mappedBy = "assignedProject")
    private List<SubordinateTimeRecord> subordinateTimeRecords;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Project() {}

    public List<SubordinateTimeRecord> getSubordinateTimeRecords() {
        return subordinateTimeRecords;
    }

    public void setSubordinateTimeRecords(List<SubordinateTimeRecord> subordinateTimeRecords) {
        this.subordinateTimeRecords = subordinateTimeRecords;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if (!(obj instanceof Project other)) {
            return false;
        }
        return other.name.equals(this.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
