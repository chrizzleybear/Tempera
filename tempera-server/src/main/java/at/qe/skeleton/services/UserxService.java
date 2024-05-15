package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.repositories.ExternalRecordRepository;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.rest.frontend.dtos.UserStateDto;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.repositories.UserxRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
  @Autowired private ExternalRecordRepository externalRecordRepository;
  @Autowired private GroupRepository groupRepository;
  @Autowired private ProjectService projectService;

  /**
   * Returns a collection of all users.
   *
   * @return
   */
  // todo: we cant preAuthorize this method because we need to get all users to display them in the
  // dashboard... or is there another way?
  @PreAuthorize(
      "hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('GROUPLEAD')")
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

  /**
   * Warning: This will delete all external and Internal Records of that user. Also you cant delete
   * an admin.
   *
   * @param id
   */
  @Transactional
  @PreAuthorize("hasAuthority('ADMIN')")
  public void deleteUser(String id) {

    Optional<Userx> userx = userRepository.findById(id);
    if (userx.isPresent()) {
      Userx user = userx.get();
      if (user.getRoles().contains(UserxRole.ADMIN)) {
        return;
      }
      if (user.getRoles().contains(UserxRole.MANAGER)
          || user.getRoles().contains(UserxRole.GROUPLEAD)) {
        List<Project> projects = projectService.getProjectsByManager(user.getUsername());
        for (var project : projects) {
          project.setManager(null);
          projectService.saveProject(project);
        }
        List<Group> groups = groupRepository.findAllByGroupLead(user);
        for (var group : groups) {
          group.setGroupLead(null);
          groupRepository.save(group);
        }
      }

      List<Project> projects = projectService.getProjectsByContributor(user.getUsername());
      for (var project : projects) {
        project.getContributors().remove(user);
        projectService.saveProject(project);
      }
      List <Group> groupsAsMember = groupRepository.findAllByMembersContains(user);
      for (var group : groupsAsMember) {
        group.getMembers().remove(user);
        groupRepository.save(group);
      }
      externalRecordRepository.deleteAllByUser(user);
      user.removeTemperaStation();
      userx.ifPresent(value -> userRepository.delete(value));
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  public Userx updateUser(UserxDto userxDTO) {
    Userx user = userRepository.findFirstByUsername(userxDTO.getUsername());
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    user.setFirstName(userxDTO.getFirstName());
    user.setLastName(userxDTO.getLastName());
    user.setPassword(passwordEncoder.encode(userxDTO.getPassword()));
    user.setEmail(userxDTO.getEmail());
    user.setRoles(userxDTO.getRoles());
    user.setEnabled(userxDTO.isEnabled());
    user.setUpdateDate(LocalDateTime.now());
    user.setUpdateUser(getAuthenticatedUser());
    return userRepository.save(user);
  }

  public UserxDto convertToDTO(Userx user) {
    UserxDto userxDTO = new UserxDto();
    userxDTO.setUsername(user.getUsername());
    userxDTO.setFirstName(user.getFirstName());
    userxDTO.setLastName(user.getLastName());
    userxDTO.setPassword(user.getPassword());
    userxDTO.setEmail(user.getEmail());
    userxDTO.setEnabled(user.isEnabled());
    userxDTO.setRoles(user.getRoles());
    return userxDTO;
  }

  public Userx convertToEntity(UserxDto userxDTO) {
    Userx user = new Userx();
    user.setUsername(userxDTO.getUsername());
    user.setFirstName(userxDTO.getFirstName());
    user.setLastName(userxDTO.getLastName());
    user.setPassword(userxDTO.getPassword());
    user.setEmail(userxDTO.getEmail());
    user.setEnabled(userxDTO.isEnabled());
    user.setRoles(userxDTO.getRoles());
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

  public List<UserStateDto> getUserWithStates(List<Userx> users) {
    return externalRecordRepository.findUserStatesByUserList(users);
  }

  public State switchState(ExternalRecord record) {
    Userx user = record.getUser();
    State state = user.getState();
    user.setState(state);
    return state;
  }
}
