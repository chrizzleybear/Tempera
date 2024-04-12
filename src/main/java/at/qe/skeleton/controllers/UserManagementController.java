package at.qe.skeleton.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
@RequestMapping("/api/users")
public class UserManagementController{

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Userx>> getAllUsers() {
        List<Userx> users = userService.getAllUsers().stream().toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Userx> getUser(@PathVariable String id) {
        Userx user = userService.loadUser(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }
}
