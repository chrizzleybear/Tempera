package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.dtos.GroupxProjectStateTimeDto;
import at.qe.skeleton.rest.frontend.payload.response.AccumulatedTimeResponse;
import at.qe.skeleton.services.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccumulatedTimeMapper {

    ProjectService projectService;




    public AccumulatedTimeResponse getManagerTimeData(String username) {

        List<GroupxProjectStateTimeDto> groupxProjectStateTimeDtos = projectService.gxpStateTimeDtosByManager(username);


        return null;
    };

    public AccumulatedTimeResponse getGroupLeadTimeData(String username) {
        return null;
    };
}
