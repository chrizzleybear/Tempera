package at.qe.skeleton.services;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Scope("application")
public class GroupService {

    private static final String INVALID_GROUP_ID = "Invalid group lead ID";
    private static final String INVALID_GROUPLEAD_ID = "Invalid group lead ID";
    private static final String INVALID_MEMBER_ID = "Invalid member ID";

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private GroupRepository groupRepository;
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Transactional
    public Group createGroup(String name, String description, String groupLeadId) {
        Userx groupLead = userxRepository.findById(groupLeadId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
        groupLead.addRole(UserxRole.GROUPLEAD);
        Group group = new Group(name, description, groupLead);
        return groupRepository.save(group);
    }

    @Transactional
    public Group updateGroup(Long groupId, String name, String description, String groupLeadId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        Userx groupLead = userxRepository.findById(groupLeadId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
        groupLead.addRole(UserxRole.GROUPLEAD);
        group.setName(name);
        group.setDescription(description);
        group.setGroupLead(groupLead);
        return groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        groupRepository.delete(group);
    }

    @Transactional
    public Userx addMember(Long groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        Userx member = userxRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(INVALID_MEMBER_ID));
        if(group.getMembers().contains(member)){
            throw new IllegalArgumentException("Member already exists");
        }
        group.addMember(member);
        groupRepository.save(group);
        return member;
        }

    public Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    }

    public void removeMember(Long groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        Userx member = userxRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        group.removeMember(member);
        groupRepository.save(group);
    }

    public List<Group> getGroupFromGroupLead(String userId) {
        Userx groupLead = userxRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
        List<Group> groups = groupRepository.findByGroupLead(groupLead);
        return groups;
    }
}
