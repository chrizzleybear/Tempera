package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.GroupxProjectRepository;
import at.qe.skeleton.repositories.ProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.slf4j.LoggerFactory;
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

  private static final String USER_NOT_FOUND = "User not found";
  private static final String PROJECT_NOT_FOUND = "Project not found";
  private static final String GROUP_NOT_FOUND = "Group not found";
  @Autowired private GroupService groupService;
  @Autowired private GroupxProjectRepository groupxProjectRepository;

  private Logger logger = Logger.getLogger("groupxProjectServiceLogger");

  //todo: write tests for project Service to test the functionality of GroupxProject in particular

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

  @Transactional(readOnly = true)
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
  public void addGroupToProject(Long projectId, Long groupId) throws IOException {
    if (groupId == null) {
      throw new NullPointerException("Contributor can not be null");
    }
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
    groupxProjectRepository.save(groupxProject);
  }

  public Project loadProject(Long projectId) {
    return projectRepository.findFirstById(projectId);
  }

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
      groupxProject.removeGroup();
      groupxProjectRepository.save(groupxProject);
    }
  }

  @Transactional
  public void addContributor(Long groupId, Long projectId, String username) throws CouldNotFindEntityException {
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
    if (!(contributor.getGroups().contains(groupxProject.getGroup()))){
      throw new IllegalArgumentException("User is not part of the group %s".formatted(groupxProject.getGroup()));
    }
    groupxProject.addContributor(contributor);
    groupxProjectRepository.save(groupxProject);
  }

  public List<Project> getProjectsForGroups(Long groupId) {
    if (groupRepository.findById(groupId).isEmpty()) {
      throw new IllegalArgumentException(GROUP_NOT_FOUND);
    }
    return projectRepository.findByGroupId(groupId);
  }

  public List<Project> getProjectsByManager(String username) {
    return projectRepository.findAllByManager_Username(username);
  }

  public List<Project> getProjectsByContributor(String username) {
    return projectRepository.findAllByContributors_Username(username);
  }

  @Transactional
  public void removeContributor(Long groupId, Long projectId, String username) throws CouldNotFindEntityException {
    GroupxProject groupxProject = groupxProjectRepository.findByGroup_IdAndProject_Id(groupId, projectId).orElseThrow(() -> new CouldNotFindEntityException("Could not find GroupxProject with GroupId %d and ProjectID %d"
            .formatted(groupId, projectId)));

    Userx contributor =
        userxRepository
            .findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    groupxProject.removeContributor(contributor);
    groupxProjectRepository.save(groupxProject);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  public void deleteProject(Project project) {
    projectRepository.delete(project);
  }

  @PreAuthorize("isAuthenticated()")
  public void saveProject(Project project) {
    projectRepository.save(project);
  }

  public List<GroupxProject> findGroupxProjectsByContributorAndProjectId(Userx contributor, Long projectId){
    return groupxProjectRepository.findAllByProjectIdAndContributorsContaining(projectId, contributor);
  }

  public GroupxProject saveGroupxProject(GroupxProject groupxProject) {
    return groupxProjectRepository.save(groupxProject);
  }
}
