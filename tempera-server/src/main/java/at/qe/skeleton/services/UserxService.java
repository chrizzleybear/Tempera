package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.repositories.UserxRepository;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for accessing and manipulating user data.
 *
 * <p>This class is part of the skeleton project provided for students of the course "Software
 * Engineering" offered by the University of Innsbruck.
 */
@Component
@Scope("application")
public class UserxService implements UserDetailsService {

  @Autowired private UserxRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private TemperaStationService temperaStationService;

  /**
   * Returns a collection of all users.
   *
   * @return
   */
  @PreAuthorize("hasAuthority('ADMIN')")
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
   * Saves the user. This method will also set {@link Userx#createDate} for new entities or {@link
   * Userx#updateDate} for updated entities. The user requesting this operation will also be stored
   * as {@link Userx#createDate} or {@link Userx#updateUser} respectively.
   *
   * @param user the user to save
   * @return the updated user
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  public Userx saveUser(Userx user) {
    if (user.isNew()) {
      user.setCreateDate(LocalDateTime.now());
      user.setCreateUser(getAuthenticatedUser());
      user.setStateVisibility(Visibility.PUBLIC);
      String password = user.getPassword();
      // Passing null as an argument to the encoder throws IllegalArgumentException,
      // but we want JpaSystemException
      if (password == null) {
        throw new JpaSystemException(new RuntimeException("Password can't be empty"));
      }
      user.setPassword(passwordEncoder.encode(password));

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
  public void deleteUser(Userx user) throws CouldNotFindEntityException {
    TemperaStation temperaStation =
        temperaStationService
            .findByUser(user)
            .orElseThrow(
                () ->
                    new CouldNotFindEntityException(
                        "Could not find Temperastation assigned to User %s".formatted(user)));
    temperaStation.setUser(null);
    temperaStationService.save(temperaStation);
    userRepository.delete(user);
    // :TODO: write some audit log stating who and when this user was permanently deleated.
  }

  private Userx getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userRepository.findFirstByUsername(auth.getName());
  }

  /** For interface which is needed for JWT */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Userx user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

  @Transactional
  public UserxDto loadUserDTOById(String username) {
    Userx user =
        userRepository
            .findById(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with username: " + username));

    return convertToDTO(user);
  }

  public void deleteUser(String id) {
    Optional<Userx> userx = userRepository.findById(id);
    userx.ifPresent(value -> userRepository.delete(value));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  public Userx updateUser(UserxDto userxDTO) {
    Userx user = userRepository.findFirstByUsername(userxDTO.username());
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    user.setFirstName(userxDTO.firstName());
    user.setLastName(userxDTO.lastName());
    if(!userxDTO.password().equals(user.getPassword())){
    user.setPassword(passwordEncoder.encode(userxDTO.password()));}
    user.setEmail(userxDTO.email());
    user.setRoles(userxDTO.roles());
    user.setEnabled(userxDTO.enabled());
    user.setUpdateDate(LocalDateTime.now());
    user.setUpdateUser(getAuthenticatedUser());
    return userRepository.save(user);
  }

  public UserxDto convertToDTO(Userx user) {
    return new UserxDto(
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.isEnabled(),
            user.getRoles());
  }

  public Userx convertToEntity(UserxDto userxDTO) {
    Userx user = new Userx();
    user.setUsername(userxDTO.username());
    user.setFirstName(userxDTO.firstName());
    user.setLastName(userxDTO.lastName());
    user.setEmail(userxDTO.email());
    user.setPassword(passwordEncoder.encode(userxDTO.password()));
    user.setEnabled(userxDTO.enabled());
    user.setRoles(userxDTO.roles());
    return user;
  }

  public UserxDto validateUser(String username, String password) {
    Userx user = userRepository.findFirstByUsername(username);
    if (passwordEncoder.matches(password, user.getPassword())) {
      return convertToDTO(user);
    }
    return null;
  }

  public void enableUser(String username, String password) {
    Userx user = userRepository.findFirstByUsername(username);
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    user.setPassword(passwordEncoder.encode(password));
    user.setEnabled(true);
    // log "Enable user with username: " + username
    userRepository.save(user);
  }

  public Collection<Userx> getManagers() {
    return userRepository.findByRole(UserxRole.MANAGER);
  }
}
