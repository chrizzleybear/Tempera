package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InternalRecordOutOfBoundsException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.repositories.GroupxProjectRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;
import at.qe.skeleton.rest.frontend.payload.request.SplitTimeRecordRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDescriptionRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateProjectRequest;
import at.qe.skeleton.rest.frontend.payload.response.GetTimetableDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.GroupService;
import at.qe.skeleton.services.ProjectService;
import at.qe.skeleton.services.TimeRecordService;
import at.qe.skeleton.services.UserxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

  public GetTimetableDataResponse getTimetableData(Userx user, int page, int size) {
    List<InternalRecord> timeRecords = timeRecordService.getPageOfInternalRecords(user, page, size);
    List<TimetableEntryDto> tableEntries = new ArrayList<>();
    //todo once frontend is ready add Groupx here as well (or just sent the entire Groupxproject.)
    for (var timeRecord : timeRecords) {
      Long id = timeRecord.getId();
      String start = timeRecord.getStart().toString();
      String end = timeRecord.getEnd() == null ? null : timeRecord.getEnd().toString();
      String projectId;
      String projectName;
      String projectDescription;
      String projectManager;
      if (timeRecord.getGroupxProject() == null){
        projectId = null;
        projectName = null;
        projectDescription = null;
        projectManager = null;
      }
      else {
        projectId = timeRecord.getGroupxProject().getProject().getId().toString();
        projectName = timeRecord.getGroupxProject().getProject().getName();
        projectDescription = timeRecord.getGroupxProject().getProject().getDescription();
        projectManager = timeRecord.getGroupxProject().getProject().getManager().getUsername();
      }
      ProjectDto project = new ProjectDto(projectId, projectName, projectDescription, projectManager);
      State state = timeRecord.getExternalRecord().getState();
      String description = timeRecord.getDescription();
      tableEntries.add(new TimetableEntryDto(id, start, end, project, state, description));
    }
    List<ProjectDto> availableProjects = projectService.getProjectsByContributor(user).stream().map(p -> new ProjectDto(Long.toString(p.getId()), p.getName(), p.getDescription(), p.getManager().getUsername())).toList();
    return new GetTimetableDataResponse(tableEntries, availableProjects);
  }

  //todo: testing and running the system

  public MessageResponse updateProject(String username, UpdateProjectRequest request)
      throws CouldNotFindEntityException {
    // Long entryId, ProjectDto project, String description, String splitTimestamp
    InternalRecord internalRecord = getInternalRecord(request.entryId());
    Userx user = userxService.loadUser(username);
    Long projectId = Long.valueOf(request.project().id());
    // todo: add the group as parameter later on
    // then we need the findbyGroupIdAndProjectId...

    //  todo: write tests for this service class
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
  public MessageResponse splitTimeRecord(String username, SplitTimeRecordRequest request)
      throws CouldNotFindEntityException {
    InternalRecord oldInternalRecord = getInternalRecord(request.entryId());
    LocalDateTime oldEnd = oldInternalRecord.getEnd();
    LocalDateTime newEnd = LocalDateTime.parse(request.splitTimestamp());
    oldInternalRecord.setEnd(newEnd);
    timeRecordService.saveInternalRecord(oldInternalRecord);

    InternalRecord newInternalRecord = new InternalRecord(newEnd.minusSeconds(1L));
    newInternalRecord.setEnd(oldEnd);
    ExternalRecord externalRecord = oldInternalRecord.getExternalRecord();
    externalRecord.addInternalRecord(newInternalRecord);
    timeRecordService.saveInternalRecord(newInternalRecord);

    if (newEnd.isAfter(externalRecord.getEnd())) {
      throw new InternalRecordOutOfBoundsException(
          "Freshly split internalTimeRecord is ending after the ExternalRecord");
    }

    return new MessageResponse(
        "Set End at %s for oldInternalRecord of User %s with id %d to %s and created new Internal Record"
            .formatted(newEnd.toString(), username, oldInternalRecord.getId(), oldInternalRecord.getEnd()));
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
