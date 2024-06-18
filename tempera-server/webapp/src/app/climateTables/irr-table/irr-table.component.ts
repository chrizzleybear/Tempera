import {Component, OnInit} from '@angular/core';
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";
import {TableModule} from "primeng/table";
import {DatePipe, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {CalendarModule} from "primeng/calendar";

type TimeUnit = "SECONDS" | "MINUTES" | "HOURS" | "DAYS" | "WEEKS" | "MONTHS" | "YEARS";
@Component({
  selector: 'app-irr-table',
  standalone: true,
  imports: [
    TableModule,
    DatePipe,
    FormsModule,
    CalendarModule,
    NgIf
  ],
  templateUrl: './irr-table.component.html',
  styleUrl: './irr-table.component.css'
})
export class IrrTableComponent implements OnInit{

  filterDate: Date | undefined;
  irrData: ClimateMeasurementDto[] | undefined = [];
  filteredData: ClimateMeasurementDto[] = [];

  timeUnit: TimeUnit = "MINUTES";
  sensorType: Sensor.SensorTypeEnum = "IRRADIANCE";
  timeAmount: number = 1;
  accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  temperaStationId: string = "tempera_station_1";

  constructor(
    private climateDataControllerService: ClimateDataControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchIrrData();
  }

  private fetchIrrData(): void {
    this.climateDataControllerService.getMeasurementsBySensorType(this.accessPointUuid, this.temperaStationId, this.sensorType, this.timeUnit, this.timeAmount).subscribe({
      next: (data) => {
        this.irrData = data.measurementDtos;
        this.filteredData = this.irrData!;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Data Fetch Failed', detail: 'Unable to fetch Irradiance data'});
      }
    });
  }

  onDateFilter() {
    if (this.filterDate && this.irrData) {
      this.filteredData = this.irrData.filter(data =>
        new Date(data.timestamp!).toDateString() === this.filterDate!.toDateString());
    }
  }

}
