import {Component, OnInit} from '@angular/core';
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";

type TimeUnit = "SECONDS" | "MINUTES" | "HOURS" | "DAYS" | "WEEKS" | "MONTHS" | "YEARS"
@Component({
  selector: 'app-temperature-table',
  standalone: true,
  imports: [],
  templateUrl: './temperature-table.component.html',
  styleUrl: './temperature-table.component.css'
})
export class TemperatureTableComponent implements OnInit{

  public temperatureData: ClimateMeasurementDto[] | undefined = [];
  public timeout: number = 0.5;  // chart refresh rate in minutes
  public timeUnit: TimeUnit = "MINUTES";
  public sensorType: Sensor.SensorTypeEnum = "TEMPERATURE";
  public timeAmount: number = 1;
  public accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  public temperaStationId: string = "tempera_station_1";


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
        this.temperatureData = data.measurementDtos;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Data Fetch Failed', detail: 'Unable to fetch temperature data'});
      }
    });
  }
}
