import { Component, OnInit, ViewChild } from '@angular/core';
import {
  ColleagueStateDto,
  GetTimetableDataResponse,
  ProjectDto,
  TimetableControllerService, TimetableEntryDto,
} from '../../api';
import { Table, TableLazyLoadEvent, TableModule } from 'primeng/table';
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
  ],
  templateUrl: './timetable.component.html',
  styleUrl: './timetable.component.css',
})
export class TimetableComponent implements OnInit {
  public timetableData?: GetTimetableDataResponse;

  public filterFields: string[] = [];

  public stateOptions: StateEnum[] = (Object.values(StateEnum) as StateEnum[]);

  public selectedRowIndex: number = 0;

  protected readonly DisplayHelper = DisplayHelper;

  public totalTime: { hours: number, minutes: number } = { hours: 0, minutes: 0 };


  /*
  * This reference to the PrimeNG table is used because its entries also reflect the correct order if the table is sorted and the available entries when filtered.
   */
  @ViewChild('table') table!: Table;

  /*
    * Validates that the time is between the start and end time of the time entry.
    * Only hours and minutes are considered since it's assumed that time entries are on the same day.
    * Time entry is selected by the selectedRowIndex.
    * Also note that at the moment the control is edited inside the validator to set the day to the same as the time entry.
   */
  inPermittedTimeRangeValidator: ValidatorFn = (control: AbstractControl<Date | undefined>): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }

    const entries = this.table.value as TimetableEntryDto[];

    const minStartTime = new Date(entries[this.selectedRowIndex].startTimestamp!);
    const maxEndTime = new Date(entries[this.selectedRowIndex].endTimestamp!);
    control.value?.setDate(minStartTime.getDate());
    control.value?.setMilliseconds(0);

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

  constructor(public timetableControllerService: TimetableControllerService, private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.timetableControllerService.getTimetableData().subscribe({
        next: data => {
          this.timetableData = data;

          this.filterFields = Object.keys(this.timetableData?.tableEntries?.[0] ?? []);

          this.calculateTotalTime();
        },
        error: err => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load timetable data' });
          console.error(err);
        },
      },
    );
  }

  updateProject(newProject: ProjectDto, timeEntryId: number) {
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


  onSplitFormSubmit() {
    console.log(this.selectedRowIndex);
    const entries = this.table.value as TimetableEntryDto[];
    const timeEntryId = entries[this.selectedRowIndex].id!;
    const time = this.splitForm.controls.time.value?.toString()!;
    this.timetableControllerService.splitTimeRecord({ entryId: timeEntryId, splitTimestamp: time }).subscribe(
      {
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Time entry split successfully' });
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to split time entry' });
          console.error('Failed to split time entry');
        },
      },
    );
  }

  /*
  Calculates the total work time with the current active filters
   */
  calculateTotalTime() {
    let totalTimeTemp: number = 0;
    let tempEntries: TimetableEntryDto[];
    if ((this.table as any)?.filteredValue?.length > 0) {
      tempEntries = this.table.filteredValue as TimetableEntryDto[];
    } else {
      tempEntries = this.table.value as TimetableEntryDto[];
    }

    tempEntries.forEach(entry => {
      totalTimeTemp += new Date(entry.endTimestamp!).getTime() - new Date(entry.startTimestamp!).getTime();
    });
    const totalDate = new Date(totalTimeTemp);
    this.totalTime = { hours: totalDate.getHours(), minutes: totalDate.getMinutes() };
  }

  // method to call onLazyLoad when enabled

  // loadEntries($event: TableLazyLoadEvent) {
  //   let state = ($event.filters?.['state'] as FilterMetadata)?.value as string | undefined;
  //   let projectId = ($event.filters?.['assignedProject.id'] as FilterMetadata)?.value as string | undefined;
  //   console.log({ state: state, projectId: projectId });
  //
  //   // call the API with the filter and sorting values
  // }
}
