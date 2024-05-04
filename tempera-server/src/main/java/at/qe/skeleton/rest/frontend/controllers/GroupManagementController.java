package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.services.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/groups")
public class GroupManagementController {

    private final GroupService groupService;

    public GroupManagementController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllUsers() {
        List<Group> users =
                groupService.getAllGroups();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestParam String name, @RequestParam String description, @RequestParam String groupLeadId) {
        Group group = groupService.createGroup(name, description, groupLeadId);
        return ResponseEntity.ok(group);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Group> updateGroup(@PathVariable String groupId, @RequestParam String name, @RequestParam String description) {
        Group updatedGroup = groupService.updateGroup(groupId, name, description);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }
}
