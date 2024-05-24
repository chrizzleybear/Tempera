package at.qe.skeleton.model;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Objects;

public class GroupxProjectId implements Serializable {
    private Groupx group;
    private Project project;

    public GroupxProjectId() {}

    public Groupx getGroup() {
        return group;
    }

    public void setGroup(Groupx group) {
        this.group = group;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, project);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof GroupxProjectId other)) return false;
        return Objects.equals(group, other.group) && Objects.equals(project, other.project);
    }
}
