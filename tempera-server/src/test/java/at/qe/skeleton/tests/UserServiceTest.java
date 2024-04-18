package at.qe.skeleton.tests;

import at.qe.skeleton.services.UserxService;
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
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.services.UserxService;

/**
 * Some very basic tests for {@link UserxService}.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
@SpringBootTest
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    UserxService userxService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDatainitialization() {
        Assertions.assertEquals(4, userxService.getAllUsers().size(), "Insufficient amount of users initialized for test data source");
        for (Userx user : userxService.getAllUsers()) {
            if ("admin".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserxRole.ADMIN), "User \"" + user + "\" does not have role ADMIN");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else if ("user1".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserxRole.MANAGER), "User \"" + user + "\" does not have role MANAGER");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                Assertions.assertNull(user.getUpdateDate(), "User \"" + user +"\" has a updateDate defined");
            } else if ("user2".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + user + "\" does not have role EMPLOYEE");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else  if ("elvis".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserxRole.ADMIN), "User \"" + user + "\" does not have role ADMIN");
                Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
                Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
                Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
            } else {
                Assertions.fail("Unknown user \"" + user.getUsername() + "\" loaded from test data source via UserService.getAllUsers");
            }
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUser() {
        String username = "user1";
        Userx adminUser = userxService.loadUser("admin");
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeDeletedUser = userxService.loadUser(username);
        Assertions.assertNotNull(toBeDeletedUser, "User \"" + username + "\" could not be loaded from test data source");

        userxService.deleteUser(toBeDeletedUser);

        Assertions.assertEquals(3, userxService.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
        Userx deletedUser = userxService.loadUser(username);
        Assertions.assertNull(deletedUser, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUser");

        for (Userx remainingUser : userxService.getAllUsers()) {
            Assertions.assertNotEquals(toBeDeletedUser.getUsername(), remainingUser.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateUser() {
        String username = "user1";
        Userx adminUser = userxService.loadUser("admin");
        Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        Userx toBeSavedUser = userxService.loadUser(username);
        Assertions.assertNotNull(toBeSavedUser, "User \"" + username + "\" could not be loaded from test data source");

        Assertions.assertNull(toBeSavedUser.getUpdateUser(), "User \"" + username + "\" has a updateUser defined");
        Assertions.assertNull(toBeSavedUser.getUpdateDate(), "User \"" + username + "\" has a updateDate defined");

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
        Assertions.assertEquals(password, freshlyCreatedUser.getPassword(), "User \"" + username + "\" does not have a the correct password attribute stored being saved");
        Assertions.assertEquals(fName, freshlyCreatedUser.getFirstName(), "User \"" + username + "\" does not have a the correct firstName attribute stored being saved");
        Assertions.assertEquals(lName, freshlyCreatedUser.getLastName(), "User \"" + username + "\" does not have a the correct lastName attribute stored being saved");
        Assertions.assertEquals(email, freshlyCreatedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
        //Assertions.assertEquals(phone, freshlyCreatedUser.getPhone(), "User \"" + username + "\" does not have a the correct phone attribute stored being saved");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserxRole.MANAGER), "User \"" + username + "\" does not have role MANAGER");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserxRole.EMPLOYEE), "User \"" + username + "\" does not have role EMPLOYEE");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateUser(), "User \"" + username + "\" does not have a createUser defined after being saved");
        Assertions.assertEquals(adminUser, freshlyCreatedUser.getCreateUser(), "User \"" + username + "\" has wrong createUser set");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateDate(), "User \"" + username + "\" does not have a createDate defined after being saved");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testExceptionForEmptyUsername() {
        Assertions.assertThrows(JpaSystemException.class, () -> {
            Userx adminUser = userxService.loadUser("admin");
            Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

            Userx toBeCreatedUser = new Userx();
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
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadUsers() {
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            for (Userx user : userxService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadUser() {
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            Userx user = userxService.loadUser("admin");
            Assertions.fail("Call to userService.loadUser should not work without proper authorization for other users than the authenticated one");
        });
    }

    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testAuthorizedLoadUser() {
        String username = "user1";
        Userx user = userxService.loadUser(username);
        Assertions.assertEquals(username, user.getUsername(), "Call to userService.loadUser returned wrong user");
    }

    @Test
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
    public void testUnauthorizedDeleteUser() {
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            Userx user = userxService.loadUser("user1");
            Assertions.assertEquals("user1", user.getUsername(), "Call to userService.loadUser returned wrong user");
            userxService.deleteUser(user);
        });
    }

}