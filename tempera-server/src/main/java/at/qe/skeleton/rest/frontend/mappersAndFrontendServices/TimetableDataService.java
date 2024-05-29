package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InternalRecordOutOfBoundsException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.dtos.GroupxProjectStateTimeDbDto;
import at.qe.skeleton.model.dtos.SimpleProjectDbDto;
import at.qe.skeleton.model.dtos.TimeTableRecordDBDto;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
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
  private final ProjectMapperService projectMapperService;
  private final UserxService userxService;

  public TimetableDataService(ProjectService projectService, TimeRecordService timeRecordService, UserxRepository userxRepository, UserxService userxService, ProjectMapperService projectMapperService) {
    this.projectService = projectService;
    this.timeRecordService = timeRecordService;
    this.projectMapperService = projectMapperService;
    this.userxService = userxService;
  }

  /**
   * Fetches all TimeTableRecords for a user and returns them as a GetTimetableDataResponse. The response
   * includes a list of TimetableEntryDto and a list of SimpleGroupxProjectDto. The TimetableEntryDto also provides
   * information about the GroupxProject (gxp) that may be connected to that timeRecord or null. A connected gxp
   * can already be deactivated (and thus not be among the user available Projects) but the user still has a record of it.
   * @param username
   * @return
   */
  public GetTimetableDataResponse getTimetableData(String username) throws CouldNotFindEntityException{
    Set<TimeTableRecordDBDto> timeTableRecordDBDtos = timeRecordService.getTimeTableRecordDtosByUser(username);
    timeTableRecordDBDtos.removeIf(record -> record.end() == null);
    Set<SimpleGroupxProjectDto> simpleGroupxProjectDtoSet = projectService.getSimpleGroupxProjectDtoByUser(username);
    List<TimetableEntryDto> tableEntries = new ArrayList<>();
    for (TimeTableRecordDBDto record : timeTableRecordDBDtos) {
      TimetableEntryDto entry = timeTableEntryDtoBuilder(record);
      tableEntries.add(entry);
    }
    // todo: include GroupxProject once frontend is ready
    List<SimpleGroupxProjectDto> availableProjects = simpleGroupxProjectDtoSet.stream().toList();
    return new GetTimetableDataResponse(tableEntries, availableProjects);
  }

  /**
   * This method builds a TimetableEntryDto from a TimeTableRecordDBDto. The TimeTableRecordDBDto provides
   * the groupId and projectId. The method will  make a call to the db and fetch the needed information
   * about Group and Project Details to build the TimetableEntryDto. The reason why we fetch the Gxp from the Db
   * is that it may not be among the users GroupxProjects. This is the case when the gxp was already deactivated
   * but the user still has a record of it.
   * @param record
   * @return
   */
  private TimetableEntryDto timeTableEntryDtoBuilder(TimeTableRecordDBDto record) throws CouldNotFindEntityException {
    SimpleGroupxProjectDto simpleGroupxProjectDto = null;

    if(record.groupId() != null && record.projectId() != null){
      GroupxProject groupxProject =
          projectService.findByGroupAndProjectDetailed(record.groupId(), record.projectId());
       simpleGroupxProjectDto =
          projectMapperService.mapToSimpleGroupxProjectDto(groupxProject);
    }
            return new TimetableEntryDto(
        record.recordId(),
        record.start().format(DateTimeFormatter.ISO_DATE_TIME),
        record.end().format(DateTimeFormatter.ISO_DATE_TIME),
        simpleGroupxProjectDto,
        record.state(),
        record.description());
  }

  //todo: testing and running the system

  public MessageResponse updateProject(String username, UpdateProjectRequest request)
      throws CouldNotFindEntityException {
    // Long entryId, ExtendedProjectDto project, String description, String splitTimestamp
    InternalRecord internalRecord = getInternalRecord(request.entryId());
    Userx user = userxService.loadUser(username);
    Long projectId = Long.valueOf(request.projectId());
    Long groupId = Long.valueOf(request.groupId());
    // todo: add the group as parameter later on
    // then we need the findbyGroupIdAndProjectId...
    GroupxProject groupxProject= projectService.findByGroupAndProject(groupId, projectId);
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
    return getTimetableData(username);
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
