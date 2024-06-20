package at.qe.skeleton.services;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.GroupxProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("application")
public class GroupService {

  private static final String INVALID_GROUP_ID = "Invalid group lead ID";
  private static final String INVALID_GROUPLEAD_ID = "Invalid group lead ID";
  private static final String INVALID_MEMBER_ID = "Invalid member ID";
  private static final String INVALID_MANAGER_ID = "Invalid manager ID";
  private final ProjectService projectService;
  private final AuditLogService auditLogService;
  private final UserxRepository userxRepository;

  private final GroupRepository groupRepository;

  private final GroupxProjectRepository groupxProjectRepository;

  public GroupService(
      ProjectService projectService,
      UserxRepository userxRepository,
      GroupRepository groupRepository,
      AuditLogService auditLogService,
      GroupxProjectRepository groupxProjectRepository) {
    this.auditLogService = auditLogService;
    this.projectService = projectService;
    this.userxRepository = userxRepository;
    this.groupRepository = groupRepository;
    this.groupxProjectRepository = groupxProjectRepository;
  }

  /**
   * will mostly be used to get all active groups as options for adding to a project
   *
   * @return
   */
  public List<SimpleGroupDto> getAllGroups() {
    List<Groupx> groups = groupRepository.findAll();
    return groups.stream()
        .map(
            group ->
                new SimpleGroupDto(
                    String.valueOf(group.getId()),
                    group.isActive(),
                    group.getName(),
                    group.getDescription(),
                    group.getGroupLead().getId()))
        .toList();
  }

  @Transactional
  public Groupx createGroup(String name, String description, String groupLeadId) {
    Userx groupLead =
        userxRepository
            .findById(groupLeadId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
    groupLead.addRole(UserxRole.GROUPLEAD);
    Groupx group = new Groupx(name, description, groupLead);
    auditLogService.logEvent(
        LogEvent.CREATE,
        LogAffectedType.GROUP,
        "New Group with name "
            + group.getName()
            + " and group leader "
            + groupLead.getUsername()
            + " was created.");
    return groupRepository.save(group);
  }

  @PreAuthorize("hasAuthority('MANAGER') or hasAnyAuthority('ADMIN')")
  @Transactional
  public Groupx updateGroup(Long groupId, String name, String description, String groupLeadId) {
    Groupx group =
        groupRepository
            .findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    Userx groupLead =
        userxRepository
            .findById(groupLeadId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
    groupLead.addRole(UserxRole.GROUPLEAD);
    group.setName(name);
    group.setDescription(description);
    group.setGroupLead(groupLead);
    auditLogService.logEvent(
        LogEvent.EDIT,
        LogAffectedType.GROUP,
        "Group with name " + group.getName() + " was edited.");
    return groupRepository.save(group);
  }

  @PreAuthorize("hasAuthority('MANAGER') or hasAnyAuthority('ADMIN')")
  public Groupx reactivateGroup(Long groupId){
    Groupx group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    group.activate();
    return groupRepository.save(group);
  }

  @PreAuthorize("hasAuthority('MANAGER') or hasAnyAuthority('ADMIN')")
  @Transactional
  public void deleteGroup(Long groupId) {
    Groupx group =
        groupRepository
            .findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    List<GroupxProject> connectedGxps = groupxProjectRepository.findAllByGroup_Id(groupId);
    connectedGxps.forEach(projectService::deactivateGroupxProject);
    group.deactivate();
    group.removeAllMembers();
    groupRepository.save(group);
    auditLogService.logEvent(
        LogEvent.DELETE,
        LogAffectedType.GROUP,
        "New Group with name " + group.getName() + " was deleted.");
  }

  @PreAuthorize("hasAuthority('GROUPLEAD') or hasAuthority('ADMIN') or hasAuthority('MANAGER')")
  @Transactional
  public Userx addMember(Long groupId, String memberId) {
    Groupx group =
        groupRepository
            .findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    Userx member =
        userxRepository
            .findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_MEMBER_ID));
    if (group.getMembers().contains(member)) {
      throw new IllegalArgumentException("Member already exists");
    }
    group.addMember(member);
    groupRepository.save(group);
    auditLogService.logEvent(
        LogEvent.EDIT,
        LogAffectedType.GROUP,
        "Member " + member.getUsername() + " was added to group " + group.getName());
    return member;
  }

  public Groupx getGroup(Long groupId) {
    return groupRepository
        .findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
  }

  public Groupx getGroupDetailedGroupLead(Long groupId) {
    return groupRepository
        .findByIdDetailedGroupLead(groupId)
        .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
  }

  @PreAuthorize("hasAuthority('GROUPLEAD') or hasAuthority('MANAGER')or hasAnyAuthority('ADMIN')")
  public void removeMember(Long groupId, String username) {
    Groupx group =
        groupRepository
            .findByIdDetailedContributors(groupId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    Userx member =
        userxRepository
            .findFirstByUsernameDetailed(username)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    List<GroupxProject> gxps =
        groupxProjectRepository.findAllByGroup_IdAndContributorsContain(groupId, username);
    for (var gxp : gxps) {
      gxp.removeContributor(member);
      projectService.saveGroupxProject(gxp);
    }
    group.removeMember(member);
    groupRepository.save(group);
    auditLogService.logEvent(
        LogEvent.EDIT,
        LogAffectedType.GROUP,
        "Member " + member.getUsername() + " was removed from group " + group.getName());
  }

  @PreAuthorize("hasAuthority('GROUPLEAD') or hasAnyAuthority('ADMIN')")
  public List<Groupx> getGroupsByGroupLead(String userId) {
    Userx groupLead =
        userxRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
    return groupRepository.findByGroupLead(groupLead);
  }

  @PreAuthorize("hasAuthority('MANAGER') or hasAnyAuthority('ADMIN')")
  public List<Groupx> getGroupsByManager(String userId) {
    Userx manager =
        userxRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_MANAGER_ID));
    return groupRepository.findAllByManager(manager);
  }

  public List<Userx> getMembers(Long groupId) {
    Groupx group =
        groupRepository
            .findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    return group.getMembers();
  }

  public void saveGroup(Groupx group) {
    groupRepository.save(group);
  }
}
