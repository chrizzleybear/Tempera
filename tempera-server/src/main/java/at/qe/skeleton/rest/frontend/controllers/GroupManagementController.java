package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.GroupMapperService;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.UserMapper;
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
  private GroupMapperService groupMapperService;
  private UserMapper userMapper;

  GroupManagementController(GroupService groupService, UserxService userxService, GroupMapperService groupMapperService, UserMapper userMapper) {
    this.groupService = groupService;
    this.userxService = userxService;
    this.groupMapperService =  groupMapperService;
    this.userMapper = userMapper;
  }

  @GetMapping("/all")
  public ResponseEntity<List<SimpleGroupDto>> getAllActiveGroups() {
    List<SimpleGroupDto> groups = groupService.getAllActiveGroups();
    return ResponseEntity.ok(groups);
  }

  @PostMapping("/create")
  public ResponseEntity<SimpleGroupDto> createGroup(@RequestBody SimpleGroupDto groupData) {
    SimpleGroupDto group = groupMapperService.createGroup(groupData);
    return ResponseEntity.ok(group);
  }

  @PutMapping("/update")
  public ResponseEntity<SimpleGroupDto> updateGroup(@RequestBody SimpleGroupDto groupData) {
    SimpleGroupDto updatedGroup = groupMapperService.updateGroup(groupData);
    return ResponseEntity.ok(updatedGroup);
  }

  @DeleteMapping("delete/{groupId}")
  public ResponseEntity<Void> deleteGroup(@PathVariable String groupId) {
    groupService.deleteGroup(Long.parseLong(groupId));
    return ResponseEntity.ok().build();
  }

  @GetMapping("/load/{groupId}")
  public ResponseEntity<GroupDetailsDto> getGroup(@PathVariable String groupId) {
    GroupDetailsDto group = groupMapperService.getGroupDetailsDto(Long.parseLong(groupId));
    return ResponseEntity.ok(group);
  }

  @GetMapping("/members/{groupId}")
  public ResponseEntity<List<SimpleUserDto>> getMembers(@PathVariable String groupId) {
    List<SimpleUserDto> members =
            groupService.getGroup(Long.parseLong(groupId)).getMembers().stream()
                    .map(userMapper::getSimpleUser)
                    .toList();
    return ResponseEntity.ok(members);
  }

  @PostMapping("/addMember")
  public ResponseEntity<SimpleUserDto> addMember(@RequestBody MemberAssigmentDto memberAssigmentDto) {
    Userx member =
            groupService.addMember(memberAssigmentDto.groupId(), memberAssigmentDto.memberId());
    return ResponseEntity.ok(userMapper.getSimpleUser(member));
  }

  @DeleteMapping("/removeMember/{groupId}/{memberId}")
  public ResponseEntity<Void> removeMember(
          @PathVariable String groupId, @PathVariable String memberId) {
    groupService.removeMember(Long.parseLong(groupId), memberId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/groupLead/{groupLeadId}")
  public ResponseEntity<List<SimpleGroupDto>> getGroupsByGroupLead(@PathVariable String groupLeadId) {
    List<SimpleGroupDto> groups = groupMapperService.getSimpleGroupDtosByGroupLead(groupLeadId);
    return ResponseEntity.ok(groups);
  }
}
