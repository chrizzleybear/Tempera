package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  private String description;

  private boolean isActive;
  @ManyToOne(fetch = FetchType.LAZY) private Userx manager;


  @OneToMany(mappedBy = "project")
  private Set<GroupxProject> groupxProjects;


  public Project(String name, String description, Userx manager) {
    this.name = name;
    this.description = description;
    this.manager = manager;
    this.isActive = true;
//    this.contributors = new ArrayList<>();
  }

  public Project() {
    this.isActive = true;
  }

  public void activate() {
    this.isActive = true;
  }
  public void deactivate() {
    this.isActive = false;
  }

  public boolean isActive() {
        return isActive;
    }
  public Set<GroupxProject> getGroupxProjects() {
    return groupxProjects;
  }

  public Userx getManager() {
    return manager;
  }

//  public List<Userx> getContributors() {
//    return contributors;
//  }

//  public List<Groupx> getGroups() {
//    return groups;
//  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setManager(Userx manager) {
    if (manager == null) {
      throw new NullPointerException("User should not be null when set as ProjectManager");
    }
    this.manager = manager;
  }

//  public void addContributor(Userx contributor) {
//    if (contributor == null) {
//      throw new NullPointerException("Contributor should not be null when added to Project");
//    }
//    this.contributors.add(contributor);
//    contributor.getProjects().add(this);
//  }
//
//  public void removeContributor(Userx contributor) {
//    if (contributor == null) {
//      throw new NullPointerException("Contributor should not be null when removed from Project");
//    }
//    this.contributors.remove(contributor);
//    contributor.getProjects().remove(this);
//  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Long getId() {
    return id;
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
    if (!(obj instanceof Project other)) {
      return false;
    }
    return other.getName().equals(this.name);
  }

  @Override
  public String toString() {
    return name;
  }
}
