import {Component, OnInit} from '@angular/core';
import {MessageService, SelectItem} from "primeng/api";
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {DatePipe, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";

type TimeUnit = "SECONDS" | "MINUTES" | "HOURS" | "DAYS" | "WEEKS" | "MONTHS" | "YEARS";
@Component({
  selector: 'app-humidity-table',
  standalone: true,
  imports: [
    DatePipe,
    TableModule,
    NgIf,
    CalendarModule,
    DropdownModule,
    FormsModule
  ],
  templateUrl: './humidity-table.component.html',
  styleUrl: './humidity-table.component.css'
})
export class HumidityTableComponent implements OnInit{

  filterDate: Date | undefined;

  humidityData: ClimateMeasurementDto[] | undefined = [];
  filteredData: ClimateMeasurementDto[] = [];

  timeUnit: TimeUnit = "MINUTES";
  sensorType: Sensor.SensorTypeEnum = "HUMIDITY";
  timeAmount: number = 1;
  accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  temperaStationId: string = "tempera_station_1";

  constructor(
    private climateDataControllerService: ClimateDataControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchHumidityData();
  }

  private fetchHumidityData(): void {
    this.climateDataControllerService.getMeasurementsBySensorType(this.accessPointUuid, this.temperaStationId, this.sensorType, this.timeUnit, this.timeAmount).subscribe({
      next: (data) => {
        console.log("hpafebi",data);
        this.humidityData = data.measurementDtos;
        this.filteredData = this.humidityData!;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Data Fetch Failed', detail: 'Unable to fetch humidity data'});
      }
    });
  }
  onDateFilter() {
    this.filteredData = this.humidityData!.filter(data =>
      new Date(data.timestamp!).toDateString() === this.filterDate!.toDateString());
  }
}
