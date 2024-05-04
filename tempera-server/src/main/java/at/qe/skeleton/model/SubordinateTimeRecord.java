package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Basic unit to measure time serverside. We want to preserve the original timerecords measured by
 * the TemperaStation and at the same time allow Users to assign these subordinate timerecords to
 * projects and divide them in smaller subunits. As soon as a SuperiorTimeRecord has been initiated,
 * a Subordinate TimeRecord with exactly the same characteristics as the SuperiorTimeRecord is
 * initialized. The End will be set to null and updated as soon as the new SuperiorTimeRecord is
 * received from TemperaStation. While End equals null, SubordinateTimeRecord may not be further
 * divided, meaning start and end stay as they are.
 *
 * <p>A SubordinateTimeRecord stores the Project and Group it is assigned to. But it does not have
 * to be assigned to a Project or a Group. Once a Group or Project gets deleted, all the TR that
 * were assigned to that Group or Project reference null as assigned Group/Project.
 */
@Entity
public class SubordinateTimeRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime start;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_end")
  private LocalDateTime end;

  // todo: should we add duration here as well?

  // bidirectional one-to-many association
  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project assignedProject;

  @ManyToOne
  @JoinColumn(name = "groupx_id")
  private Group assignedGroup;

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public SubordinateTimeRecord(LocalDateTime start) {
    this.start = start;
    this.end = null;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  protected SubordinateTimeRecord() {}
  ;

  public Project getAssignedProject() {
    return assignedProject;
  }

  public void setAssignedProject(Project assignedProject) {
    this.assignedProject = assignedProject;
  }

  public Group getAssignedGroup() {
    return assignedGroup;
  }

  public void setAssignedGroup(Group assignedGroup) {
    this.assignedGroup = assignedGroup;
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
    if (!(o instanceof SubordinateTimeRecord other)) {
      return false;
    }
    return other.start.equals(this.start);
  }

  @Override
  public String toString() {
    return "[SubordinateTimeRecord start: %s, end: %s, project: %s]"
        .formatted(
            start.toString(),
            end == null ? "null" : end,
            assignedProject == null ? "null" : assignedProject);
  }
}
