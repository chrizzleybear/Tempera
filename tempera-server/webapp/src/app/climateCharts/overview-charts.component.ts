import { Component, OnInit, ViewChild } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { DropdownModule } from 'primeng/dropdown';
import { ClimateDataControllerService } from '../../api';
import { TemperatureCo2ChartComponent } from './temperature-co2-chart/temperature-co2-chart.component';
import { HumidityIrradianceChartComponent } from './humidity-irradiance-chart/humidity-irradiance-chart.component';


@Component({
  selector: 'app-overview-charts',
  standalone: true,
  imports: [
    TemperatureCo2ChartComponent,
    HumidityIrradianceChartComponent,
    PanelModule,
    CalendarModule,
    FormsModule,
    DropdownModule,
  ],
  templateUrl: './overview-charts.component.html',
  styleUrl: './overview-charts.component.css',
})
export class OverviewChartsComponent implements OnInit {
  protected readonly String: StringConstructor = String;

  // Default chart window to 1h prior to how - now if not set by the user
  public rangeDates: Date[] = [new Date(Date.now() - (60 * 60 * 1_000)), new Date()];
  public accessPointUuid: string = '123e4567-e89b-12d3-a456-426614174001';
  public temperaStationId: string = 'tempera_station_1';
  public accessPoints: string[] = [];
  public temperaStations: string[] = [];

  @ViewChild(TemperatureCo2ChartComponent)
  private temperatureCo2ChartComponent: TemperatureCo2ChartComponent | undefined;

  @ViewChild(HumidityIrradianceChartComponent)
  private humidityIrradianceChartComponent: HumidityIrradianceChartComponent | undefined;

  constructor(private climateDataControllerService: ClimateDataControllerService, private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.getAccessPoints();
    this.getTemperaStations();
  }

  onDateChange(newDates: Date[]): void {
    console.log('rangeDates changed:', newDates);
    if (newDates[0] === null || newDates[1] === null) {
      this.messageService.add({
        severity: 'info',
        summary: 'Info',
        detail: 'Please select a start & end date and time.',
      });
      return;
    }

    let startDate = newDates[0].getTime();
    let endDate = newDates[1].getTime();
    if (endDate < startDate) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'The end date can\'t be before the start date.',
      });
      return;
    }
    this.updatePlots();
  }

  onAccessPointSelectionChange(): void {
    this.updatePlots();
  }

  onTemperaStationSelectionChange(): void {
    this.updatePlots();
  }

  updatePlots(): void {
    if (this.temperatureCo2ChartComponent !== undefined) {
      this.temperatureCo2ChartComponent.updateChart(['TEMPERATURE', 'NMVOC']);
    } else {
      console.log('TemperatureCo2ChartComponent is undefined');
    }
    if (this.humidityIrradianceChartComponent !== undefined) {
      this.humidityIrradianceChartComponent.updateChart(['HUMIDITY', 'IRRADIANCE']);
    } else {
      console.log('HumidityIrradianceChartComponent is undefined');
    }
  }

  getAccessPoints(): void {
    this.climateDataControllerService.getEnabledAccessPoints()
      .subscribe({
        next: accessPoints => {
          console.log('Found the following enabled access points: ' + accessPoints.map(ap => ap.id));
          for (let accessPoint of accessPoints) {
            this.accessPoints.push(accessPoint.id as string);
          }
        },
      });
  }

  getTemperaStations(): void {
    this.climateDataControllerService.getTemperaStations()
      .subscribe({
        next: temperaStations => {
          console.log('Found the following enabled tempera stations: ' + temperaStations.map(ts => ts.id));
          for (let temperaStation of temperaStations) {
            this.temperaStations.push(temperaStation.id as string);
          }
        },
      });
  }
}
