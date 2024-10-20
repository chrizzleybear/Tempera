package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import org.springframework.security.access.prepost.PreAuthorize;

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
@NamedEntityGraph(name = "GroupxProject.detail", attributeNodes = {
        @NamedAttributeNode("group"),
        @NamedAttributeNode("project"),
})
public class GroupxProject implements Persistable<GroupxProjectId>{

    @Transient private boolean isNew = true;

    @Override
    public GroupxProjectId getId() {
        GroupxProjectId groupxProjectID = new GroupxProjectId();
        groupxProjectID.setProject(project);
        groupxProjectID.setGroup(group);
        return groupxProjectID;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Groupx group;

    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Project project;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Userx> contributors;

    @OneToMany(mappedBy = "groupxProject", cascade = CascadeType.ALL)
    private Set<InternalRecord> internalRecords;

    private boolean isActive;

    public GroupxProject() {
        contributors = new HashSet<>();
        internalRecords = new HashSet<>();
        isActive = true;
    }

    @PreAuthorize("hasRole('GROUPLEAD') or hasRole('ADMIN')")
    public void addContributor(Userx contributor) {
        if (!isActive){
            throw new IllegalStateException("GroupxProject is not active");
        }
        contributors.add(contributor);
    }
    @PreAuthorize("hasRole('GROUPLEAD') or hasRole('ADMIN')")
    public void removeContributor(Userx contributor) {
        contributor.getGroupxProjects().remove(this);
        contributors.remove(contributor);
    }
    public void removeInternalRecords(){
        internalRecords.forEach(internalRecord -> internalRecord.setGroupxProject(null));
        internalRecords.clear();
    }

    @PreAuthorize("hasRole('GROUPLEAD') or hasRole('ADMIN') or hasRole('MANAGER')")
    public void removeAllContributors(){
        contributors.forEach(contributor -> contributor.getGroupxProjects().remove(this));
        contributors.clear();
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
        //todo remove policy Überlegen
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Project getProject() {
        return project;
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public void addProject(Project project) {
        this.project = project;
        project.getGroupxProjects().add(this);
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public void removeProject() {
        this.project.getGroupxProjects().remove(this);
        this.project = null;

    }

    public void addInternalRecord(InternalRecord internalRecord){
        internalRecords.add(internalRecord);
        internalRecord.setGroupxProject(this);
    }

    public Set<Userx> getContributors() {
        return new HashSet<>(contributors); // return mutable collection
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
        return Objects.equals(group, other.getGroup()) && Objects.equals(project, other.getProject());
    }

    @Override
    public String toString() {
        return "GroupxProject{" +
                "group=" + group +
                ", project=" + project +
                '}';
    }
}
