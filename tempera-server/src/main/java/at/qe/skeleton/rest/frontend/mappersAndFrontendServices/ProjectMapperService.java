package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.GroupxProjectDto;
import at.qe.skeleton.services.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMapperService {
    private ProjectService projectService;


    public ProjectMapperService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<GroupxProjectDto> getAllGroupxProjectsAsManager(String managerId) {
        return null;
    }

    public List<GroupxProjectDto> getAllGroupxProjectsAsGrouplead(String managerId) {
        return null;
    }


}
