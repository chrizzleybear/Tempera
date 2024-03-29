package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String description;

    @ManyToOne
    private Userx groupLead;

    @ManyToMany
    private List<Userx> members;

    public Group(String name, String description, Userx groupLead) {
        this.name = name;
        this.description = description;
        this.groupLead = groupLead;
        this.members = new ArrayList<>();
    }

    public Group() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Userx getGroupLead() {
        return groupLead;
    }

    public void setGroupLead(Userx groupLead) {
        this.groupLead = groupLead;
    }

    public List<Userx> getMembers() {
        return members;
    }

    public void setMembers(List<Userx> members) {
        this.members = members;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (!(obj instanceof Group other)){
            return false;
        }
        return other.name.equals(this.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
