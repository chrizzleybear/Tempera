package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This ValueObject is necessary to map the timerecords correctly to each project and group.
 * It is basically a helper Entity thats sole purpose is to represent the relationship between
 * a group that has been assigned a project and the user that is in the group and is also assigned to
 * that project.
 */
@Entity
@IdClass(value = GroupxProjectId.class)
@Table(name = "groupx_project_object")
public class GroupxProject {

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Groupx group;

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Project project;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Userx> contributors;

    @OneToMany(mappedBy = "groupxProject", cascade = CascadeType.ALL)
    private Set<InternalRecord> internalRecords;

    public GroupxProject() {
        contributors = new HashSet<>();
        internalRecords = new HashSet<>();
    }

    public void addContributor(Userx contributor) {
        contributors.add(contributor);
        contributor.addProject(project);
    }

    public void removeContributor(Userx contributor) {
        contributor.getGroupxProjects().remove(this);
        contributors.remove(contributor);
    }


    public Groupx getGroup() {
        return group;
    }

    public void addGroup(Groupx group) {
        this.group = group;
        group.getGroupxProjects().add(this);
    }

    public void removeGroup(){
        this.group.getGroupxProjects().remove(this);
        this.group = null;

    }

    public Project getProject() {
        return project;
    }

    public void addProject(Project project) {
        this.project = project;
        project.getGroupxProjects().add(this);
    }

    public void removeProject() {
        this.project.getGroupxProjects().remove(this);
        this.project = null;

    }

    public void addInternalRecord(InternalRecord internalRecord){
        internalRecords.add(internalRecord);
        internalRecord.setGroupxProject(this);
    }

    public Set<Userx> getContributors() {
        return contributors;
    }


    @Override
    public int hashCode() {
        return Objects.hash(group, project);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof GroupxProject other)) return false;
        return Objects.equals(group, other.group) && Objects.equals(project, other.project);
    }
}
