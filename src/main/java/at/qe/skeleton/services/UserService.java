package at.qe.skeleton.services;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for accessing and manipulating user data.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
@Component
@Scope("application")
public class UserService implements UserDetailsService {

    @Autowired
    private UserxRepository userRepository;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Userx loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link Userx#createDate} for new
     * entities or {@link Userx#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Userx#createDate}
     * or {@link Userx#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx saveUser(Userx user) {
        if (user.isNew()) {
            user.setCreateDate(LocalDateTime.now());
            user.setCreateUser(getAuthenticatedUser());
        } else {
            user.setUpdateDate(LocalDateTime.now());
            user.setUpdateUser(getAuthenticatedUser());
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        userRepository.delete(user);
        // :TODO: write some audit log stating who and when this user was permanently deleated.
    }

    private Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * For interface which is needed for JWT
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Userx user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public void deleteUser(String id) {
       Optional<Userx> userx = userRepository.findById(id);
         if(userx.isPresent()){
              userRepository.delete(userx.get());
             System.out.println("User with id: " + id + " deleted");
         }

    }


    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx updateUser(Userx v2_user) {
            Userx v1_user = userRepository.findById(v2_user.getId()).orElse(v2_user);
            v1_user.setId(v2_user.getId());
            v1_user.setFirstName(v2_user.getFirstName());
            v1_user.setLastName(v2_user.getLastName());
            v1_user.setUsername(v2_user.getUsername());
            v1_user.setEmail(v2_user.getEmail());
            v1_user.setRoles(v2_user.getRoles());
            v1_user.setEnabled(v2_user.isEnabled());
            v1_user.setUpdateDate(LocalDateTime.now());
            v1_user.setUpdateUser(getAuthenticatedUser());
        return userRepository.save(v1_user);
    }
}
