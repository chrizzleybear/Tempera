import {Component, OnInit} from '@angular/core';
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";
import {TableModule} from "primeng/table";
import {DatePipe, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {CalendarModule} from "primeng/calendar";
import {PanelModule} from "primeng/panel";

@Component({
  selector: 'app-irr-table',
  standalone: true,
  imports: [
    TableModule,
    DatePipe,
    FormsModule,
    CalendarModule,
    NgIf,
    PanelModule
  ],
  templateUrl: './irr-table.component.html',
  styleUrl: './irr-table.component.css'
})
export class IrrTableComponent implements OnInit{

  irrData: ClimateMeasurementDto[] | undefined = [];
  sensorType: Sensor.SensorTypeEnum = "IRRADIANCE";
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

  fetchIrrData(startDate: Date, endDate: Date): void {
    this.climateDataControllerService.getMeasurementsBySensorType(
      this.accessPointUuid,
      this.temperaStationId,
      this.sensorType,
      startDate.toISOString(),
      endDate.toISOString(),
      this.numberOfDisplayedEntries
    ).subscribe({
      next: (data) => {
        this.irrData = data.measurementDtos;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Data Fetch Failed', detail: 'Unable to fetch temperature data'});
      }
    });
  }

  onDatesSelected() {
    // check if both dates are selected
    if (this.rangeDates.length === 2) {
      console.log(this.rangeDates);
      this.fetchIrrData(this.rangeDates[0], this.rangeDates[1]);
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
    this.fetchIrrData(this.rangeDates[0], this.rangeDates[1]);
  }

  protected readonly String = String;
}
