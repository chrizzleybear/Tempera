package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InternalRecordOutOfBoundsException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.dtos.TimeTableRecordDBDto;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;
import at.qe.skeleton.rest.frontend.payload.request.SplitTimeRecordRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDescriptionRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateProjectRequest;
import at.qe.skeleton.rest.frontend.payload.response.GetTimetableDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.ProjectService;
import at.qe.skeleton.services.TimeRecordService;
import at.qe.skeleton.services.UserxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TimetableDataService {

  private final ProjectService projectService;
  private final TimeRecordService timeRecordService;
  private final UserxRepository userxRepository;
  private final UserxService userxService;

  public TimetableDataService(ProjectService projectService, TimeRecordService timeRecordService, UserxRepository userxRepository, UserxService userxService) {
    this.projectService = projectService;
    this.timeRecordService = timeRecordService;
    this.userxRepository = userxRepository;
    this.userxService = userxService;
  }

  public GetTimetableDataResponse getTimetableData(Userx user) {
    Set<TimeTableRecordDBDto> timeTableRecordDBDtos = timeRecordService.getTimeTableRecordDtosByUser(user);
    // now load a set of all projects the user is allowed to see or better even directly load the simpleProjectDtos (maybe the db version because of id long) and then connect them to the timeTableRecordDBDtos
    Set<>


    List<TimetableEntryDto> tableEntries = new ArrayList<>();
    //todo once frontend is ready add Groupx here as well (or just sent the entire Groupxproject.)
//    for (var timeRecord : timeRecords) {
//      String end;
//      // um die Arbeitszeitberechnung im Frontend nicht zu sprengen filtern wir den aktuellen TR raus.
//      if (timeRecord.getEnd() == null){
//        continue;
//      }
//        end = timeRecord.getEnd().toString();
//      Long id = timeRecord.getId();
//      String start = timeRecord.getStart().toString();
//      SimpleProjectDto simpleProjectDto = null;
//      if (timeRecord.getGroupxProject() != null){
//        Project project = timeRecord.getGroupxProject().getProject();
//        String projectId = project.getId().toString();
//        String projectName = project.getName();
//        String projectDescription = project.getDescription();
//        String projectManager = project.getManager().getUsername();
//
//        simpleProjectDto = new SimpleProjectDto(
//            projectId,
//            projectName,
//            projectDescription,
//            projectManager);
//      }
//      State state = timeRecord.getExternalRecord().getState();
//      String description = timeRecord.getDescription();
//      tableEntries.add(new TimetableEntryDto(id, start, end, simpleProjectDto, state, description));
//    }



    List<SimpleProjectDto> availableProjects =
        projectService.getProjectsByContributor(user).stream()
            .map(
                p ->
                    new SimpleProjectDto(
                        Long.toString(p.getId()),
                        p.getName(),
                        p.getDescription(),
                        p.getManager().getUsername()))
            .toList();
    return new GetTimetableDataResponse(tableEntries, availableProjects);

  }

  //todo: testing and running the system

  public MessageResponse updateProject(String username, UpdateProjectRequest request)
      throws CouldNotFindEntityException {
    // Long entryId, ExtendedProjectDto project, String description, String splitTimestamp
    InternalRecord internalRecord = getInternalRecord(request.entryId());
    Userx user = userxService.loadUser(username);
    Long projectId = Long.valueOf(request.project().projectId());
    // todo: add the group as parameter later on
    // then we need the findbyGroupIdAndProjectId...
    List<GroupxProject> groupxProjects = projectService.findGroupxProjectsByContributorAndProjectId(user, projectId);
    if(groupxProjects.isEmpty()){
      throw new CouldNotFindEntityException("No Group with ProjectId %s and User %s found".formatted(projectId, username));
    }
    GroupxProject groupxProject = groupxProjects.get(0);
    groupxProject.addInternalRecord(internalRecord);
    projectService.saveGroupxProject(groupxProject);

    return new MessageResponse(
        "Set Project %s and Group %s for %s's internal record with id %d.".formatted(groupxProject.getProject(), groupxProject.getGroup(), username, internalRecord.getId()));
  }

  public MessageResponse updateProjectDescription(String username, UpdateDescriptionRequest request)
      throws CouldNotFindEntityException {
    InternalRecord internalRecord = getInternalRecord(request.entryId());
    internalRecord.setDescription(request.description());
    internalRecord = timeRecordService.saveInternalRecord(internalRecord);

    return new MessageResponse(
        "Set new Description of %s's internal record with id %d.".formatted(username, internalRecord.getId()));
  }

  @Transactional
  public GetTimetableDataResponse splitTimeRecord(String username, SplitTimeRecordRequest request)
      throws CouldNotFindEntityException {
    InternalRecord oldInternalRecord = getInternalRecord(request.entryId());
    LocalDateTime oldEnd = oldInternalRecord.getEnd();
    LocalDateTime newEnd =
        LocalDateTime.parse(request.splitTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
    oldInternalRecord.setEnd(newEnd);
    timeRecordService.saveInternalRecord(oldInternalRecord);

    InternalRecord newInternalRecord = new InternalRecord(newEnd);
    newInternalRecord.setEnd(oldEnd);
    ExternalRecord externalRecord = oldInternalRecord.getExternalRecord();
    externalRecord.addInternalRecord(newInternalRecord);
    timeRecordService.saveInternalRecord(newInternalRecord);

    if (newEnd.isAfter(externalRecord.getEnd())) {
      throw new InternalRecordOutOfBoundsException(
          "Freshly split internalTimeRecord is ending after the ExternalRecord");
    }
    return getTimetableData(userxService.loadUser(username));
  }

  private InternalRecord getInternalRecord(Long id) throws CouldNotFindEntityException {
    return
        timeRecordService
            .findInternalRecordById(id)
            .orElseThrow(
                () ->
                    new CouldNotFindEntityException(
                        "There is no Internal Record with the Id %d".formatted(id)));
  }
}
