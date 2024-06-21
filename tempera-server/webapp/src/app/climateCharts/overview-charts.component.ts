import { Component, OnInit, ViewChild } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { DropdownModule } from 'primeng/dropdown';
import { TemperatureCo2ChartComponent } from './temperature-co2-chart/temperature-co2-chart.component';
import { HumidityIrradianceChartComponent } from './humidity-irradiance-chart/humidity-irradiance-chart.component';
import { InputNumberModule } from 'primeng/inputnumber';
import { ChartModule } from 'primeng/chart';
import { NgIf } from '@angular/common';
import { TemperaStationService } from '../_services/tempera-station.service';
import { User } from '../models/user.model';
import { StorageService } from '../_services/storage.service';


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
    InputNumberModule,
    ChartModule,
    NgIf,
  ],
  templateUrl: './overview-charts.component.html',
  styleUrl: './overview-charts.component.css',
})
export class OverviewChartsComponent implements OnInit {
  protected readonly String: StringConstructor = String;

  // Default chart window to 1h prior to how - now if not set by the user
  public rangeDates: Date[] = [new Date(Date.now() - (60 * 60 * 1_000)), new Date()];
  public temperaStationId: string | undefined;
  public accessPointUuid: string | undefined;
  public numberOfDisplayedEntries: number = 10;
  public currentUser: User | undefined;

  @ViewChild(TemperatureCo2ChartComponent)
  private temperatureCo2ChartComponent: TemperatureCo2ChartComponent | undefined;

  @ViewChild(HumidityIrradianceChartComponent)
  private humidityIrradianceChartComponent: HumidityIrradianceChartComponent | undefined;

  constructor(
    private messageService: MessageService,
    private temperaStationService: TemperaStationService,
    private storageService: StorageService) {
    this.currentUser = this.storageService.getUser();
    if (!this.currentUser) {
      messageService.add({ severity: 'error', summary: 'Error', detail: 'No user logged in' });
    }
  }

  async ngOnInit() {
    await this.fetchTemperaAndAccessPoint();
    if (this.humidityIrradianceChartComponent === undefined || this.temperatureCo2ChartComponent === undefined) {
      console.log('lsdjfsdalkfj');
    }
    await new Promise(r => setTimeout(r, 150));
    this.humidityIrradianceChartComponent?.whenInit();
    this.temperatureCo2ChartComponent?.whenInit();
  }

  onDateChange(newDates: Date[]): void {
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
    } else if (startDate == endDate) {
      this.messageService.add({
        severity: 'info',
        summary: 'Info',
        detail: 'Please select an end date & time that is different from the start date & time',
      });
      return;
    }
    // ugly fix, but without this, the chart component's values are set after completing this method,
    // and therefore updatePlots() is called still with the old values => app lags behind user input
    if (this.temperatureCo2ChartComponent === undefined || this.humidityIrradianceChartComponent === undefined) {
      console.log('Chart child components are undefined.');
      return;
    }
    this.temperatureCo2ChartComponent.rangeDates = newDates;
    this.humidityIrradianceChartComponent.rangeDates = newDates;
    this.updatePlots();
  }

  async fetchTemperaAndAccessPoint(): Promise<void> {
    this.temperaStationService.getAllTemperaStations().subscribe({
      next: (data) => {
        let temperaStation = data.find((temperaStation) => temperaStation.user === this.currentUser?.id);
        if (temperaStation === undefined) {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'No tempera station found for the current user',
          });
          console.error('No tempera station found for the current user');
          return;
        }
        this.temperaStationId = temperaStation?.id;
        this.accessPointUuid = temperaStation.accessPointId;
        if (this.temperaStationId === undefined || this.accessPointUuid === undefined) {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'No tempera station found for the current user',
          });
          console.error('No tempera station found for the current user');
        }
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  onDataPointsChange(numberOfPointsToDisplay: any) {
    if (this.temperatureCo2ChartComponent === undefined || this.humidityIrradianceChartComponent === undefined) {
      console.log('Chart child components are undefined.');
      return;
    }
    if (numberOfPointsToDisplay === 0) {
      return;
    }
    this.temperatureCo2ChartComponent.numberOfDisplayedEntries = numberOfPointsToDisplay;
    this.humidityIrradianceChartComponent.numberOfDisplayedEntries = numberOfPointsToDisplay;
    this.updatePlots();
  }

  updatePlots(): void {
    if (this.temperatureCo2ChartComponent !== undefined) {
      this.temperatureCo2ChartComponent.whenInit();
    } else {
      console.log('TemperatureCo2ChartComponent is undefined');
    }
    if (this.humidityIrradianceChartComponent !== undefined) {
      this.humidityIrradianceChartComponent.whenInit();
    } else {
      console.log('HumidityIrradianceChartComponent is undefined');
    }
  }
}
