import { Component, OnInit } from '@angular/core';
import {
  ColleagueStateDto,
  GetTimetableDataResponse,
  ProjectDto,
  TimetableControllerService,
} from '../../api';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { DatePipe } from '@angular/common';
import { TagModule } from 'primeng/tag';
import { DisplayHelper } from '../_helpers/display-helper';
import { WrapFnPipe } from '../_pipes/wrap-fn.pipe';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { MultiSelectModule } from 'primeng/multiselect';
import StateEnum = ColleagueStateDto.StateEnum;

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
  ],
  templateUrl: './timetable.component.html',
  styleUrl: './timetable.component.css',
})
export class TimetableComponent implements OnInit {
  public timetableData?: GetTimetableDataResponse;
  public filterFields: string[] = [];

  public stateOptions: StateEnum[] = (Object.values(StateEnum) as StateEnum[]);

  protected readonly DisplayHelper = DisplayHelper;

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
}
