import {Component, OnInit} from '@angular/core';
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";

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

  temperatureData: ClimateMeasurementDto[] | undefined = [];
  filteredData: ClimateMeasurementDto[] = [];

  sensorType: Sensor.SensorTypeEnum = "TEMPERATURE";
  accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  temperaStationId: string = "tempera_station_1";
  numberOfDisplayedEntries: number = 10;
  rangeDates: Date[] = [];


  constructor(
    private climateDataControllerService: ClimateDataControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchTemperatureData();
  }

  private fetchTemperatureData(): void {
    this.fetchDate();
    let startDateTime: Date = this.rangeDates[0];
    let endDateTime: Date = this.rangeDates[1];
    this.climateDataControllerService.getMeasurementsBySensorType(
      this.accessPointUuid,
      this.temperaStationId,
      this.sensorType,
      startDateTime.toISOString(),
      endDateTime.toISOString(),
      this.numberOfDisplayedEntries
    ).subscribe({
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

  private fetchDate() {
    let today = new Date();
    today.setDate(today.getDate());
    today.setHours(8);
    today.setMinutes(0);
    today.setSeconds(0);
    this.rangeDates[0] = today;
    let end = new Date();
    end.setDate(today.getDate());
    end.setHours(20);
    end.setMinutes(0);
    end.setSeconds(0);
    this.rangeDates[1] = end;
  }
}
