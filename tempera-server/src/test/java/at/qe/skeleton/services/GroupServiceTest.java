package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class GroupServiceTest {

    @Mock private UserxRepository userxRepository;
    @Mock private ProjectService projectService;
    @Mock private GroupRepository groupRepository;
    @Mock private GroupxProjectRepository groupxProjectRepository;
    @Mock private AuditLogService auditLogService;

    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);
        this.groupService =
                spy(new GroupService(
                        projectService,
                        userxRepository,
                        groupRepository,
                        auditLogService,
                        groupxProjectRepository
                ));
    }

    @Test
    void testCreateGroup() {

        String username = "groupLead";
        Userx groupLead = new Userx();
        groupLead.setUsername(username);
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));

        String name = "Test Group";
        String description = "Test Group Description";
        String groupLeadId = "groupLead123";
        when(userxRepository.findById(groupLeadId)).thenReturn(Optional.of(groupLead));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Groupx createdGroup = groupService.createGroup(name, description, groupLeadId);

        assertNotNull(createdGroup);
        assertEquals(name, createdGroup.getName());
        assertEquals(description, createdGroup.getDescription());
        assertEquals(groupLead, createdGroup.getGroupLead());
        verify(auditLogService, atLeastOnce()).logEvent(eq(LogEvent.CREATE), eq(LogAffectedType.GROUP), anyString());
        verify(groupRepository).save(any());
    }

    @Test
    void testUpdateGroup() {
        String name = "Updated Group";
        String description = "Updated Group Des";

        String oldGroupLeadName = "oldGroupLead";
        Userx oldGroupLead = new Userx();
        oldGroupLead.setUsername(oldGroupLeadName);
        oldGroupLead.setRoles(Set.of(UserxRole.GROUPLEAD));

        String newGroupLeadName = "newGroupLead";
        Userx newGroupLead = new Userx();
        newGroupLead.setUsername(newGroupLeadName);
        newGroupLead.setRoles(Set.of(UserxRole.GROUPLEAD));

        Groupx group = new Groupx(
                "Original Group",
                "Original Des",
                oldGroupLead);

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userxRepository.findById(newGroupLeadName)).thenReturn(Optional.of(newGroupLead));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Groupx updatedGroup = groupService.updateGroup(group.getId(), name, description, newGroupLeadName);

        assertNotNull(updatedGroup);
        assertEquals(name, updatedGroup.getName());
        assertEquals(description, updatedGroup.getDescription());
        assertEquals(newGroupLead, updatedGroup.getGroupLead());
        verify(auditLogService, atLeastOnce()).logEvent(eq(LogEvent.EDIT), eq(LogAffectedType.GROUP), anyString());
        verify(groupRepository).save(any());
    }

    @Test
    void testDeleteGroup() {

        String username = "groupLead";
        Userx groupLead = new Userx();
        groupLead.setUsername(username);
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));

        Groupx group = new Groupx("Delete", ".", groupLead);
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupxProjectRepository.findAllByGroup_Id(group.getId())).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> groupService.deleteGroup(group.getId()));
        assertFalse(group.isActive());
        assertTrue(group.getMembers().isEmpty());
        verify(auditLogService, atLeastOnce()).logEvent(eq(LogEvent.DELETE), eq(LogAffectedType.GROUP), anyString());
        verify(groupRepository).save(group);
    }

    @Test
    void testAddMember() {
        String username = "groupLead";
        Userx groupLead = new Userx();
        groupLead.setUsername(username);
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));

        Groupx group = new Groupx("Test Group", "Description", groupLead);

        String memberName = "member";
        Userx member = new Userx();
        groupLead.setUsername(memberName);

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userxRepository.findById(memberName)).thenReturn(Optional.of(member));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        List<Groupx> groupList = new ArrayList<Groupx>();
        groupList.add(group);
        member.setGroups(groupList);
        Userx addedMember = groupService.addMember(group.getId(), memberName);

        assertNotNull(addedMember);
        assertTrue(group.getMembers().contains(addedMember));
        verify(auditLogService, atLeastOnce()).logEvent(eq(LogEvent.EDIT), eq(LogAffectedType.GROUP), anyString());
        verify(groupRepository).save(group);
    }

    @Test
    void testRemoveMember() throws CouldNotFindEntityException {

        String username = "groupLead";
        Userx groupLead = new Userx();
        groupLead.setUsername(username);
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD, UserxRole.MANAGER));

        String memberName = "memberToRemove";
        Userx member = new Userx();
        member.setUsername(memberName);

        Groupx group = new Groupx("Test Group", "Description", groupLead);
        List<Groupx> groupList = new ArrayList<Groupx>();
        groupList.add(group);
        member.setGroups(groupList);
        groupService.addMember(group.getId(), memberName);

        GroupxProject gxp = new GroupxProject();
        member.setGroupxProjects(Set.of(gxp));
        Project project = new Project("0", "1", groupLead);
        projectService.addContributor(group.getId(), project.getId(), memberName);

        List<GroupxProject> gxps = new ArrayList<>();
        gxps.add(gxp);

        when(groupRepository.findByIdDetailedContributors(group.getId()))
                .thenReturn(Optional.of(group));
        when(userxRepository.findFirstByUsernameDetailed(username))
                .thenReturn(Optional.of(member));
        when(groupxProjectRepository.findAllByGroup_IdAndContributorsContain(group.getId(), username))
                .thenReturn(gxps);
        doNothing().when(projectService).saveGroupxProject(any());

        assertDoesNotThrow(() -> groupService.removeMember(group.getId(), username));
        assertFalse(group.getMembers().contains(member));
        assertTrue(gxp.getContributors().isEmpty());
        verify(auditLogService, atLeastOnce()).logEvent(eq(LogEvent.EDIT), eq(LogAffectedType.GROUP), anyString());
        verify(projectService).saveGroupxProject(gxp);
        verify(groupRepository).save(group);
    }

    @Test
    void testGetGroupsByGroupLead() {
        String username = "groupLead";
        Userx groupLead = new Userx();
        groupLead.setUsername(username);

        List<Groupx> groups = new ArrayList<>();
        groups.add(new Groupx("Group 1", "Description 1", groupLead));
        groups.add(new Groupx("Group 2", "Description 2", groupLead));
        when(userxRepository.findById(username)).thenReturn(Optional.of(groupLead));
        when(groupRepository.findByGroupLead(groupLead)).thenReturn(groups);
        List<Groupx> groupsByGroupLead = groupService.getGroupsByGroupLead(username);

        assertEquals(groups.size(), groupsByGroupLead.size());
        assertEquals(groups, groupsByGroupLead);
        verify(groupRepository).findByGroupLead(groupLead);
    }



}
