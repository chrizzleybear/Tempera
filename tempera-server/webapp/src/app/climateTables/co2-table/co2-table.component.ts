import {Component, OnInit} from '@angular/core';
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";
import {DatePipe, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/calendar";
import {FormsModule} from "@angular/forms";

type TimeUnit = "SECONDS" | "MINUTES" | "HOURS" | "DAYS" | "WEEKS" | "MONTHS" | "YEARS";
@Component({
  selector: 'app-co2-table',
  standalone: true,
  imports: [
    DatePipe,
    TableModule,
    NgIf,
    CalendarModule,
    FormsModule
  ],
  templateUrl: './co2-table.component.html',
  styleUrl: './co2-table.component.css'
})
export class Co2TableComponent implements OnInit{

  filterDate: Date | undefined;
  co2Data: ClimateMeasurementDto[] | undefined = [];
  filteredData: ClimateMeasurementDto[] = [];

  timeUnit: TimeUnit = "MINUTES";
  sensorType: Sensor.SensorTypeEnum = "NMVOC";
  timeAmount: number = 1;
  accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  temperaStationId: string = "tempera_station_1";

  constructor(
    private climateDataControllerService: ClimateDataControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchCO2Data();
  }

  private fetchCO2Data(): void {
    this.climateDataControllerService.getMeasurementsBySensorType(this.accessPointUuid, this.temperaStationId, this.sensorType, this.timeUnit, this.timeAmount).subscribe({
      next: (data) => {
        this.co2Data = data.measurementDtos;
        this.filteredData = this.co2Data!;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Data Fetch Failed', detail: 'Unable to fetch CO2 data'});
      }
    });
  }

  onDateFilter() {
    if (this.filterDate && this.co2Data) {
      this.filteredData = this.co2Data.filter(data =>
        new Date(data.timestamp!).toDateString() === this.filterDate!.toDateString());
    }
  }
}
