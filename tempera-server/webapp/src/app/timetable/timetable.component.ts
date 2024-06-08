import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import {
  ColleagueStateDto, SimpleGroupxProjectDto,
  TimetableControllerService, TimetableEntryDto,
} from '../../api';
import { Table, TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { DatePipe, NgForOf, NgIf, NgStyle } from '@angular/common';
import { TagModule } from 'primeng/tag';
import { DisplayHelper } from '../_helpers/display-helper';
import { WrapFnPipe } from '../_pipes/wrap-fn.pipe';
import { DropdownModule } from 'primeng/dropdown';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule, ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MultiSelectModule } from 'primeng/multiselect';
import StateEnum = ColleagueStateDto.StateEnum;
import { ButtonModule } from 'primeng/button';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { DialogModule } from 'primeng/dialog';
import { CalendarModule } from 'primeng/calendar';
import { TooltipModule } from 'primeng/tooltip';
import { MessageModule } from 'primeng/message';
import { MessagesModule } from 'primeng/messages';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CardModule } from 'primeng/card';
import { DateTime } from 'luxon';
import { SkeletonModule } from 'primeng/skeleton';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TotalTimeHelper } from '../_helpers/total-time-helper';
import { OverlappingProjectHelper } from '../_helpers/overlapping-project-helper';

/**
 * InternalTimetableEntryDto extends TimetableEntryDto with additional properties startTime, endTime and showProjectDropdown.
 * ShowProjectDropdown is used to determine if the project dropdown should be shown in the table, or not.
 * If the assigned Project for that entry is no longer available to the user, the dropdown should not be shown.
 */
interface InternalTimetableEntryDto extends TimetableEntryDto {
  startTime: Date;
  endTime: Date;
  showProjectDropdown: boolean;
}

@Component({
  selector: 'app-timetable',
  standalone: true,
  imports: [
    TableModule,
    InputTextModule,
    DatePipe,
    TagModule,
    WrapFnPipe,
    DropdownModule,
    FormsModule,
    MultiSelectModule,
    ButtonModule,
    OverlayPanelModule,
    DialogModule,
    CalendarModule,
    ReactiveFormsModule,
    TooltipModule,
    MessageModule,
    NgIf,
    MessagesModule,
    ToastModule,
    InputTextareaModule,
    CardModule,
    SkeletonModule,
    ProgressSpinnerModule,
    NgStyle,
    NgForOf,
  ],
  templateUrl: './timetable.component.html',
  styleUrl: './timetable.component.css',
})
export class TimetableComponent implements OnInit {
  public tableEntries?: InternalTimetableEntryDto[];

  public availableProjects: SimpleGroupxProjectDto[] = [];

  public deactivatedProjects: SimpleGroupxProjectDto[] = [];

  public filterFields: string[] = [];

  public stateOptions: StateEnum[] = (Object.values(StateEnum));

  public selectedEntry?: InternalTimetableEntryDto;

  protected readonly DisplayHelper = DisplayHelper;

  public totalTime: { hours: number, minutes: number } = { hours: 0, minutes: 0 };

  public editDescriptionVisible: boolean = false;

  public splitVisible: boolean = false;
  public calendarVisible: boolean = false;

  /*
  Used for handling when a user is assigned to a project from multiple groups
  Key is the projectId and value is an object containing the projects with the same projectId and the original name of the project
   */
  private duplicatedProjects: Map<string, {
    projects: SimpleGroupxProjectDto[],
    originalName: string
  }> = new Map<string, {
    projects: SimpleGroupxProjectDto[],
    originalName: string
  }>();

  protected readonly Date = Date;
  protected readonly StateEnum = StateEnum;


  /*
  * This reference to the PrimeNG table is used because its entries also reflect the correct order if the table is sorted and the available entries when filtered.
  * Setter is needed because of ngIf directive in the template (table is not available when component is initialized)
   */
  private table?: Table;

  @ViewChild('table') set tableRef(tableRef: Table) {
    if (tableRef) {
      this.table = tableRef;
      this.calculateTotalTime();
    }
  }

