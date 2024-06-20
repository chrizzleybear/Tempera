package at.qe.skeleton.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;

@SpringBootTest
class UserDetailsImplTest {

    private Userx user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        user = new Userx();
        user.setId("1");
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setEnabled(true);
        user.setRoles(Set.of(UserxRole.EMPLOYEE));

        userDetails = UserDetailsImpl.build(user);
    }

    @Test
    void testBuild() {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(user.isEnabled(), userDetails.isEnabled());
        assertEquals(authorities, userDetails.getAuthorities());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(UserxRole.EMPLOYEE.toString())));
    }

    @Test
    void testEquals() {
        UserDetailsImpl anotherUserDetails = UserDetailsImpl.build(user);
        assertEquals(userDetails, anotherUserDetails);

        Userx differentUser = new Userx();
        differentUser.setId("2");
        differentUser.setUsername("df");
        differentUser.setEmail("df@e.com");
        differentUser.setPassword("dp");
        differentUser.setEnabled(false);
        differentUser.setRoles(Set.of(UserxRole.ADMIN));

        UserDetailsImpl differentUserDetails = UserDetailsImpl.build(differentUser);
        assertNotEquals(userDetails, differentUserDetails);
    }
}
