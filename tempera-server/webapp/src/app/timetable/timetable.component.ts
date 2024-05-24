import { Component, OnInit, ViewChild } from '@angular/core';
import {
  ColleagueStateDto,
  SimpleProjectDto,
  TimetableControllerService, TimetableEntryDto,
} from '../../api';
import { Table, TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { DatePipe, NgIf } from '@angular/common';
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
  ],
  templateUrl: './timetable.component.html',
  styleUrl: './timetable.component.css',
})
export class TimetableComponent implements OnInit {
  public tableEntries?: InternalTimetableEntryDto[];

  public availableProjects: SimpleProjectDto[] = [];

  public filterFields: string[] = [];

  public stateOptions: StateEnum[] = (Object.values(StateEnum));

  public selectedRowIndex: number = 0;

  protected readonly DisplayHelper = DisplayHelper;

  public totalTime: { hours: number, minutes: number } = { hours: 0, minutes: 0 };

  public editDescriptionVisible: boolean = false;

  public splitVisible: boolean = false;

  protected readonly Date = Date;


  /*
  * This reference to the PrimeNG table is used because its entries also reflect the correct order if the table is sorted and the available entries when filtered.
   */
  @ViewChild('table') table!: Table;

  /*
    * Validates that the time is between the start and end time of the time entry.
    * Time entry is selected by the selectedRowIndex.
   */
  inPermittedTimeRangeValidator: ValidatorFn = (control: AbstractControl<Date | undefined>): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }

    const entries = this.getFilteredEntries();

    const minStartTime = entries[this.selectedRowIndex].startTime;
    const maxEndTime = entries[this.selectedRowIndex].endTime;

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

  constructor(public timetableControllerService: TimetableControllerService, private messageService: MessageService) {
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

  onOpenSplitForm(rowIndex: number) {
    this.selectedRowIndex = rowIndex;
    this.splitForm.controls.time.setValue(this.getFilteredEntries()[rowIndex].startTime);
    this.splitVisible = true;
  }

  onSplitFormSubmit() {
    const entries = this.getFilteredEntries();
    const timeEntryId = entries[this.selectedRowIndex].id;

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

  onEditDescription(rowIndex: number) {
    this.editDescriptionVisible = true;
    this.selectedRowIndex = rowIndex;
    this.descriptionForm.controls.description.setValue(this.getFilteredEntries()[rowIndex].description);
  }

  onDescriptionFormSubmit() {
    const entries = this.getFilteredEntries();
    const timeEntryId = entries[this.selectedRowIndex].id;
    const description = this.descriptionForm.controls.description.value!;
    this.timetableControllerService.updateDescription({ entryId: timeEntryId, description }).subscribe(
      {
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Description updated successfully',
          });
          this.tableEntries![this.selectedRowIndex].description = description;
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
    let totalTimeTemp: number = 0;

    const tempEntries = this.getFilteredEntries()

    tempEntries.forEach(entry => {
      totalTimeTemp += entry.endTime.getTime() - entry.startTime.getTime();
    });
    const hours = Math.floor(totalTimeTemp / 3600000);

    const remainingTime = totalTimeTemp % 3600000;

    const minutes = Math.floor(remainingTime / 60000);
    this.totalTime = { hours: hours, minutes: minutes };
  }

  /*
  * Returns the entries that are currently displayed in the table, depending on the active filters.
   */
  getFilteredEntries(): InternalTimetableEntryDto[] {
    const filters = this.table?.filters as any;
    if (filters['startTime']?.value || filters['endTime']?.value || filters['state']?.value || filters['assignedProject.projectId']?.value || filters['description']?.value) {
      return this.table.filteredValue as InternalTimetableEntryDto[];
    } else {
      return  this.table.value as InternalTimetableEntryDto[];
    }
  }
}
