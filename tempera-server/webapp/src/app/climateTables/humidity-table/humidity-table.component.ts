import {Component, OnInit} from '@angular/core';
import {MessageService} from "primeng/api";
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {DatePipe, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";

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

  sensorType: Sensor.SensorTypeEnum = "HUMIDITY";
  accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  temperaStationId: string = "tempera_station_1";

  numberOfDisplayedEntries: number = 10;
  rangeDates: Date[] = [];

  constructor(
    private climateDataControllerService: ClimateDataControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchHumidityData();
  }

  private fetchHumidityData(): void {
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
