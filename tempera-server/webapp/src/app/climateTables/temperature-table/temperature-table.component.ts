import {Component, OnInit} from '@angular/core';
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService, SelectItem} from "primeng/api";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";

type TimeUnit = "SECONDS" | "MINUTES" | "HOURS" | "DAYS" | "WEEKS" | "MONTHS" | "YEARS"
@Component({
  selector: 'app-temperature-table',
  standalone: true,
  imports: [
    DatePipe,
    NgIf,
    NgForOf,
    TableModule,
    CalendarModule,
    DropdownModule,
    FormsModule
  ],
  templateUrl: './temperature-table.component.html',
  styleUrl: './temperature-table.component.css'
})
export class TemperatureTableComponent implements OnInit{

  filterDate: Date | undefined;
  selectedInterval: any; // Assuming interval is a pre-defined set of options
  intervals: SelectItem[] | undefined; // For dropdown intervals

  temperatureData: ClimateMeasurementDto[] | undefined = [];
  filteredData: ClimateMeasurementDto[] = [];

  timeUnit: TimeUnit = "MINUTES";
  sensorType: Sensor.SensorTypeEnum = "TEMPERATURE";
  timeAmount: number = 1;
  accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  temperaStationId: string = "tempera_station_1";


  constructor(
    private climateDataControllerService: ClimateDataControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchTemperatureData();
  }

  private fetchTemperatureData(): void {
    this.climateDataControllerService.getMeasurementsBySensorType(this.accessPointUuid, this.temperaStationId, this.sensorType, this.timeUnit, this.timeAmount).subscribe({
      next: (data) => {
        console.log("temp",data);
        this.temperatureData = data.measurementDtos;
        this.filteredData = this.temperatureData!;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Data Fetch Failed', detail: 'Unable to fetch temperature data'});
      }
    });
  }
  onDateFilter() {
    this.filteredData = this.temperatureData!.filter(data =>
      new Date(data.timestamp!).toDateString() === this.filterDate!.toDateString());
  }

  onIntervalFilter() {

  }
}
