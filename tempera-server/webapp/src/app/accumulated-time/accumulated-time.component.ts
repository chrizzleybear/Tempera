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
  }

  // todo: change this logic so it can be used for a graph

  /*
  Calculates the total work time with the current active filters
   */
  calculateTotalTime() {
    let totalTimeTemp: number = 0;
    let tempEntries: InternalAccumulatedTimeDto[];
    const filters = this.table?.filters as any;

    const hasActiveFilter = Object.keys(filters).some(key => filters[key]?.value);

    if (this.table.filteredValue != null && hasActiveFilter) {
      tempEntries = this.table?.filteredValue ?? [] as InternalAccumulatedTimeDto[];
    } else {
      tempEntries = this.table?.value ?? [] as InternalAccumulatedTimeDto[];
    }

    tempEntries.forEach(entry => {
      totalTimeTemp += entry.endTime.getTime() - entry.startTime.getTime();
    });
    const hours = Math.floor(totalTimeTemp / 3600000);

    const remainingTime = totalTimeTemp % 3600000;

    const minutes = Math.floor(remainingTime / 60000);
    this.totalTime = { hours: hours, minutes: minutes };
  }
}
