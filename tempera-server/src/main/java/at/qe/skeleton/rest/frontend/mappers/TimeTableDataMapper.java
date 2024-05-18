package at.qe.skeleton.rest.frontend.mappers;

import at.qe.skeleton.model.InternalRecord;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.InternalRecordRepository;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.dtos.TimetableEntryDto;
import at.qe.skeleton.rest.frontend.payload.response.GetTimetableDataResponse;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeTableDataMapper {

    private TimeRecordService timeRecordService;



    public GetTimetableDataResponse getTimetableData(Userx user, int page, int size){
        List<InternalRecord> records = timeRecordService.getPageOfInternalRecords(user, page, size);
        List<TimetableEntryDto> tableEntries =
                records
                        .stream()
                        .map(ir ->
                new TimetableEntryDto(ir.getId(),
                        ir.getStart().toString(),
                        ir.getEnd().toString(),
                        new ProjectDto(ir.getAssignedProject().getId(), ir.getAssignedProject().getName()),
                        ir.getExternalRecord().getState(),
                        ir.getDescription()))
                        .toList();
        // list all available Projects

        List<ProjectDto> availableProjects =
                user.getProjects()
                        .stream()
                        .map(p -> new ProjectDto(p.getId(), p.getName()))
                        .toList();

        return new GetTimetableDataResponse(tableEntries, availableProjects);
    }

}
