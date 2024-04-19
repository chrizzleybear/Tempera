package at.qe.skeleton.controllers;

import at.qe.skeleton.model.Userx;
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

    public UserManagementController(UserxService userxService) {
        this.userxService = userxService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Userx>> getAllUsers() {
        List<Userx> users = userxService.getAllUsers().stream().toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<Userx> getUser(@PathVariable String id) {
        Userx user = userxService.loadUser(id);
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
    public ResponseEntity<Userx> updateUser(@RequestBody Userx user) {
        Userx updatedUser = userxService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/create")
    public ResponseEntity<Userx> createUser(@RequestBody Userx user) {
        Userx createdUser = userxService.saveUser(user);
        return ResponseEntity.ok(createdUser);
    }
}
