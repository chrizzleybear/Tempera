package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/groups")
public class GroupManagementController {

    @Autowired
    GroupService groupService;

    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> users =
                groupService.getAllGroups();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody Map<String, String> groupData) {
        Group group = groupService.createGroup(groupData.get("name"), groupData.get("description"), groupData.get("groupLead"));
        return ResponseEntity.ok(group);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Group> updateGroup(@PathVariable String groupId, @RequestParam String name, @RequestParam String description) {
        Group updatedGroup = groupService.updateGroup(groupId, name, description);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("delete/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String groupId) {
        groupService.deleteGroup(Long.parseLong(groupId));
        return ResponseEntity.ok().build();

    }
}
