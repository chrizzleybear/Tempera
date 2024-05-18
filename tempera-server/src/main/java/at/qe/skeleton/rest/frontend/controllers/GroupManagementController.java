package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.MemberAssigmentDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.services.GroupService;
import at.qe.skeleton.services.UserxService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupManagementController {

  private final GroupService groupService;
  private final UserxService userxService;

  GroupManagementController(GroupService groupService, UserxService userxService) {
    this.groupService = groupService;
    this.userxService = userxService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<Group>> getAllGroups() {
    List<Group> users = groupService.getAllGroups();
    return ResponseEntity.ok(users);
  }

  @PostMapping("/create")
  public ResponseEntity<Group> createGroup(@RequestBody SimpleGroupDto groupData) {
    Group group =
        groupService.createGroup(groupData.name(), groupData.description(), groupData.groupLead());
    return ResponseEntity.ok(group);
  }

  @PutMapping("/update")
  public ResponseEntity<Group> updateGroup(@RequestBody SimpleGroupDto groupData) {
    Group updatedGroup =
        groupService.updateGroup(
            groupData.groupId(), groupData.name(), groupData.description(), groupData.groupLead());
    return ResponseEntity.ok(updatedGroup);
  }

  @DeleteMapping("delete/{groupId}")
  public ResponseEntity<Void> deleteGroup(@PathVariable String groupId) {
    groupService.deleteGroup(Long.parseLong(groupId));
    return ResponseEntity.ok().build();
  }

  @GetMapping("/load/{groupId}")
  public ResponseEntity<Group> getGroup(@PathVariable String groupId) {
    Group group = groupService.getGroup(Long.parseLong(groupId));
    return ResponseEntity.ok(group);
  }

  @GetMapping("/members/{groupId}")
  public ResponseEntity<List<UserxDto>> getMembers(@PathVariable String groupId) {
    List<UserxDto> members =
        groupService.getGroup(Long.parseLong(groupId)).getMembers().stream()
            .map(userxService::convertToDTO)
            .toList();
    return ResponseEntity.ok(members);
  }

  @PostMapping("/addMember")
  public ResponseEntity<UserxDto> addMember(@RequestBody MemberAssigmentDto memberAssigmentDto) {
    Userx member =
        groupService.addMember(memberAssigmentDto.groupId(), memberAssigmentDto.memberId());
    return ResponseEntity.ok(userxService.convertToDTO(member));
  }

  @DeleteMapping("/removeMember/{groupId}/{memberId}")
  public ResponseEntity<Void> removeMember(
      @PathVariable String groupId, @PathVariable String memberId) {
    groupService.removeMember(Long.parseLong(groupId), memberId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/groupLead/{groupLeadId}")
  public ResponseEntity<List<Group>> getGroupByGroupLead(@PathVariable String groupLeadId) {
    List<Group> groups = groupService.getGroupFromGroupLead(groupLeadId);
    return ResponseEntity.ok(groups);
  }
}
