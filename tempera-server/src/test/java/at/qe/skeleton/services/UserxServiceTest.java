package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.services.UserxService;
import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Some very basic tests for {@link UserxService}.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
@SpringBootTest
@WebAppConfiguration
public class UserxServiceTest {

    @Autowired
    UserxService userxService;
    @Autowired
    private TemperaStationService temperaStationService;

    @Test
    @DirtiesContext
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDatainitialization() {
        Assertions.assertEquals(12, userxService.getAllUsers().size(), "Insufficient amount of users initialized for test data source");
        for (Userx user : userxService.getAllUsers()) {
            if ("admin".equals(user.getUsername())) {
                assertTrue(user.getRoles().contains(UserxRole.ADMIN), "User \"" + user + "\" does not have role ADMIN");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else if ("user1".equals(user.getUsername())) {
                assertTrue(user.getRoles().contains(UserxRole.MANAGER), "User \"" + user + "\" does not have role MANAGER");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                assertNull(user.getUpdateDate(), "User \"" + user +"\" has a updateDate defined");
            } else if ("user2".equals(user.getUsername())) {
                assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role EMPLOYEE");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else  if ("elvis".equals(user.getUsername())) {
                assertTrue(user.getRoles().contains(UserxRole.ADMIN), "User \"" + user + "\" does not have role ADMIN");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else if (user.getUsername().equals("brucewayne")) {
                assertTrue(user.getRoles().contains(UserxRole.MANAGER), "User \"" + user + "\" does not have role Manager");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertEquals("admin", user.getUpdateUser().getUsername(), "User \"" + user + "\" has no updateUser defined");
            } else if (user.getUsername().equals("clarkkent")) {
                assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role Employee");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNotNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
            } else if (user.getUsername().equals("alicebrown")) {
                assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role Employee");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNotNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
            } else if (user.getUsername().equals("tonystark")) {
                assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role Employee");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNotNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
            } else if (user.getUsername().equals("peterparker")) {
                assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role Employee");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
            } else if (user.getUsername().equals("johndoe")) {
                assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role Employee");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertEquals("admin", user.getUpdateUser().getUsername(), "User \"" + user + "\" has no updateUser defined");
            } else if (user.getUsername().equals("janedoe")) {
                assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role Employee");
                } else if (user.getUsername().equals("chriswilliams")) {
                assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role Employee");
    }
        else if (user.getUsername().equals("bobjones")) {
            assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role Employee");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertEquals("bobjones", user.getCreateUser().getUsername(), "User \"" + user + "\" has a updateUser defined");
            }
        else {
            Assertions.fail("Unexpected user \"" + user + "\" found in test data source");
        }}}

