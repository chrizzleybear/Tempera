package at.qe.skeleton.controllers;

import at.qe.skeleton.model.DTOs.UserxDTO;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.AuthenticationService;
import at.qe.skeleton.services.UserxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public ResponseEntity<List<UserxDTO>> getAllUsers() {
        List<UserxDTO> users = userxService.getAllUsers().stream()
                .map(userxService::convertToDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<UserxDTO> getUser(@PathVariable String id) {
        UserxDTO user = userxService.loadUserDTOById(id);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
        userxService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<UserxDTO> updateUser(@RequestBody UserxDTO userxDto) {
        Userx updatedUser = userxService.updateUser(userxDto);
        return ResponseEntity.ok(userxService.convertToDTO(updatedUser));
    }

    @PostMapping("/create")
    public ResponseEntity<UserxDTO> createUser(@RequestBody UserxDTO userxDto) {
        UserxDTO createdUser = authenticationService.registerUser(userxDto);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/validate")
    public ResponseEntity<UserxDTO> validateUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        UserxDTO isValidUser = userxService.validateUser(username, password);
        return ResponseEntity.ok(isValidUser);
    }

    @PostMapping("/enable")
    public ResponseEntity<String> enableUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        userxService.enableUser(username, password);
        return ResponseEntity.ok("You have been enabled!");
    }
}