  /*
    * Validates that the time is between the start and end time of the time entry.
    * Time entry is selected by the selectedEntry.
   */
  inPermittedTimeRangeValidator: ValidatorFn = (control: AbstractControl<Date | undefined>): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }

    const minStartTime = this.selectedEntry!.startTime;
    const maxEndTime = this.selectedEntry!.endTime;

    if (control.value > minStartTime && control.value < maxEndTime) {
      return null;
    } else {
      return { notInRange: true };
    }
  };

  public splitForm = new FormGroup({
    time: new FormControl<Date | undefined>(undefined, {
      nonNullable: true,
      validators: [Validators.required, this.inPermittedTimeRangeValidator],
    }),
  });

  public descriptionForm = new FormGroup({
    description: new FormControl<string | undefined>(undefined, {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  constructor(public timetableControllerService: TimetableControllerService, private messageService: MessageService, private cd: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.timetableControllerService.getTimetableData().subscribe({
        next: data => {
          this.tableEntries = data.tableEntries?.map(entry => ({
            ...entry,
            startTime: new Date(entry.startTimestamp),
            endTime: new Date(entry.endTimestamp),
            showProjectDropdown: !(entry.assignedGroupxProject) ||
              (data.availableProjects?.some(project =>
              project.projectId === entry.assignedGroupxProject?.projectId && project.groupId === entry.assignedGroupxProject?.groupId) ?? true),
          })) ?? [];

          this.availableProjects = data.availableProjects ?? [];
          this.deactivatedProjects = this.tableEntries?.filter(entry => !entry.showProjectDropdown)?.map(entry => entry.assignedGroupxProject!) ?? [];

          // Rename projects that have the same projectId
          this.duplicatedProjects = OverlappingProjectHelper.getDuplicatedProjects(this.availableProjects);
          OverlappingProjectHelper.renameOverlappingProjects(this.duplicatedProjects, this.availableProjects);
          const assignedProjects = this.tableEntries.filter(x => x?.assignedGroupxProject).map(entry => entry.assignedGroupxProject!);
          OverlappingProjectHelper.renameOverlappingProjects(this.duplicatedProjects, assignedProjects);

          this.filterFields = Object.keys(this.tableEntries?.[0] ?? []);
        },
        error: err => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load timetable data' });
          console.error(err);
        },
      },
    );
  }

  updateProject(newProject: SimpleGroupxProjectDto, timeEntryId: number) {

    // give the project back the original name if it is a duplicate
    let submitProject = structuredClone(newProject);
    if (this.duplicatedProjects.has(submitProject.projectId ?? '')) {
      submitProject.projectName = this.duplicatedProjects.get(submitProject.projectId!)?.originalName;
    }

    this.timetableControllerService.updateProject1({
      entryId: timeEntryId,
      projectId: submitProject.projectId!,
      groupId: submitProject.groupId!,
    }).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Project updated successfully' });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update project' });
        console.error('Failed to update project for time entry', timeEntryId);
      },
    });
  }

  onOpenSplitForm(entry: InternalTimetableEntryDto) {
    this.selectedEntry = entry;
    this.splitForm.controls.time.setValue(entry.startTime);
    this.splitVisible = true;
  }

  onSplitFormSubmit() {
    const timeEntryId = this.selectedEntry!.id;

    const time = DateTime.fromJSDate(this.splitForm.controls.time.value!).toString();
    this.timetableControllerService.splitTimeRecord({ entryId: timeEntryId, splitTimestamp: time }).subscribe(
      {
        next: data => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Time entry split successfully' });
          this.splitVisible = false;
          this.tableEntries = data.tableEntries?.map(entry => ({
            ...entry,
            startTime: new Date(entry.startTimestamp),
            endTime: new Date(entry.endTimestamp),
            showProjectDropdown: entry.state !== StateEnum.OutOfOffice && this.availableProjects.some(project =>
              project.projectId === entry.assignedGroupxProject?.projectId && project.groupId === entry.assignedGroupxProject?.groupId),
          })) ?? [];

          this.availableProjects = data.availableProjects ?? [];
          this.deactivatedProjects = this.tableEntries?.filter(entry => entry.showProjectDropdown)?.map(entry => entry.assignedGroupxProject!) ?? [];

        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to split time entry' });
          console.error('Failed to split time entry');
        },
      },
    );
  }

  onEditDescription(entry: InternalTimetableEntryDto) {
    this.editDescriptionVisible = true;
    this.selectedEntry = entry;
    this.descriptionForm.controls.description.setValue(entry.description);
  }

  onDescriptionFormSubmit() {
    const timeEntryId = this.selectedEntry!.id;
    const description = this.descriptionForm.controls.description.value!;
    this.timetableControllerService.updateDescription({ entryId: timeEntryId, description }).subscribe(
      {
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Description updated successfully',
          });
          this.tableEntries!.find(entry => entry.id === timeEntryId)!.description = description;
          this.editDescriptionVisible = false;
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update description' });
          console.error('Failed to update description for time entry', timeEntryId);
        },
      },
    );
  }

  /*
  Calculates the total work time with the current active filters
   */
  calculateTotalTime() {
    const filteredEntries = TotalTimeHelper.getFilteredEntries<InternalTimetableEntryDto>(this.table);
    this.totalTime = TotalTimeHelper.calculate(filteredEntries);

    this.cd.detectChanges();
  }
}
