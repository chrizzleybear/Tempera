package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.dtos.GroupxProjectStateTimeDto;
import at.qe.skeleton.rest.frontend.dtos.AccumulatedTimeDto;
import at.qe.skeleton.rest.frontend.payload.response.AccumulatedTimeResponse;
import at.qe.skeleton.services.ProjectService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AccumulatedTimeMapper {

    ProjectService projectService;




    public AccumulatedTimeResponse getManagerTimeData(String username) {

        List<GroupxProjectStateTimeDto> groupxProjectStateTimeDtos = projectService.gxpStateTimeDtosByManager(username);
        List<AccumulatedTimeDto> accumulatedTimeDtos = groupxProjectStateTimeDtos.stream()
                .map(this::mapToAccumulatedTimeDto)
                .toList();

        //all available Projects for this manager -> all Projects where this manager is assigned

        //all available Groups for this manager -> all groups that are assigned to the projects where this manager is assigned


        return null;
    };

    public AccumulatedTimeResponse getGroupLeadTimeData(String username) {
        return null;
    };



    private AccumulatedTimeDto mapToAccumulatedTimeDto(GroupxProjectStateTimeDto groupxProjectStateTimeDto) {
        return new AccumulatedTimeDto(
                groupxProjectStateTimeDto.projectId().toString(),
                groupxProjectStateTimeDto.groupId().toString(),
                groupxProjectStateTimeDto.state(),
                groupxProjectStateTimeDto.start().format(DateTimeFormatter.ISO_DATE_TIME),
                groupxProjectStateTimeDto.end().format(DateTimeFormatter.ISO_DATE_TIME));
    }


}



