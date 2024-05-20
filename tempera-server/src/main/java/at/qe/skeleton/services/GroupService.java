package at.qe.skeleton.services;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.GroupxProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Autowired
    private GroupxProjectRepository groupxProjectRepository;

    public List<Groupx> getAllGroups() {
        return groupRepository.findAll();
    }

    @Transactional
    public Groupx createGroup(String name, String description, String groupLeadId) {
        Userx groupLead = userxRepository.findById(groupLeadId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
        groupLead.addRole(UserxRole.GROUPLEAD);
        Groupx group = new Groupx(name, description, groupLead);
        return groupRepository.save(group);
    }

    @Transactional
    public Groupx updateGroup(Long groupId, String name, String description, String groupLeadId) {
        Groupx group = groupRepository.findById(groupId)
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
        Groupx group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        groupRepository.delete(group);
    }

    @Transactional
    public Userx addMember(Long groupId, String memberId) {
        Groupx group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        Userx member = userxRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(INVALID_MEMBER_ID));
        if(group.getMembers().contains(member)){
            throw new IllegalArgumentException("Member already exists");
        }
        group.addMember(member);
        groupRepository.save(group);
        return member;
        }

    public Groupx getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
    }

    public void removeMember(Long groupId, String memberId) {
        Groupx group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        Userx member = userxRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        group.removeMember(member);
        groupRepository.save(group);
    }

    public List<Groupx> getGroupFromGroupLead(String userId) {
        Userx groupLead = userxRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
        List<Groupx> groups = groupRepository.findByGroupLead(groupLead);
        return groups;
    }

    public void saveGroup(Groupx group){
        groupRepository.save(group);
    }


}
