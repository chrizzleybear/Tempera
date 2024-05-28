import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import {
  ColleagueStateDto,
  SimpleProjectDto,
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

interface InternalTimetableEntryDto extends TimetableEntryDto {
  startTime: Date;
  endTime: Date;
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

  public availableProjects: SimpleProjectDto[] = [];

  public filterFields: string[] = [];

  public stateOptions: StateEnum[] = (Object.values(StateEnum));

  public selectedEntry?: InternalTimetableEntryDto;

  protected readonly DisplayHelper = DisplayHelper;

  public totalTime: { hours: number, minutes: number } = { hours: 0, minutes: 0 };

  public editDescriptionVisible: boolean = false;

  public splitVisible: boolean = false;
  public calendarVisible: boolean = false;

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
          })) ?? [];

          this.availableProjects = data.availableProjects ?? [];

          this.filterFields = Object.keys(this.tableEntries?.[0] ?? []);
        },
        error: err => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load timetable data' });
          console.error(err);
        },
      },
    );
  }

  updateProject(newProject: SimpleProjectDto, timeEntryId: number) {
    this.timetableControllerService.updateProject1({
      entryId: timeEntryId,
      project: newProject,
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
          })) ?? [];

          this.availableProjects = data.availableProjects ?? [];
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
