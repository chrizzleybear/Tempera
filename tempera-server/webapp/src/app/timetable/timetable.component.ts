import { Component, OnInit } from '@angular/core';
import {
  ColleagueStateDto,
  GetTimetableDataResponse,
  ProjectDto,
  TimetableControllerService,
} from '../../api';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { DatePipe } from '@angular/common';
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
  ],
  templateUrl: './timetable.component.html',
  styleUrl: './timetable.component.css',
})
export class TimetableComponent implements OnInit {
  public timetableData?: GetTimetableDataResponse;
  public filterFields: string[] = [];

  public stateOptions: StateEnum[] = (Object.values(StateEnum) as StateEnum[]);

  selectedRowIndex: number = 0;

  protected readonly DisplayHelper = DisplayHelper;

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

    const minStartTime = new Date(this.timetableData?.tableEntries?.[this.selectedRowIndex].startTimestamp!);
    const maxEndTime = new Date(this.timetableData?.tableEntries?.[this.selectedRowIndex].endTimestamp!);
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

  constructor(public timetableControllerService: TimetableControllerService) {
  }

  ngOnInit(): void {
    this.timetableControllerService.getTimetableData().subscribe({
        next: data => {
          this.timetableData = data;

          this.filterFields = Object.keys(this.timetableData?.tableEntries?.[0] ?? []);
        },
        error: err => {
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
        console.log('Updating project for time entry', timeEntryId, 'to', newProject);
      },
    });
  }


  onSplitFormSubmit() {
    const timeEntryId = this.timetableData?.tableEntries?.[this.selectedRowIndex].id!;
    const time = this.splitForm.controls.time.value?.toString()!;
    this.timetableControllerService.splitTimeRecord({ entryId: timeEntryId, splitTimestamp: time }).subscribe(
      {
        next: () => {
          console.log('Splitting time entry', timeEntryId, 'at', time);
        },
        error: () => {
          console.error('Failed to split time entry');
        },
      },
    );
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
