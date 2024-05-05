package at.qe.skeleton.services;

import at.qe.skeleton.model.Group;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Scope("application")
public class GroupService {

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private GroupRepository groupRepository;

    //@PreAuthorize("hasAuthority('GROUPLEAD')")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Transactional
    public Group createGroup(String name, String description, String groupLeadId) {
        Userx groupLead = userxRepository.findById(groupLeadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group lead ID"));
        Group group = new Group(name, description, groupLead);
        return groupRepository.save(group);
    }

    @Transactional
    public Group updateGroup(Long groupId, String name, String description, String groupLeadId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));
        group.setName(name);
        group.setDescription(description);
        group.setGroupLead(userxRepository.findById(groupLeadId).orElseThrow(() -> new IllegalArgumentException("Invalid group lead ID")));
        return groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));
        groupRepository.delete(group);
    }

    @Transactional
    public void addMember(Long groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));
        Userx member = userxRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        group.addMember(member);
        groupRepository.save(group);
        }


    public Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));
    }
}
