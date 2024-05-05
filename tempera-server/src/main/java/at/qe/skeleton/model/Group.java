package at.qe.skeleton.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Groupx")
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  private String description;

  @ManyToOne private Userx groupLead;

  @ManyToMany private List<Userx> members;

  /**
   * For Creating Groups, this Constructor should be used.
   *
   * @param name the name of the Group.
   * @param description a short description of the purpose of the group
   * @param groupLead the Grouplead in Charge of that group
   */
  public Group(@NotNull String name, @NotNull String description, @NotNull Userx groupLead) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name must not be null or empty");
    }
    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description must not be null or empty");
    }
    this.name = name;
    this.description = description;
    this.groupLead = Objects.requireNonNull(groupLead, "GroupLead must not be null");
    this.members = new ArrayList<>();
  }

  protected Group() {
    this.members = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name must not be null or empty");
    }
    this.name = name;
  }

  public Long getId() {
    return id;
  }
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description must not be null or empty");
    }
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
    this.members = Objects.requireNonNull(members);
  }

  /**
   * Adds a Member to the Group, if the Member is already in the Group, the Member will be updated.
   *
   * @param member the Member to be added to the Group
   * @return true if member was not already in the group and false if member was updated.
   */
  public boolean addMember(@NotNull Userx member) {
    //Already checked for null in the annotation?
    if (member == null) {
      throw new IllegalArgumentException("Member must not be null");
    }
    if (!members.contains(member)) {
      return members.add(member);
    }
    //Why is this here?
    members.remove(member);
    members.add(member);
    return false;
  }

    public void removeMember(@NotNull Userx member) {
        if (!members.contains(member)) {
            throw new IllegalArgumentException("Member is not in the group");
        }
        members.remove(member);
    }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Group other)) {
      return false;
    }
    return other.name.equals(this.name);
  }

  @Override
  public String toString() {
    return name;
  }

}
