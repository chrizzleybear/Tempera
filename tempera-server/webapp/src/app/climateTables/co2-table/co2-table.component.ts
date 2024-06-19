import {Component, OnInit} from '@angular/core';
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";
import {DatePipe, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/calendar";
import {FormsModule} from "@angular/forms";

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

  sensorType: Sensor.SensorTypeEnum = "NMVOC";
  accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  temperaStationId: string = "tempera_station_1";
  numberOfDisplayedEntries: number = 10;
  rangeDates: Date[] = [];

  constructor(
    private climateDataControllerService: ClimateDataControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchCO2Data();
  }

  private fetchCO2Data(): void {
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
