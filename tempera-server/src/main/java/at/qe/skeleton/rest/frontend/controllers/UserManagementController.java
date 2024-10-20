package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.DeletionResponseDto;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.rest.frontend.payload.request.EnableUserRequest;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.AuthenticationService;
import at.qe.skeleton.services.UserxService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/users", produces = "application/json")
public class UserManagementController {

  private final UserxService userxService;
  private final AuthenticationService authenticationService;

  public UserManagementController(
      UserxService userxService, AuthenticationService authenticationService) {
    this.userxService = userxService;
    this.authenticationService = authenticationService;
  }

  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('GROUPLEAD')")
  public ResponseEntity<List<UserxDto>> getAllUsers() {
    List<UserxDto> users =
        userxService.getAllUsers().stream().map(userxService::convertToDTO).toList();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/load/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<UserxDto> getUser(@PathVariable String id) {
    UserxDto user = userxService.loadUserDTOById(id);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<DeletionResponseDto> deleteUser(@PathVariable String id) {
    DeletionResponseDto response= userxService.deleteUser(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<UserxDto> updateUser(@RequestBody UserxDto userxDto) {
    Userx updatedUser = userxService.updateUser(userxDto);
    return ResponseEntity.ok(userxService.convertToDTO(updatedUser));
  }

  @PostMapping("/create")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> createUser(@RequestBody UserxDto userxDto) {
    try {
      UserxDto createdUser = authenticationService.registerUser(userxDto);
      return ResponseEntity.ok(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(Map.of("message", e.getMessage()));
    }
    catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Map.of("message", e.getMessage()));
    }
  }

  @PostMapping("/enable")
  public ResponseEntity<MessageResponse> enableUser(
      @RequestBody EnableUserRequest request) {
    userxService.enableUser(request.username(), request.token(), request.password());
    return ResponseEntity.ok(new MessageResponse("User enabled"));
  }

  @GetMapping("/managers")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
  public ResponseEntity<List<UserxDto>> getManagers() {
      List<UserxDto> managers = userxService.getManagers().stream().map(userxService::convertToDTO).toList();
      return ResponseEntity.ok(managers);
  }

    @PostMapping("/resend")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> resendValidation(@RequestBody UserxDto userxDTO) {
        try {
            authenticationService.resendValidation(userxDTO);
            return ResponseEntity.ok(Map.of("message", "Validation email sent"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

}
