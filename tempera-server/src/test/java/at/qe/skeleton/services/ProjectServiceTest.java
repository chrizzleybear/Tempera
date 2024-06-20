package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.GroupxProjectRepository;
import at.qe.skeleton.repositories.ProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProjectServiceTest {

    // ############################################################
    //          ONLY THE CORE FUNCTIONALITIES ARE TESTED
    //          TO-DO: refine tests, add some if necessary
    // ############################################################

    @Autowired private UserxService userxService;
    @Autowired private GroupService groupService;
    @Mock private ProjectRepository projectRepository;
    @Mock private UserxRepository userxRepository;
    @Mock private GroupRepository groupRepository;
    @Mock private GroupxProjectRepository groupxProjectRepository;
    @Mock private AuditLogService auditLogService;;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);
        this.projectService =
                new ProjectService(
                        projectRepository,
                        auditLogService,
                        userxRepository,
                        groupRepository,
                        groupxProjectRepository
                );
    }

    @Test
    void testCreateProject() {
        Userx managerUser = new Userx();
        managerUser.setId("manager");
        managerUser.setRoles(Set.of(UserxRole.MANAGER));
        when(userxRepository.findById("manager")).thenReturn(Optional.of(managerUser));

        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setManager(managerUser);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project createdProject = projectService.createProject("Test Project", "Test Description", "manager");

        assertNotNull(createdProject);
        assertEquals("Test Project", createdProject.getName());
        assertEquals("Test Description", createdProject.getDescription());
        assertEquals("manager", createdProject.getManager().getId());
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
    }

    @Test
    void testUpdateProject() {
        Project project = new Project();
        project.setName("old name");
        project.setDescription("old des");
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Userx managerUser = new Userx();
        managerUser.setId("manager");
        managerUser.setRoles(Set.of(UserxRole.MANAGER));
        when(userxRepository.findById("manager")).thenReturn(Optional.of(managerUser));

        // to-do: check first if update project throws correct exception when user cant be found
        //assertThrows(IllegalArgumentException.class,
        //        projectService.updateProject(project.getId(), "New Name", "New Description", "manager"));

        Project updatedProject = projectService.updateProject(project.getId(), "New Name", "New Description", "manager");

        assertNotNull(updatedProject);
        assertEquals("New Name", updatedProject.getName());
        assertEquals("New Description", updatedProject.getDescription());
        assertEquals("manager", updatedProject.getManager().getUsername());
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
    }

    @Test
    void testCreateGroupxProject() throws IOException {
        Userx groupLead = new Userx();
        groupLead.setId("groupLead");
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));
        groupLead.setGroupxProjects(new HashSet<>());
        groupLead.setGroups(new ArrayList<>());
        Project project = new Project();
        project.setName("Test Project");
        project.setGroupxProjects(new HashSet<>());
        Groupx group = new Groupx();
        group.setName("Test Group");
        group.addMember(groupLead);
        group.setGroupLead(groupLead);
        group.setGroupxProjects(new HashSet<>());
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(groupxProjectRepository.findByGroup_IdAndProject_IdFetchGroupAndProjectEagerly(group.getId(), project.getId()))
                .thenReturn(Optional.empty());
        when(groupxProjectRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GroupxProject createdGroupxProject = projectService.createGroupxProject(project.getId(), group.getId());

        assertNotNull(createdGroupxProject);
        assertEquals(group.getName(), createdGroupxProject.getGroup().getName());
        assertEquals(project.getName(), createdGroupxProject.getProject().getName());
        assertTrue(createdGroupxProject.isActive());
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
    }

    @Test
    public void testLoadProject() {
        Userx managerUser = new Userx();
        managerUser.setId("manager");
        managerUser.setRoles(Set.of(UserxRole.MANAGER));
        when(userxRepository.findById("manager")).thenReturn(Optional.of(managerUser));

        Project project = new Project();
        project.setName("A");
        project.setDescription("B");
        project.setManager(managerUser);
        when(projectRepository.findFirstById(project.getId())).thenReturn(project);

        Project loadedProject = projectService.loadProject(project.getId());

        assertNotNull(loadedProject);
        assertEquals("A", loadedProject.getName());
        assertEquals("B", loadedProject.getDescription());
        assertEquals("manager", loadedProject.getManager().getId());
        verify(projectRepository, times(1)).findFirstById(project.getId());
    }

    @Test
    public void testDeleteProject() {

        Project project = new Project();
        project.setName("Test Project");

        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addProject(project);
        GroupxProjectId id = groupxProject.getId();

        when(groupxProjectRepository.findAllByProjectId(1L)).thenReturn(List.of(groupxProject));
        when(projectRepository.findFirstById(1L)).thenReturn(project);

        projectService.deleteProject(1L);

        verify(groupxProjectRepository, times(1)).findAllByProjectId(1L);
        verify(groupxProjectRepository, times(1)).save(groupxProject);
        verify(projectRepository, times(1)).findFirstById(1L);
        verify(projectRepository, times(1)).save(project);
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
        assertFalse(groupxProject.isActive());
        assertFalse(project.isActive());
    }

    @Test
    public void testReactivateProject() {
        Project project = new Project();
        project.setName("A");
        project.deactivate();
        when(projectRepository.findFirstById(project.getId())).thenReturn(project);

        Project reactivatedProject = projectService.reactivateProject(project.getId());

        assertNotNull(reactivatedProject);
        assertTrue(reactivatedProject.isActive());
        verify(projectRepository, times(1)).findFirstById(project.getId());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void testRemoveGroupFromProject() throws CouldNotFindEntityException {
        Project project = new Project();
        project.setName("Test Project");
        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addProject(project);

        when(groupxProjectRepository.findByGroup_IdAndProject_IdFetchContributorsEagerly(1L, 1L)).thenReturn(Optional.of(groupxProject));

        projectService.removeGroupFromProject(1L, 1L);

        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_IdFetchContributorsEagerly(1L, 1L);
        verify(groupxProjectRepository, times(1)).save(groupxProject);
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
        assertFalse(groupxProject.isActive());
    }

    @Test
    public void testRemoveGroupFromProjectNotFound() {
        when(groupxProjectRepository.findByGroup_IdAndProject_IdFetchContributorsEagerly(1L, 1L)).thenReturn(Optional.empty());

        CouldNotFindEntityException exception = assertThrows(CouldNotFindEntityException.class, () -> {
            projectService.removeGroupFromProject(1L, 1L);
        });

        assertEquals("GroupxProject with groupId 1 and projectId 1 are not present", exception.getMessage());
        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_IdFetchContributorsEagerly(1L, 1L);
        verify(auditLogService, never()).logEvent(any(), any(), any());
    }

    @Test
    public void testDeactivateGroupxProject() throws CouldNotFindEntityException {

        Project project = new Project();
        project.setName("Test Project");
        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addProject(project);

        when(groupxProjectRepository.findByGroup_IdAndProject_IdFetchContributorsEagerly(1L, 1L)).thenReturn(Optional.of(groupxProject));

        projectService.deactivateGroupxProject(groupxProject);

        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_IdFetchContributorsEagerly(1L, 1L);
        verify(groupxProjectRepository, times(1)).save(groupxProject);
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
        assertFalse(groupxProject.isActive());
    }

    @Test
    public void testDeactivateGroupxProjectNotFound() {
        when(groupxProjectRepository.findByGroup_IdAndProject_IdFetchContributorsEagerly(1L, 1L)).thenReturn(Optional.empty());

        CouldNotFindEntityException exception = assertThrows(CouldNotFindEntityException.class, () -> {
            projectService.deactivateGroupxProject(new GroupxProject());
        });

        assertEquals("GroupxProject with groupId 1 and projectId 1 are not present", exception.getMessage());
        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_IdFetchContributorsEagerly(1L, 1L);
        verify(auditLogService, never()).logEvent(any(), any(), any());
    }

    @Test
    public void testAddContributor() throws CouldNotFindEntityException {
        Userx groupLead = new Userx();
        groupLead.setId("testUSer");
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));
        groupLead.setGroupxProjects(new HashSet<>());
        groupLead.setGroups(new ArrayList<>());

        Project project = new Project();
        project.setName("Test Project");
        project.setGroupxProjects(new HashSet<>());

        Groupx group = new Groupx();
        group.setName("Test Group");
        group.addMember(groupLead);
        group.setGroupLead(groupLead);
        group.setGroupxProjects(new HashSet<>());
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addGroup(group);
        groupxProject.addProject(project);
        when(userxRepository.findByUsername(groupLead.getId())).thenReturn(Optional.of(groupLead));
        when(groupxProjectRepository.findByGroup_IdAndProject_Id(group.getId(), project.getId())).thenReturn(Optional.of(groupxProject));

        projectService.addContributor(group.getId(), project.getId(), groupLead.getId());

        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_Id(group.getId(), project.getId());
        verify(userxRepository, times(1)).findByUsername(groupLead.getId());
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
        assertTrue(groupxProject.getContributors().contains(groupLead));
    }

    @Test
    public void testAddContributorGroupxProjectNotFound() {
        Userx groupLead = new Userx();
        groupLead.setId("groupLead");
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));
        groupLead.setGroupxProjects(new HashSet<>());

        Project project = new Project();
        project.setName("Test Project");
        project.setGroupxProjects(new HashSet<>());

        Groupx group = new Groupx();
        group.setName("Test Group");
        group.setGroupLead(groupLead);
        group.setGroupxProjects(new HashSet<>());
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addGroup(group);
        groupxProject.addProject(project);
        when(userxRepository.findByUsername("groupLead")).thenReturn(Optional.of(groupLead));

        when(groupxProjectRepository.findByGroup_IdAndProject_Id(group.getId(), project.getId())).thenReturn(Optional.empty());

        CouldNotFindEntityException exception = assertThrows(CouldNotFindEntityException.class, () -> {
            projectService.addContributor(group.getId(), project.getId(), "groupLead");
        });

        assertEquals("Could not find GroupxProject with GroupId %d and ProjectID %d"
                .formatted(group.getId(), project.getId()), exception.getMessage());
        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_Id(group.getId(), project.getId());
        verify(userxRepository, never()).findById(anyString());
        verify(groupxProjectRepository, never()).save(any(GroupxProject.class));
        verify(auditLogService, never()).logEvent(any(), any(), any());
    }

    @Test
    public void testAddContributorUserNotFound() {
        Userx groupLead = new Userx();
        groupLead.setId("groupLead");
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));
        groupLead.setGroupxProjects(new HashSet<>());
        when(userxRepository.findById("groupLead")).thenReturn(Optional.of(groupLead));

        Project project = new Project();
        project.setName("Test Project");
        project.setGroupxProjects(new HashSet<>());

        Groupx group = new Groupx();
        group.setName("Test Group");
        group.setGroupLead(groupLead);
        group.setGroupxProjects(new HashSet<>());
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addGroup(group);
        groupxProject.addProject(project);
        when(groupxProjectRepository.findByGroup_IdAndProject_Id(group.getId(), project.getId())).thenReturn(Optional.of(groupxProject));
        when(userxRepository.findByUsername("groupLead")).thenReturn(Optional.of(groupLead));
        when(userxRepository.findByUsername("userName")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.addContributor(group.getId(), project.getId(), "userName");
        });

        assertEquals("User not found", exception.getMessage());
        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_Id(group.getId(), project.getId());
        verify(userxRepository, times(1)).findByUsername(anyString());
        verify(groupxProjectRepository, never()).save(any(GroupxProject.class));
        verify(auditLogService, never()).logEvent(any(), any(), any());
    }

    @Test
    public void testRemoveContributor() throws CouldNotFindEntityException {
        Userx groupLead = new Userx();
        groupLead.setId("groupLead");
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));
        groupLead.setGroupxProjects(new HashSet<>());
        when(userxRepository.findByUsername("groupLead")).thenReturn(Optional.of(groupLead));

        Project project = new Project();
        project.setName("Test Project");
        project.setGroupxProjects(new HashSet<>());

        Groupx group = new Groupx();
        group.setName("Test Group");
        group.setGroupLead(groupLead);
        group.setGroupxProjects(new HashSet<>());
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addGroup(group);
        groupxProject.addProject(project);
        when(groupxProjectRepository.findByGroup_IdAndProject_Id(group.getId(), project.getId())).thenReturn(Optional.of(groupxProject));

        groupxProject.addContributor(groupLead);

        projectService.removeContributor(group.getId(), project.getId(), groupLead.getUsername());

        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_Id(group.getId(), project.getId());
        verify(userxRepository, times(1)).findByUsername(anyString());
        verify(groupxProjectRepository, times(1)).save(groupxProject);
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
        assertFalse(groupxProject.getContributors().contains(groupLead));
    }

    @Test
    public void testRemoveContributorGroupxProjectNotFound() {
        Userx groupLead = new Userx();
        groupLead.setId("groupLead");
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));
        groupLead.setGroupxProjects(new HashSet<>());

        Project project = new Project();
        project.setName("Test Project");
        project.setGroupxProjects(new HashSet<>());

        Groupx group = new Groupx();
        group.setName("Test Group");
        group.setGroupLead(groupLead);
        group.setGroupxProjects(new HashSet<>());
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addGroup(group);
        groupxProject.addProject(project);
        when(userxRepository.findByUsername("groupLead")).thenReturn(Optional.of(groupLead));

        when(groupxProjectRepository.findByGroup_IdAndProject_Id(group.getId(), project.getId())).thenReturn(Optional.empty());

        CouldNotFindEntityException exception = assertThrows(CouldNotFindEntityException.class, () -> {
            projectService.removeContributor(group.getId(), project.getId(), "groupLead");
        });

        assertEquals("Could not find GroupxProject with GroupId %d and ProjectID %d"
                .formatted(group.getId(), project.getId()), exception.getMessage());
        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_Id(group.getId(), project.getId());
        verify(userxRepository, never()).findById(anyString());
        verify(groupxProjectRepository, never()).save(any(GroupxProject.class));
        verify(auditLogService, never()).logEvent(any(), any(), any());
    }

    @Test
    public void testRemoveContributorUserNotFound() {
        Userx groupLead = new Userx();
        groupLead.setId("groupLead");
        groupLead.setRoles(Set.of(UserxRole.GROUPLEAD));
        when(userxRepository.findById("groupLead")).thenReturn(Optional.of(groupLead));

        Project project = new Project();
        project.setName("Test Project");
        project.setGroupxProjects(new HashSet<>());

        Groupx group = new Groupx();
        group.setName("Test Group");
        group.setGroupLead(groupLead);
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GroupxProject groupxProject = new GroupxProject();
        groupxProject.addProject(project);

        when(groupxProjectRepository.findByGroup_IdAndProject_Id(group.getId(), project.getId())).thenReturn(Optional.of(groupxProject));
        when(userxRepository.findById("groupLead")).thenReturn(Optional.of(groupLead));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.removeContributor(group.getId(), project.getId(), "groupLead");
        });

        assertEquals("User not found", exception.getMessage());
        verify(groupxProjectRepository, times(1)).findByGroup_IdAndProject_Id(group.getId(), project.getId());
        verify(userxRepository, times(1)).findByUsername(groupLead.getId());
        verify(groupxProjectRepository, never()).save(any(GroupxProject.class));
    }



}