    @DirtiesContext
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUser() throws CouldNotFindEntityException {
        String username = "user1";
        Userx adminUser = userxService.loadUser("admin");
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeDeletedUser = userxService.loadUser(username);
        Assertions.assertNotNull(toBeDeletedUser, "User \"" + username + "\" could not be loaded from test data source");

        userxService.deleteUser(username);

        Assertions.assertEquals(11, userxService.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
        Userx deletedUser = userxService.loadUser(username);
        assertNull(deletedUser, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUser");

        for (Userx remainingUser : userxService.getAllUsers()) {
            Assertions.assertNotEquals(toBeDeletedUser.getUsername(), remainingUser.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
    }

    @DirtiesContext
    @Transactional
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUserExtreme() throws CouldNotFindEntityException {
        String username = "johndoe";
        Userx toBeDeletedUser = userxService.loadUser(username);
        TemperaStation temperaStation = toBeDeletedUser.getTemperaStation();
        assertEquals(toBeDeletedUser,temperaStation.getUser(),"the user is not referenced in his temperastation");
        List<Groupx> usersGroups = toBeDeletedUser.getGroups();
        for (Groupx groupx : usersGroups) {
            assertTrue(groupx.getMembers().contains(toBeDeletedUser), "The user is not reference in his Group %s".formatted(groupx.getName()));
        }
        List<Project> usersProjects = toBeDeletedUser.getProjects();
        for(Project project : usersProjects) {
            assertTrue(project.getContributors().contains(toBeDeletedUser), "The user is not reference in his Project %s".formatted(project.getName()));
        }
        Assertions.assertNotNull(toBeDeletedUser, "User \"" + username + "\" could not be loaded from test data source");

        userxService.deleteUser(username);

        Assertions.assertEquals(11, userxService.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
        Userx deletedUser = userxService.loadUser(username);
        assertNull(deletedUser, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUser");

        for (Groupx group : usersGroups) {
            assertFalse(group.getMembers().contains(toBeDeletedUser), "The user is still referenced in group %s".formatted(group.getName()));
        }
        for (Project project : usersProjects) {
            assertFalse(project.getContributors().contains(toBeDeletedUser), "The user is still refernced in project %s".formatted(project.getName()));
        }
        for (Userx remainingUser : userxService.getAllUsers()) {
            Assertions.assertNotEquals(toBeDeletedUser.getUsername(), remainingUser.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
    }



    @DirtiesContext
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateUser() {
        String username = "user1";
        Userx adminUser = userxService.loadUser("admin");
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeSavedUser = userxService.loadUser(username);
        Assertions.assertNotNull(toBeSavedUser, "User \"" + username + "\" could not be loaded from test data source");

        assertNull(toBeSavedUser.getUpdateUser(), "User \"" + username + "\" has a updateUser defined");
        assertNull(toBeSavedUser.getUpdateDate(), "User \"" + username + "\" has a updateDate defined");

        toBeSavedUser.setEmail("changed-email@whatever.wherever");
        userxService.saveUser(toBeSavedUser);

        Userx freshlyLoadedUser = userxService.loadUser("user1");
        Assertions.assertNotNull(freshlyLoadedUser, "User \"" + username + "\" could not be loaded from test data source after being saved");
        Assertions.assertNotNull(freshlyLoadedUser.getUpdateUser(), "User \"" + username + "\" does not have a updateUser defined after being saved");
        Assertions.assertEquals(adminUser, freshlyLoadedUser.getUpdateUser(), "User \"" + username + "\" has wrong updateUser set");
        Assertions.assertNotNull(freshlyLoadedUser.getUpdateDate(), "User \"" + username + "\" does not have a updateDate defined after being saved");
        Assertions.assertEquals("changed-email@whatever.wherever", freshlyLoadedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
    }

    @DirtiesContext
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateUser() {
        Userx adminUser = userxService.loadUser("admin");
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

        String username = "newuser";
        String password = "passwd";
        String fName = "New";
        String lName = "User";
        String email = "new-email@whatever.wherever";
        String phone = "+12 345 67890";
        Userx toBeCreatedUser = new Userx();
        toBeCreatedUser.setUsername(username);
        toBeCreatedUser.setPassword(password);
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName(fName);
        toBeCreatedUser.setLastName(lName);
        toBeCreatedUser.setEmail(email);
        //toBeCreatedUser.setPhone(phone);
        toBeCreatedUser.setRoles(Sets.newSet(UserxRole.EMPLOYEE, UserxRole.MANAGER));
        userxService.saveUser(toBeCreatedUser);

        Userx freshlyCreatedUser = userxService.loadUser(username);
        Assertions.assertNotNull(freshlyCreatedUser, "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(username, freshlyCreatedUser.getUsername(), "New user could not be loaded from test data source after being saved");
        // compare the saved password (encrypted), not the password field (plain text) with the user
        // service retrieved (also encrypted) password
        Assertions.assertEquals(toBeCreatedUser.getPassword(), freshlyCreatedUser.getPassword(), "User \"" + username + "\" does not have a the correct password attribute stored being saved");
        Assertions.assertEquals(fName, freshlyCreatedUser.getFirstName(), "User \"" + username + "\" does not have a the correct firstName attribute stored being saved");
        Assertions.assertEquals(lName, freshlyCreatedUser.getLastName(), "User \"" + username + "\" does not have a the correct lastName attribute stored being saved");
        Assertions.assertEquals(email, freshlyCreatedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
        //Assertions.assertEquals(phone, freshlyCreatedUser.getPhone(), "User \"" + username + "\" does not have a the correct phone attribute stored being saved");
        assertTrue(freshlyCreatedUser.getRoles().contains(UserxRole.MANAGER), "User \"" + username + "\" does not have role MANAGER");
        assertTrue(freshlyCreatedUser.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + username + "\" does not have role EMPLOYEE");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateUser(), "User \"" + username + "\" does not have a createUser defined after being saved");
        Assertions.assertEquals(adminUser, freshlyCreatedUser.getCreateUser(), "User \"" + username + "\" has wrong createUser set");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateDate(), "User \"" + username + "\" does not have a createDate defined after being saved");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testExceptionForEmptyUsername() {
        Assertions.assertThrows(JpaSystemException.class, () -> {
            Userx adminUser = userxService.loadUser("admin");
            Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

            Userx toBeCreatedUser = new Userx();

            // set roles to avoid another exception being thrown before
            toBeCreatedUser.setRoles(new HashSet<>(Sets.newSet(UserxRole.ADMIN)));
            userxService.saveUser(toBeCreatedUser);
        });
    }

    @Test
    public void testUnauthenticateddLoadUsers() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            for (Userx user : userxService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadUser() {
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            Userx user = userxService.loadUser("admin");
            Assertions.fail("Call to userService.loadUser should not work without proper authorization for other users than the authenticated one");
        });
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testAuthorizedLoadUser() {
        String username = "user1";
        Userx user = userxService.loadUser(username);
        Assertions.assertEquals(username, user.getUsername(), "Call to userService.loadUser returned wrong user");
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveUser() {
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            String username = "user1";
            Userx user = userxService.loadUser(username);
            Assertions.assertEquals(username, user.getUsername(), "Call to userService.loadUser returned wrong user");
            userxService.saveUser(user);
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:userxServiceTest.sql")
    public void testUnauthorizedDeleteUser() {
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            Userx user = userxService.loadUser("user1");
            Assertions.assertEquals("user1", user.getUsername(), "Call to userService.loadUser returned wrong user");
            userxService.deleteUser(user.getUsername());
        });
    }

}