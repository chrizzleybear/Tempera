package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.dtos.GroupxProjectStateTimeDbDto;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.GroupxProjectRepository;
import at.qe.skeleton.repositories.ProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleUserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ProjectService {

 private final ProjectRepository projectRepository;
 private final UserxRepository userxRepository;
 private final  GroupRepository groupRepository;
 private final GroupxProjectRepository groupxProjectRepository;
 private final AuditLogService auditLogService;;

  public ProjectService(ProjectRepository projectRepository, AuditLogService auditLogService, UserxRepository userxRepository, GroupRepository groupRepository, GroupxProjectRepository groupxProjectRepository) {
    this.projectRepository = projectRepository;
    this.userxRepository = userxRepository;
    this.groupRepository = groupRepository;
    this.groupxProjectRepository = groupxProjectRepository;
    this.auditLogService = auditLogService;
  }

  private static final String USER_NOT_FOUND = "User not found";
  private static final String PROJECT_NOT_FOUND = "Project not found";
  private static final String GROUP_NOT_FOUND = "Group not found";

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
    auditLogService.logEvent(LogEvent.CREATE, LogAffectedType.PROJECT,
            "Created project " + project.getName() + " with manager " + project.getManager().getUsername() + "."
    );
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

  public SimpleProjectDto getSimpleProjectDtoById(Long id) throws CouldNotFindEntityException {
    return projectRepository.findSimpleProjectDtoById(id).orElseThrow(() -> new CouldNotFindEntityException("No Project with id %s found".formatted(id)));
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
    auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.PROJECT,
          "Project " + project.getName() + " was updated.");
    return projectRepository.save(project);
  }

  /**
   * This method creates a GroupxProject. If the GroupxProject already exists and is just deactivated, it will be reactivated.
   * @param projectId
   * @param groupId
   * @return
   * @throws IOException
   */
  @Transactional
  public GroupxProject createGroupxProject(Long projectId, Long groupId) throws IOException {
    // GroupxProject may already exist but be deactivated. In this case, we should reactivate gxp.
Optional<GroupxProject> groupxProjectOptional =
        groupxProjectRepository.findByGroup_IdAndProject_IdFetchGroupAndProjectEagerly(groupId, projectId);
if(groupxProjectOptional.isPresent()){
    GroupxProject groupxProject = groupxProjectOptional.get();
    if(groupxProject.isActive()){
        throw new IOException("GroupxProject with Group %s and Project %s already exists and is active");
    }
      if (!(groupxProject.getGroup().isActive()) || !(groupxProject.getProject().isActive())){
        throw new IOException("Either Group or Project are not active and cannot be added to a GroupxProject");
    }
      else {
        groupxProject.setActive(true);
          auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.PROJECT,
                  "Project " + groupxProject.getProject().getName() + " was linked to group " + groupxProject.getGroup().getName() + ".");
        return groupxProjectRepository.save(groupxProject);

    }
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
    auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.PROJECT,
          "Project " + project.getName() + " was linked to group " + group.getName() + ".");
    return groupxProjectRepository.save(groupxProject);
  }

  public Project loadProject(Long projectId) {
    return projectRepository.findFirstById(projectId);
  }

  @Transactional
  public void deleteProject(Long projectId) {
    List<GroupxProject> groupxProjects = groupxProjectRepository.findAllByProjectId(projectId);
    for (GroupxProject groupxProject : groupxProjects) {
      deactivateGroupxProject(groupxProject);
    }
    Project project = projectRepository.findFirstById(projectId);
    project.deactivate();
    projectRepository.save(project);
      auditLogService.logEvent(LogEvent.DELETE, LogAffectedType.PROJECT,
              "Project " + project.getName() + "was deleted.");
  }


  public Project reactivateProject(Long projectId) {
    Project project = projectRepository.findFirstById(projectId);
    project.activate();
    return projectRepository.save(project);
  }

  /**
   * This Method only conceptually removes a Group from  a Project. It sets
   * the isActive Flag for the GroupxProject to false, and removes all active contributors,
   * thus making it unaccessable for users. The TimeRecords stored on that Gxp are still present
   * and can be accessed in the accumulated Manager/ Grouplead Time Overview.
   * The Group Object will not be removed from the GroupxProject Object, so it can be reactivated later.
   * @param groupId
   * @param projectId
   * @throws CouldNotFindEntityException
   */
  public void removeGroupFromProject(Long groupId, Long projectId)
      throws CouldNotFindEntityException {
    Optional<GroupxProject> groupxProjectOptional =
        groupxProjectRepository.findByGroup_IdAndProject_IdFetchContributorsEagerly(groupId, projectId);
    if (groupxProjectOptional.isEmpty()) {
      throw new CouldNotFindEntityException(
          "GroupxProject with groupId %d and projectId %d are not present"
              .formatted(groupId, projectId));
    } else {
      GroupxProject groupxProject = groupxProjectOptional.get();
      deactivateGroupxProject(groupxProject);
        auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.PROJECT,
                "Group " + groupxProject.getGroup().getName() + " was removed from project " + groupxProject.getProject().getName() + ".");
    }
  }

  /**
   * This Method deactivates a GroupxProject. It sets the active flag to false and removes the
   * GroupxProject from all contributors, therefore making it unaccessable for them. This way,
   * managers and projectLeaders can still see the stored timerecords but users can`t add new records.
   * @param groupxProject
   */
  public void deactivateGroupxProject (GroupxProject groupxProject) {
    groupxProject.setActive(false);
    groupxProject.removeAllContributors();
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
    List<Groupx> groups = contributor.getGroups();
    if (!(groups.contains(groupxProject.getGroup()))) {
      throw new IllegalArgumentException(
          "User is not part of the group %s".formatted(groupxProject.getGroup()));
    }
    groupxProject.addContributor(contributor);
    auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.PROJECT,
          "User " + contributor.getUsername() +
                  " was added to project " + groupxProject.getProject().getName() +
                  " of group " + groupxProject.getGroup().getName() + ".");
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

  public List<Project> getProjectsByGroupLead(String username) {
    return projectRepository.findAllByGroupLead(username);
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
    auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.PROJECT,
          "User " + contributor.getUsername() +
                  " was removed from project " + groupxProject.getProject().getName() +
                  " of group " + groupxProject.getGroup().getName() + ".");
    groupxProject.removeContributor(contributor);
    groupxProjectRepository.save(groupxProject);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  public List<GroupxProjectStateTimeDbDto> gxpStateTimeDtosByManager(String username) {
    return groupxProjectRepository.getAllgxpStateTimeDtosByManager(username);
  }

  @PreAuthorize("hasAuthority('GROUPLEAD')")
  public List<GroupxProjectStateTimeDbDto> gxpStateTimeDtosByGroupLead(String username) {
    return groupxProjectRepository.getAllgxpStateTimeDtosByGroupLead(username);
  }

  public Set<SimpleGroupxProjectDto> getSimpleGroupxProjectDtoByUser(String username) {
    return groupxProjectRepository.getSimpleGroupxProjectDtoByUser(username);
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


  public List<SimpleProjectDto> getAllSimpleProjectDtos(){
    return projectRepository.findAllSimpleProjectDtos();
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

  public GroupxProject findByGroupAndProject(Long groupId, Long projectId) {
    return groupxProjectRepository.findByGroup_IdAndProject_Id(groupId, projectId).orElseThrow();

  }

  /**
   * Loads a GroupxProject by its group and project id while fetching Contributors List, rest is lazy loaded.
   * @param groupId
   * @param projectId
   * @return
   * @throws CouldNotFindEntityException
   */
  public GroupxProject findByGroupAndProjectDetailedC(Long groupId, Long projectId) throws CouldNotFindEntityException{
    return groupxProjectRepository
        .findByGroupAndProjectDetailedC(groupId, projectId)
        .orElseThrow(
            () ->
                new CouldNotFindEntityException(
                    "GroupxProject with groupId %s and projectId %s not found"
                        .formatted(groupId, projectId)));
  }

  /**
   * Loads a GroupxProject by its group and project id while fetchin group and project details
   * (which are usually lazy loaded) thus enabling the GroupxProject to deliver all the needed
   * information in one go.
   *
   * @param groupId
   * @param projectId
   * @return
   */
  public GroupxProject findByGroupAndProjectDetailedGP(Long groupId, Long projectId) throws CouldNotFindEntityException{
    return groupxProjectRepository
        .findByGroupAndProjectDetailedGP(groupId, projectId)
        .orElseThrow(
            () ->
                new CouldNotFindEntityException(
                    "GroupxProject with groupId %s and projectId %s not found"
                        .formatted(groupId, projectId)));
  }
}
