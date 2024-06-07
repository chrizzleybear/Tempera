package at.qe.skeleton.services;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.repositories.GroupRepository;
import at.qe.skeleton.repositories.GroupxProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Scope("application")
public class GroupService {

    private static final String INVALID_GROUP_ID = "Invalid group lead ID";
    private static final String INVALID_GROUPLEAD_ID = "Invalid group lead ID";
    private static final String INVALID_MEMBER_ID = "Invalid member ID";
    private static final String INVALID_MANAGER_ID = "Invalid manager ID";

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupxProjectRepository groupxProjectRepository;


    /**
     * will mostly be used to get all active groups as options for adding to a project
     * @return
     */
    public List<SimpleGroupDto> getAllActiveGroups() {
        List<Groupx> groups = groupRepository.findAllActive();
        List<SimpleGroupDto> dtos = groups.stream()
                .map(group -> new SimpleGroupDto(String.valueOf(group.getId()), group.isActive(), group.getName(), group.getDescription(), group.getGroupLead().getId()))
                .toList();
        return dtos;
    }

    @Transactional
    public Groupx createGroup(String name, String description, String groupLeadId) {
        Userx groupLead = userxRepository.findById(groupLeadId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
        groupLead.addRole(UserxRole.GROUPLEAD);
        Groupx group = new Groupx(name, description, groupLead);
        return groupRepository.save(group);
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAnyAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('MANAGER') or hasAnyAuthority('ADMIN')")
    @Transactional
    public void deleteGroup(Long groupId) {
        Groupx group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        groupRepository.delete(group);
    }

    @PreAuthorize("hasAuthority('GROUPLEAD') or hasAnyAuthority('ADMIN')")
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
                Groupx group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        return group;
    }



    @PreAuthorize("hasAuthority('GROUPLEAD') or hasAnyAuthority('ADMIN')")
    public void removeMember(Long groupId, String memberId) {
        Groupx group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        Userx member = userxRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        group.removeMember(member);
        groupRepository.save(group);
    }

    @PreAuthorize("hasAuthority('GROUPLEAD') or hasAnyAuthority('ADMIN')")
    public List<Groupx> getGroupsByGroupLead(String userId) {
        Userx groupLead = userxRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUPLEAD_ID));
        List<Groupx> groups = groupRepository.findByGroupLead(groupLead);
        return groups;
    }


    @PreAuthorize("hasAuthority('MANAGER') or hasAnyAuthority('ADMIN')")
    public List<Groupx> getGroupsByManager(String userId) {
        Userx manager = userxRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(INVALID_MANAGER_ID));
        return groupRepository.findAllByManager(manager);
    }

    public List<Userx> getMembers(Long groupId) {
        Groupx group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(INVALID_GROUP_ID));
        return group.getMembers();
    }

    public void saveGroup(Groupx group){
        groupRepository.save(group);
    }


}
