import { Component } from '@angular/core';
import { TemperatureCo2ChartComponent } from './temperature-co2-chart/temperature-co2-chart.component';
import { PanelModule } from 'primeng/panel';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
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
  ],
  templateUrl: './overview-charts.component.html',
  styleUrl: './overview-charts.component.css',
})
export class OverviewChartsComponent {
  protected readonly String: StringConstructor = String;

  // Default chart window to 1h prior to how - now if not set by the user
  public rangeDates: Date[] = [new Date(Date.now() - (60 * 60 * 1_000)), new Date()];
  public accessPointUuid: string = '123e4567-e89b-12d3-a456-426614174001';
  public temperaStationId: string = 'tempera_station_1';

  constructor(private messageService: MessageService) {
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
    }
  }
}
