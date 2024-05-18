package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InternalRecordOutOfBoundsException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.State;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimetableDataService {

  private final ProjectService projectService;
  private final TimeRecordService timeRecordService;
  private final GroupService groupService;

  public TimetableDataService(ProjectService projectService, TimeRecordService timeRecordService, GroupService groupService) {
    this.projectService = projectService;
    this.timeRecordService = timeRecordService;
    this.groupService = groupService;
  }

  public GetTimetableDataResponse getTimetableData(Userx user, int page, int size) {
    List<InternalRecord> records = timeRecordService.getPageOfInternalRecords(user, page, size);
    List<TimetableEntryDto> tableEntries = new ArrayList<>();
    for (var record : records) {
      Long id = record.getId();
      String start = record.getStart().toString();
      String end = record.getEnd() == null ? null : record.getEnd().toString();
      String projectId;
      String projectName;
      if (record.getAssignedProject() == null){
        projectId = null;
        projectName = null;
      }
      else {
        projectId = record.getAssignedProject().getId().toString();
        projectName = record.getAssignedProject().getName();
      }
      ProjectDto project = new ProjectDto(projectId, projectName);
      State state = record.getExternalRecord().getState();
      String description = record.getDescription();
      tableEntries.add(new TimetableEntryDto(id, start, end, project, state, description));
    }

    List<ProjectDto> availableProjects =
        user.getProjects().stream().map(p -> new ProjectDto(Long.toString(p.getId()), p.getName())).toList();

    return new GetTimetableDataResponse(tableEntries, availableProjects);
  }

  //todo: we need to set group here as well.

  public MessageResponse updateProject(String username, UpdateProjectRequest request)
      throws CouldNotFindEntityException {
    // Long entryId, ProjectDto project, String description, String splitTimestamp
    InternalRecord internalRecord = getInternalRecord(request.entryId());

    String projectDtoId = request.project().id();
    Project project = projectService.loadProject(Long.valueOf(projectDtoId));
    internalRecord.setAssignedProject(project);

    // set the group over which this user is assigned to that project as well
    List<Groupx> groupList =  groupService.findGroupByUsernameAndProjectId(username, project.getId());
    if(groupList.isEmpty()){
      throw new CouldNotFindEntityException("No Group with ProjectId %s and User %s found".formatted(project.getId(), username)));
    }
    if (groupList.size() > 1){
      throw new RuntimeException("The User %s is assigned to the project %s over more than one Group".formatted(username, project.getName()));
    }
    internalRecord.setAssignedGroup(groupList.get(0));
    internalRecord = timeRecordService.saveInternalRecord(internalRecord);

    return new MessageResponse(
        "Set Project %s for %s's internal record with id %d.".formatted(project.getName(),username, internalRecord.getId()));
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
