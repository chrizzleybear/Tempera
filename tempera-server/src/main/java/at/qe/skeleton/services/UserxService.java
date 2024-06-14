package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.*;
import at.qe.skeleton.repositories.ExternalRecordRepository;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.rest.frontend.dtos.UserStateDto;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;

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
  @Autowired private AuditLogService auditLogService;

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

  @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
  public Userx loadUserDetailed(String username) {
    return userRepository.findFirstByUsernameDetailed(username).orElseThrow();
  }


  /**
   * Saves the user. This method will also set {@link Userx#createDate} for new entities or {@link
   * Userx#updateDate} for updated entities. The user requesting this operation will also be stored
   * as {@link Userx#createDate} or {@link Userx#updateUser} respectively.
   *
   * @param user the user to save
   * @return the updated user
   */
  @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #user.getUsername()")
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
      auditLogService.logEvent(LogEvent.CREATE, LogAffectedType.USER,
              "New user " + user.getUsername() + ", " + user.getId() + " was saved.");
    } else {
      user.setUpdateDate(LocalDateTime.now());
      user.setUpdateUser(getAuthenticatedUser());
      auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.USER,
              "User " + user.getUsername() + ", " + user.getId() + " was saved.");
    }
    return userRepository.save(user);
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
        auditLogService.logEvent(LogEvent.DELETE, LogAffectedType.USER,
                "Deletion of user " + user.getUsername() + ", " + user.getId() + " failed. User is ADMIN.");
        return;
      }
      if (user.getRoles().contains(UserxRole.MANAGER)
          || user.getRoles().contains(UserxRole.GROUPLEAD)) {
        List<Project> projects = projectService.getProjectsByManager(user.getUsername());
        for (var project : projects) {
          project.setManager(null);
          projectService.saveProject(project);
        }
        List<Groupx> groups = groupRepository.findByGroupLead(user);
        for (var group : groups) {
          group.setGroupLead(null);
          groupRepository.save(group);
        }
      }

      List<GroupxProject> groupxProjects = projectService.findAllGroupxProjectsOfAUser(user);
      for (var groupxProject : groupxProjects) {
        groupxProject.removeContributor(user);
        projectService.saveGroupxProject(groupxProject);
      }
      List <Groupx> groupsAsMember = groupRepository.findAllByMembersContains(user);
      for (var group : groupsAsMember) {
        user.removeGroup(group);
      }

      externalRecordRepository.deleteAllByUser(user);
      auditLogService.logEvent(LogEvent.DELETE, LogAffectedType.TIME_RECORD,
              "External time records of user " + user.getUsername() + ", " + user.getId() + " were deleted.");

      user.removeTemperaStation();
      auditLogService.logEvent(LogEvent.DELETE, LogAffectedType.TEMPERA_STATION,
              "Station of user " + user.getUsername() + ", " + user.getId() + " was deleted.");

      // we are saving the user so that all the other objects, where we set the user reference to null are being
      // saved via cascading
      saveUser(user);

      auditLogService.logEvent(LogEvent.DELETE, LogAffectedType.USER,
              "User " + user.getUsername() + ", " + user.getId() + " was deleted.");
      userRepository.delete(user);
      }
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
    auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.USER,
            "User " + user.getUsername() + ", " + user.getId() + " with roles " + user.getRoles() + " was edited.");
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
      auditLogService.logEvent(LogEvent.LOAD, LogAffectedType.USER,
              "Successfully validated user " + user.getUsername() + "."
      );
      return convertToDTO(user);
    }
    auditLogService.logEvent(LogEvent.WARN, LogAffectedType.USER,
            "Could not validate user with details " + user.getUsername() + ", " + user.getPassword() + " ."
    );
    return null;
  }

  public void enableUser(String username, String password) {
    Userx user = userRepository.findFirstByUsername(username);
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    user.setPassword(passwordEncoder.encode(password));
    user.setEnabled(true);
    auditLogService.logEvent(LogEvent.EDIT, LogAffectedType.USER,
            "User " + user.getUsername() + ", " + user.getId() + " was enabled.");
    userRepository.save(user);
  }

  public Collection<Userx> getManagers() {
    return userRepository.findByRole(UserxRole.MANAGER);
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
