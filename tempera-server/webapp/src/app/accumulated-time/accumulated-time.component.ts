import { Component, OnInit, ViewChild } from '@angular/core';
import {
  AccumulatedTimeControllerService,
  AccumulatedTimeDto,
  SimpleGroupDto,
  SimpleProjectDto,
} from '../../api';
import { Table, TableModule } from 'primeng/table';
import { DropdownModule } from 'primeng/dropdown';
import { CardModule } from 'primeng/card';
import { TotalTimeHelper, TotalTimeWithStates } from '../_helpers/total-time-helper';
import { ChartModule} from 'primeng/chart';
import { Chart } from 'chart.js';

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
  ],
  templateUrl: './accumulated-time.component.html',
  styleUrl: './accumulated-time.component.css',
})
export class AccumulatedTimeComponent implements OnInit {
  public accumulatedTimes: InternalAccumulatedTimeDto[] = [];
  public availableProjects: SimpleProjectDto[] = [];
  public availableGroups: SimpleGroupDto[] = [];

  public stateTimes: TotalTimeWithStates = {
    AVAILABLE: 0,
    MEETING: 0,
    DEEPWORK: 0,
    OUT_OF_OFFICE: 0,
  }

  public totalTime: number = 0;

  /*
  * The table is used for its filtering functionality
  * This reference to the PrimeNG table is used because its entries also reflect the correct order if the table is sorted and the available entries when filtered.
  */
  @ViewChild('table') table!: Table;

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
          this.availableProjects = response.availableProjects ?? [];
          this.availableGroups = response.availableGroups ?? [];
        },
        error: error => {
          console.error('Error while fetching accumulated time data', error);
        },
      },
    );

    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');


    this.chart = new Chart("MyChart", {
      type: 'pie', //this denotes tha type of chart

      data: {
        labels: ['Available', 'Deep Work', 'Meeting'],
        datasets: [
          {
            data: [0, 0, 0],
            backgroundColor: ['#22c55e', '#3b82f6', '#f59e0b'],
            hoverBackgroundColor: ['#52cc7f', '#7396ea', '#efae5c']
          }
        ]
      },
      options: {
        aspectRatio:2.5,
        plugins: {
          legend: {
            labels: {
              usePointStyle: true,
              color: textColor
            }
          }
        }
      }
    });
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
