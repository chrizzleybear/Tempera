package at.qe.skeleton.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.model.enums.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 *
 * <p>This class is part of the skeleton project provided for students of the course "Software
 * Engineering" offered by the University of Innsbruck.
 */
@Entity
public class Userx implements Persistable<String>, Serializable, Comparable<Userx> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 100)
  private String username;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JsonIgnore
  private Userx createUser;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @JsonIgnore
  private LocalDateTime createDate;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private Userx updateUser;

  @Temporal(TemporalType.TIMESTAMP)
  @JsonIgnore
  private LocalDateTime updateDate;

  @OneToOne (mappedBy = "user", fetch = FetchType.LAZY) private TemperaStation temperaStation;

  @ManyToMany(mappedBy = "contributors")
  private Set<GroupxProject> groupxProjects;

  @ManyToMany(mappedBy = "members", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Groupx> groups;

  private String password;

  private String firstName;
  private String lastName;
  private String email;
  boolean enabled;
  @Enumerated(EnumType.STRING)
  private State state;
  @Enumerated(EnumType.STRING)
  private Visibility stateVisibility;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(name = "default_group_id", referencedColumnName = "group_id"),
    @JoinColumn(name = "default_project_id", referencedColumnName = "project_id")

  })
  private GroupxProject defaultGroupxProject;

  @ElementCollection(targetClass = UserxRole.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "Userx_UserxRole")
  @Enumerated(EnumType.STRING)
  private Set<UserxRole> roles;


  public Userx() {}

  public Userx(String username, String email, String password, LocalDateTime createDate) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.createDate = createDate;
  }

  public List<Groupx> getGroups() {
    return groups;
  }

  public TemperaStation getTemperaStation() {
    return temperaStation;
  }

  public void setTemperaStation(TemperaStation temperaStation) {
    this.temperaStation = temperaStation;
    temperaStation.setUser(this);
  }

  public void removeTemperaStation() {
    if (this.temperaStation != null) {
      this.temperaStation.setUser(null);
      this.temperaStation = null;
    }
  }

  public Set<GroupxProject> getGroupxProjects() {
    return groupxProjects;
  }
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set<UserxRole> getRoles() {
    return roles;
  }

  public void setGroupxProjects(Set<GroupxProject> groupxProjects) {
    this.groupxProjects = groupxProjects;
  }

  public void setRoles(Set<UserxRole> roles) {
    this.roles = roles;
  }

  public void addRole(UserxRole role) {
    if (this.roles.contains(role)) {
      return;
    }
    this.roles.add(role);
  }

  public void addGroup(Groupx group) {
    groups.add(group);
    group.getMembers().add(this);
  }

  public void removeGroup(Groupx group) {
    groups.remove(group);
    group.getMembers().remove(this);
  }

  public GroupxProject getDefaultGroupxProject() {
    return defaultGroupxProject;
  }

  public void setGroups(List<Groupx> groupxes) {
    this.groups = groupxes;
  }

  public void setDefaultGroupxProject(GroupxProject defaultGroupxProject) {
    this.defaultGroupxProject = defaultGroupxProject;
  }

  public Userx getCreateUser() {
    return createUser;
  }

  public void setCreateUser(Userx createUser) {
    this.createUser = createUser;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public Userx getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(Userx updateUser) {
    this.updateUser = updateUser;
  }

  public LocalDateTime getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(LocalDateTime updateDate) {
    this.updateDate = updateDate;
  }

  public State getState() {
    return state;
  }

  public Visibility getStateVisibility() {
    return stateVisibility;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void setStateVisibility(Visibility stateVisibility) {
    if (this.roles.contains(UserxRole.ADMIN)) {
      this.stateVisibility = Visibility.PUBLIC;
      return;
    }
    this.stateVisibility = stateVisibility;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.username);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Userx)) {
      return false;
    }
    final Userx other = (Userx) obj;
    if (!Objects.equals(this.username, other.getUsername())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "at.qe.skeleton.model.User[ id=" + username + " ]";
  }

  @Override
  public String getId() {
    return getUsername();
  }

  public void setId(String id) {
    setUsername(id);
  }

  @Override
  public boolean isNew() {
    return (null == createDate);
  }

  @Override
  public int compareTo(Userx o) {
    return this.username.compareTo(o.getUsername());
  }
}
