package at.qe.skeleton.controllers;

import at.qe.skeleton.model.DTOs.UserDTO;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.AuthenticationService;
import at.qe.skeleton.services.UserxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
@RequestMapping("/api/users")
public class UserManagementController{

    private final UserxService userxService;
    private final AuthenticationService authenticationService;

    public UserManagementController(UserxService userxService, AuthenticationService authenticationService) {
        this.userxService = userxService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userxService.getAllUsers().stream()
                .map(userxService::convertToDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        Userx user = userxService.loadUser(id);
        return ResponseEntity.ok(userxService.convertToDTO(user));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
        userxService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDto) {
        Userx updatedUser = userxService.updateUser(userxService.convertToEntity(userDto));
        return ResponseEntity.ok(userxService.convertToDTO(updatedUser));
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto) {
        UserDTO createdUser = authenticationService.registerUser(userDto);
        return ResponseEntity.ok(createdUser);
    }
}
