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
import { TotalTimeHelper } from '../_helpers/total-time-helper';
import { ChartModule} from 'primeng/chart';

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

  public totalTime: { hours: number, minutes: number } = { hours: 0, minutes: 0 };

  /*
  * The table is used for its filtering functionality
  * This reference to the PrimeNG table is used because its entries also reflect the correct order if the table is sorted and the available entries when filtered.
  */
  @ViewChild('table') table!: Table;

  data: any;

  options: any;

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

    this.data = {
      labels: ['A', 'B', 'C'],
      datasets: [
        {
          data: [540, 325, 702],
          backgroundColor: [documentStyle.getPropertyValue('--blue-500'), documentStyle.getPropertyValue('--yellow-500'), documentStyle.getPropertyValue('--green-500')],
          hoverBackgroundColor: [documentStyle.getPropertyValue('--blue-400'), documentStyle.getPropertyValue('--yellow-400'), documentStyle.getPropertyValue('--green-400')]
        }
      ]
    };

    this.options = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor
          }
        }
      }
    };
  }

  // todo: change this logic so it can be used for a graph

  /*
  Calculates the total work time with the current active filters
   */
  calculateTotalTime() {
    const filteredEntries = TotalTimeHelper.getFilteredEntries<InternalAccumulatedTimeDto>(this.table);

    this.totalTime = TotalTimeHelper.calculate(filteredEntries);
  }
}
