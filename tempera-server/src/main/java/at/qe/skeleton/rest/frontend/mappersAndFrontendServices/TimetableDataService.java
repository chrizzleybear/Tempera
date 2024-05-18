package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InternalRecordOutOfBoundsException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;
import at.qe.skeleton.rest.frontend.payload.request.SplitTimeRecordRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDescriptionRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateProjectRequest;
import at.qe.skeleton.rest.frontend.payload.response.GetTimetableDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.ProjectService;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimetableDataService {

  private final ProjectService projectService;
  private final TimeRecordService timeRecordService;

  public TimetableDataService(ProjectService projectService, TimeRecordService timeRecordService) {
    this.projectService = projectService;
    this.timeRecordService = timeRecordService;
  }

  public GetTimetableDataResponse getTimetableData(Userx user, int page, int size) {
    List<InternalRecord> records = timeRecordService.getPageOfInternalRecords(user, page, size);
    List<TimetableEntryDto> tableEntries =
        records.stream()
            .map(
                ir ->
                    new TimetableEntryDto(
                        ir.getId(),
                        ir.getStart().toString(),
                        ir.getEnd().toString(),
                        new ProjectDto(
                            ir.getAssignedProject().getId(), ir.getAssignedProject().getName()),
                        ir.getExternalRecord().getState(),
                        ir.getDescription()))
            .toList();
    // list all available Projects

    List<ProjectDto> availableProjects =
        user.getProjects().stream().map(p -> new ProjectDto(p.getId(), p.getName())).toList();

    return new GetTimetableDataResponse(tableEntries, availableProjects);
  }

  public MessageResponse updateProject(UpdateProjectRequest request)
      throws CouldNotFindEntityException {
    // Long entryId, ProjectDto project, String description, String splitTimestamp
    InternalRecord internalRecord = getInternalRecord(request.entryId());

    String projectDtoId = request.project().id();
    Project project = projectService.loadProject(Long.valueOf(projectDtoId));
    internalRecord.setAssignedProject(project);
    internalRecord = timeRecordService.saveInternalRecord(internalRecord);
    return new MessageResponse(
        "Successfully updated internal record with id %d.".formatted(internalRecord.getId()));
  }

  public MessageResponse updateProjectDescription(UpdateDescriptionRequest request)
      throws CouldNotFindEntityException {
    InternalRecord internalRecord = getInternalRecord(request.entryId());
    internalRecord.setDescription(request.description());
    internalRecord = timeRecordService.saveInternalRecord(internalRecord);
    return new MessageResponse(
        "Successfully updated internal record with id %d.".formatted(internalRecord.getId()));
  }

  @Transactional
  public MessageResponse splitTimeRecord(SplitTimeRecordRequest request)
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
        "Successfully set End for oldInternalRecord with id %d to %s and created new Internal Record"
            .formatted(oldInternalRecord.getId(), oldInternalRecord.getEnd()));
  }

  private InternalRecord getInternalRecord(Long id) throws CouldNotFindEntityException {
    InternalRecord internalRecord =
        timeRecordService
            .findInternalRecordById(id)
            .orElseThrow(
                () ->
                    new CouldNotFindEntityException(
                        "There is no Internal Record with the Id %d".formatted(id)));
  }
}
