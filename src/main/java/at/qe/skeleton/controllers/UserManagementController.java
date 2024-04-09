package at.qe.skeleton.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserManagementController{

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Userx>> getAllUsers() {
        List<Userx> users = userService.getAllUsers().stream().toList();
        return ResponseEntity.ok(users);
    }
}
