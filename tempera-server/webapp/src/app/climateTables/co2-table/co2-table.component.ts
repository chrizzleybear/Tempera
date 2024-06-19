import {Component, OnInit} from '@angular/core';
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";
import {DatePipe, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/calendar";
import {FormsModule} from "@angular/forms";
import {PanelModule} from "primeng/panel";

@Component({
  selector: 'app-co2-table',
  standalone: true,
  imports: [
    DatePipe,
    TableModule,
    NgIf,
    CalendarModule,
    FormsModule,
    PanelModule
  ],
  templateUrl: './co2-table.component.html',
  styleUrl: './co2-table.component.css'
})
export class Co2TableComponent implements OnInit{

  co2Data: ClimateMeasurementDto[] | undefined = [];
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
    this.fetchDate();
  }

  fetchCo2Data(startDate: Date, endDate: Date): void {
    this.climateDataControllerService.getMeasurementsBySensorType(
      this.accessPointUuid,
      this.temperaStationId,
      this.sensorType,
      startDate.toISOString(),
      endDate.toISOString(),
      this.numberOfDisplayedEntries
    ).subscribe({
      next: (data) => {
        if(data === null) {
          this.co2Data = [];
          return;
        }
        this.co2Data = data.measurementDtos;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Data Fetch Failed', detail: 'Unable to fetch CO2 data: : ' + error});
      }
    });
  }

  onDatesSelected() {
    // check if both dates are selected
    if (this.rangeDates.length === 2) {
      this.fetchCo2Data(this.rangeDates[0], this.rangeDates[1]);
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
    this.fetchCo2Data(this.rangeDates[0], this.rangeDates[1]);
  }

  protected readonly String = String;
}
