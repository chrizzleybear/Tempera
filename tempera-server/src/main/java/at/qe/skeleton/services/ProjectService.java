package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.dtos.GroupxProjectStateTimeDto;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.GroupxProjectRepository;
import at.qe.skeleton.repositories.ProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.rest.frontend.dtos.GroupxProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ProjectService {

  @Autowired private ProjectRepository projectRepository;
  @Autowired private UserxRepository userxRepository;
  @Autowired private GroupRepository groupRepository;
  @Autowired private GroupxProjectRepository groupxProjectRepository;

  private static final String USER_NOT_FOUND = "User not found";
  private static final String PROJECT_NOT_FOUND = "Project not found";
  private static final String GROUP_NOT_FOUND = "Group not found";

  private Logger logger = Logger.getLogger("groupxProjectServiceLogger");

  // todo: write tests for project Service to test the functionality of GroupxProject in particular

  @Transactional
  public Project createProject(String name, String description, String manager) {
    Userx managerUser =
        userxRepository
            .findById(manager)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    Project project = new Project();
    project.setName(name);
    project.setDescription(description);
    project.setManager(managerUser);
    return projectRepository.save(project);
  }

  //  @Transactional(readOnly = true)
  public List<Project> getAllProjects() {
    return projectRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Optional<Project> getProjectById(Long id) {
    return projectRepository.findById(id);
  }

  @Transactional
  public Project updateProject(Long id, String name, String description, String manager) {
    Project project =
        projectRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Project with ID " + id + " not found"));
    project.setName(name);
    project.setDescription(description);
    project.setManager(
        userxRepository
            .findById(manager)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND)));
    return projectRepository.save(project);
  }

  // todo: for the following methods develop queries in the groupxprojectRepository that make it
  // easy to handle everyting
  @Transactional
  public GroupxProject createGroupxProject(Long projectId, Long groupId) throws IOException {
    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException(PROJECT_NOT_FOUND));
    Groupx group =
        groupRepository
            .findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException(GROUP_NOT_FOUND));
    if (groupxProjectRepository.findByGroup_IdAndProject_Id(groupId, projectId).isPresent()) {
      throw new IOException("GroupxProject with Group %s and Project %s already exists");
    }
    GroupxProject groupxProject = new GroupxProject();
    groupxProject.addProject(project);
    groupxProject.addGroup(group);
    return groupxProjectRepository.save(groupxProject);
  }

  public Project loadProject(Long projectId) {
    return projectRepository.findFirstById(projectId);
  }

  @Transactional
  public void deleteProject(Long projectId) {
    List<GroupxProject> groupxProjects = groupxProjectRepository.findAllByProjectId(projectId);
    // todo: decide what happens to the groupxproject object if project gets deleted...
    for (GroupxProject groupxProject : groupxProjects) {
      groupxProject.removeProject();
      groupxProjectRepository.save(groupxProject);
    }
    Project project = projectRepository.findFirstById(projectId);
    projectRepository.delete(project);
  }

  public void removeGroupFromProject(Long groupId, Long projectId)
      throws CouldNotFindEntityException {
    Optional<GroupxProject> groupxProjectOptional =
        groupxProjectRepository.findByGroup_IdAndProject_Id(groupId, projectId);
    if (groupxProjectOptional.isEmpty()) {
      throw new CouldNotFindEntityException(
          "GroupxProject with groupId %d and projectId %d are not present"
              .formatted(groupId, projectId));
    } else {
      GroupxProject groupxProject = groupxProjectOptional.get();
      // ohne Groupx kann das GroupxProject nicht existieren, da das Teil der Id ist. derweil
      // löschen wir einfach mal das GroupxProject
      // todo: eine geeignete lösch-policy überlegen: ein feld in GroupxProject setzen mit "is
      // active" oder so
      deleteGroupxProject(groupxProject);
    }
  }

  public void deleteGroupxProject(GroupxProject groupxProject) {
    groupxProject.removeGroup();
    groupxProject.removeProject();
    groupxProject
        .getContributors()
        .forEach(
            contributor -> {
              contributor.getGroupxProjects().remove(groupxProject);
              userxRepository.save(contributor);
            });
    groupxProject.removeInternalRecords();
    groupxProjectRepository.save(groupxProject);
  }

  @Transactional
  public void addContributor(Long groupId, Long projectId, String username)
      throws CouldNotFindEntityException {
    GroupxProject groupxProject =
        groupxProjectRepository
            .findByGroup_IdAndProject_Id(groupId, projectId)
            .orElseThrow(
                () ->
                    new CouldNotFindEntityException(
                        "Could not find GroupxProject with GroupId %d and ProjectID %d"
                            .formatted(groupId, projectId)));
    Userx contributor =
        userxRepository
            .findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

    if (groupxProject.getContributors().contains(contributor)) {
      throw new IllegalArgumentException("User is already a contributor");
    }
    if (!(contributor.getGroups().contains(groupxProject.getGroup()))) {
      throw new IllegalArgumentException(
          "User is not part of the group %s".formatted(groupxProject.getGroup()));
    }
    groupxProject.addContributor(contributor);
    groupxProjectRepository.save(groupxProject);
  }

  public List<Project> getProjectsByGroupId(Long groupId) {
    if (groupRepository.findById(groupId).isEmpty()) {
      throw new IllegalArgumentException(GROUP_NOT_FOUND);
    }
    List<GroupxProject> groupxProjects = groupxProjectRepository.findAllByGroup_Id(groupId);
    return groupxProjects.stream().map(GroupxProject::getProject).toList();
  }

  public List<Project> getProjectsByManager(String username) {
    return projectRepository.findAllByManager_Username(username);
  }

  public List<Project> getProjectsByContributor(Userx user) {
    List<GroupxProject> groupxProjects =
        groupxProjectRepository.findAllByContributorsContains(user);
    return groupxProjects.stream().map(GroupxProject::getProject).toList();
  }

  @Transactional
  public void removeContributor(Long groupId, Long projectId, String username)
      throws CouldNotFindEntityException {
    GroupxProject groupxProject =
        groupxProjectRepository
            .findByGroup_IdAndProject_Id(groupId, projectId)
            .orElseThrow(
                () ->
                    new CouldNotFindEntityException(
                        "Could not find GroupxProject with GroupId %d and ProjectID %d"
                            .formatted(groupId, projectId)));

    Userx contributor =
        userxRepository
            .findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    groupxProject.removeContributor(contributor);
    groupxProjectRepository.save(groupxProject);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  public List<GroupxProjectStateTimeDto> gxpStateTimeDtosByManager(String username) {
    return groupxProjectRepository.getAllGroupxProjectStateTimeDtos(username);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  public void deleteProject(Project project) {
    projectRepository.delete(project);
  }

  @PreAuthorize("isAuthenticated()")
  public void saveProject(Project project) {
    projectRepository.save(project);
  }

  public List<GroupxProject> findGroupxProjectsByContributorAndProjectId(
      Userx contributor, Long projectId) {
    return groupxProjectRepository.findAllByProjectIdAndContributorsContaining(
        projectId, contributor);
  }

  public List<GroupxProject> findAllGroupxProjectsOfAUser(Userx user) {
    return groupxProjectRepository.findAllByContributorsContains(user);
  }

  public GroupxProject saveGroupxProject(GroupxProject groupxProject) {
    return groupxProjectRepository.save(groupxProject);
  }

  public List<GroupxProject> findAllGroupxProjectsByProjectId(Long projectId) {
    return groupxProjectRepository.findAllByProjectId(projectId);
  }

  public List<Project> findAllProjects() {
    return projectRepository.findAll();
  }

  public List<SimpleUserDto> findAllContributorsByProjectId(Long projectId) {
    return groupxProjectRepository.findAllContributorsByProject_Id(projectId);
  }

  public List<GroupxProject> getGroupxProjectsByGroupId(Long groupId) {
    return groupxProjectRepository.findAllByGroup_Id(groupId);
  }
}
