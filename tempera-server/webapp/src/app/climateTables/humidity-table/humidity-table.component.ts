import {Component, Input, OnInit} from '@angular/core';
import {MessageService} from "primeng/api";
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {DatePipe, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {PanelModule} from "primeng/panel";

@Component({
  selector: 'app-humidity-table',
  standalone: true,
  imports: [
    DatePipe,
    TableModule,
    NgIf,
    CalendarModule,
    DropdownModule,
    FormsModule,
    PanelModule
  ],
  templateUrl: './humidity-table.component.html',
  styleUrl: './humidity-table.component.css'
})
export class HumidityTableComponent implements OnInit{

  humidityData: ClimateMeasurementDto[] | undefined = [];
  sensorType: Sensor.SensorTypeEnum = "HUMIDITY";
  @Input() temperaStationId: string | undefined;
  @Input() accessPointUuid: string | undefined;

  numberOfDisplayedEntries: number = 10;
  rangeDates: Date[] = [];

  constructor(
    private climateDataControllerService: ClimateDataControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchDate();
  }

  fetchHumidityData(startDate: Date, endDate: Date): void {
    this.climateDataControllerService.getMeasurementsBySensorType(
      this.accessPointUuid!,
      this.temperaStationId!,
      this.sensorType,
      startDate.toISOString(),
      endDate.toISOString(),
      this.numberOfDisplayedEntries
    ).subscribe({
      next: (data) => {
        if(data === null) {
          this.humidityData = [];
          return;
        }
        this.humidityData = data.measurementDtos;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Data Fetch Failed', detail: 'Unable to fetch humidity data: ' + error});
      }
    });
  }

  onDatesSelected() {
    // check if both dates are selected
    if (this.rangeDates.length === 2) {
      console.log(this.rangeDates);
      this.fetchHumidityData(this.rangeDates[0], this.rangeDates[1]);
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
    this.fetchHumidityData(this.rangeDates[0], this.rangeDates[1]);
  }

  protected readonly String = String;
}
