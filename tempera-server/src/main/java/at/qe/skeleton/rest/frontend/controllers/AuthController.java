package at.qe.skeleton.rest.frontend.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import at.qe.skeleton.jwt.JwtUtils;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.rest.frontend.payload.request.LoginRequest;
import at.qe.skeleton.rest.frontend.payload.request.SignupRequest;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.rest.frontend.payload.response.UserInfoResponse;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.UserDetailsImpl;
import at.qe.skeleton.services.UserxService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Parts of this code were taken from: https://www.bezkoder.com/angular-17-spring-boot-jwt-auth/

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/auth", produces = "application/json")
public class AuthController {
  @Autowired AuthenticationManager authenticationManager;

  @Autowired UserxRepository userRepository;

  @Autowired PasswordEncoder encoder;

  @Autowired JwtUtils jwtUtils;

  @Autowired UserxService userxService;

  @Autowired AuditLogService auditLogService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
    auditLogService.logEvent(LogEvent.LOGIN, LogAffectedType.USER,
            "User " + userDetails.getId() + " logged in."
    );
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(
            new UserInfoResponse(
                userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    Userx user =
        new Userx(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()),
            LocalDateTime.now());

    // Todo: handle role management and enabling of user (and createUser?)

    user.setRoles(Set.of(UserxRole.EMPLOYEE));
    user.setCreateUser(user);
    user.setEnabled(true);
    userRepository.save(user);
    auditLogService.logEvent(LogEvent.CREATE, LogAffectedType.USER,
            "Successfully created user for " + user.getFirstName() + " " + user.getLastName() + " with username " + user.getId() + " and roles " + user.getRoles() + "."
    );
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    auditLogService.logEvent(LogEvent.LOGOUT, LogAffectedType.USER,
            "Current user with details " + cookie.toString() + " logged out."
    );
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }

  @PostMapping("/validate")
  public ResponseEntity<UserxDto> validateUser(@RequestBody Map<String, String> credentials) {
    String username = credentials.get("username");
    String password = credentials.get("password");
    UserxDto isValidUser = userxService.validateUser(username, password);
    auditLogService.logEvent(LogEvent.LOGIN, LogAffectedType.USER,
            ((isValidUser == null ? "Failed to validate " : "Validated ")) + "user with credentials " + username + ", " + password + " ."
    );
    return ResponseEntity.ok(isValidUser);
  }
}
