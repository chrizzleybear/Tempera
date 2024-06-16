import { Component, OnInit, ViewChild } from '@angular/core';
import {
  AccumulatedTimeControllerService,
  AccumulatedTimeDto, ColleagueStateDto,
  SimpleGroupDto,
  SimpleProjectDto,
} from '../../api';
import { Table, TableModule } from 'primeng/table';
import { DropdownModule } from 'primeng/dropdown';
import { CardModule } from 'primeng/card';
import { TotalTimeHelper, TotalTimeWithStates } from '../_helpers/total-time-helper';
import { ChartModule } from 'primeng/chart';
import { Chart } from 'chart.js';
import { TagModule } from 'primeng/tag';
import StateEnum = ColleagueStateDto.StateEnum;
import { DisplayHelper } from '../_helpers/display-helper';
import { WrapFnPipe } from '../_pipes/wrap-fn.pipe';
import { FilterMatchMode } from 'primeng/api';
import { MultiSelect, MultiSelectModule } from 'primeng/multiselect';
import { ButtonModule } from 'primeng/button';
import { NgIf } from '@angular/common';

interface InternalAccumulatedTimeDto extends AccumulatedTimeDto {
  startTime: Date;
  endTime: Date;
}

@Component({
  selector: 'app-manager-time-overview',
  standalone: true,
  imports: [
    TableModule,
    DropdownModule,
    CardModule,
    ChartModule,
    TagModule,
    WrapFnPipe,
    ButtonModule,
    MultiSelectModule,
    NgIf,
  ],
  templateUrl: './accumulated-time.component.html',
  styleUrl: './accumulated-time.component.css',
})
export class AccumulatedTimeComponent implements OnInit {
  public accumulatedTimes: InternalAccumulatedTimeDto[] = [];
  public allProjects: SimpleProjectDto[] = [];
  public allGroups: SimpleGroupDto[] = [];
  public activeProjects: SimpleProjectDto[] = [];
  public activeGroups: SimpleGroupDto[] = [];
  public activeProjectIds: string[] = [];
  public activeGroupIds: string[] = [];
  public allProjectIds: string[] = [];
  public allGroupIds: string[] = [];
  public availableProjects: SimpleProjectDto[] = [];
  public availableGroups: SimpleGroupDto[] = [];
  public onlyActiveProjectsAndGroupsShown: boolean = false;
  public stateTimes: TotalTimeWithStates = {
    AVAILABLE: 0,
    MEETING: 0,
    DEEPWORK: 0,
    OUT_OF_OFFICE: 0,
  };

  public totalTime: number = 0;

  protected readonly StateEnum = StateEnum;
  protected readonly DisplayHelper = DisplayHelper;

  /*
  * The table is used for its filtering functionality
  * This reference to the PrimeNG table is used because its entries also reflect the correct order if the table is sorted and the available entries when filtered.
  */
  @ViewChild('table') table!: Table;
  // @ViewChild('projectFilter') projectFilterOverlay!: MultiSelect;


  chart: any;

  constructor(private accumulatedTimeControllerService: AccumulatedTimeControllerService) {
  }

  ngOnInit(): void {
    this.accumulatedTimeControllerService.getAccumulatedTimeData().subscribe(
      {
        next: response => {
          this.accumulatedTimes = response.accumulatedTimes?.map(entry => ({
              ...entry,
              startTime: new Date(entry.startTimestamp),
              endTime: new Date(entry.endTimestamp),
            }),
          ) ?? [];
          // need to seperate between active and all projects and groups
          // available Projects changes between active and all projects when the filter is applied
          this.allProjects = response.availableProjects;
          this.allGroups = response.availableGroups;

          this.activeProjects = this.allProjects.filter(project => project.isActive);
          this.activeGroups = this.allGroups.filter(group => group.isActive);


          this.availableProjects = this.allProjects;
          this.availableGroups = this.allGroups;

        },
        error: error => {
          console.error('Error while fetching accumulated time data', error);
        },
      },
    );

    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');


    this.chart = new Chart('MyChart', {
      type: 'pie', //this denotes tha type of chart

      data: {
        labels: ['Available', 'Deep Work', 'Meeting'],
        datasets: [
          {
            data: [0, 0, 0],
            backgroundColor: ['#22c55e', '#3b82f6', '#f59e0b'],
            hoverBackgroundColor: ['#52cc7f', '#7396ea', '#efae5c'],
          },
        ],
      },
      options: {
        aspectRatio: 2.5,
        plugins: {
          legend: {
            labels: {
              usePointStyle: true,
              color: textColor,
            },
          },
        },
      },
    });
  }

  /*
* Filters the table-data so only active projects & groups are being
* taken into account. If the array that the filter compares against the filter has
* problems, thats why the if-clauses are there.
 */
  filterActiveProjects() {
    if (this.activeProjects.length === 0) {
      this.table?.filter([''], 'projectId', FilterMatchMode.IN);
    } else {
      this.table?.filter(this.activeProjects.map(p => p.projectId), 'projectId', FilterMatchMode.IN);
    }
    if (this.activeGroups.length === 0) {
      this.table?.filter([''], 'groupId', FilterMatchMode.IN);
    } else {
      this.table?.filter(this.activeGroups.map(g => g.id), 'groupId', FilterMatchMode.IN);
    }
    this.availableProjects = this.activeProjects;
    this.availableGroups = this.activeGroups;
    this.onlyActiveProjectsAndGroupsShown = true;
  }

  /*
* Removes the filter condition for the table-data so all projects & groups are being
* taken into account. If the array that the filter compares against the filter has
* problems, thats why the if-clause is there.
*/
  removeOnlyActiveProjectsFilter() {
    if (this.allProjects.length === 0) {
      this.table?.filter([''], 'projectId', FilterMatchMode.IN);
    } else {
      this.table?.filter(this.allProjects.map(p => p.projectId), 'projectId', FilterMatchMode.IN);
    }
    if (this.allGroups.length === 0) {
      this.table?.filter([''], 'groupId', FilterMatchMode.IN);
    } else {
      this.table?.filter(this.allGroups.map(g => g.id), 'groupId', FilterMatchMode.IN);
    }
    // this.table?.reset();
    this.availableProjects = this.allProjects;
    this.availableGroups = this.allGroups;
    this.onlyActiveProjectsAndGroupsShown = false;
  }

  /*
  Calculates the total work time with the current active filters
   */
  calculateTotalTime() {
    const filteredEntries = TotalTimeHelper.getFilteredEntries<InternalAccumulatedTimeDto>(this.table);

    this.stateTimes = TotalTimeHelper.calculateWithState(filteredEntries);
    this.totalTime = this.stateTimes.AVAILABLE + this.stateTimes.DEEPWORK + this.stateTimes.MEETING;

    this.chart.data.datasets[0].data = [];

    this.chart.data.datasets[0].data.push(this.stateTimes.AVAILABLE);
    this.chart.data.datasets[0].data.push(this.stateTimes.DEEPWORK);
    this.chart.data.datasets[0].data.push(this.stateTimes.MEETING);

    this.chart.update();
  }
}
