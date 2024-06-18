import {ClimateDataControllerService, ClimateMeasurementDto} from '../../api';
import {MessageService} from 'primeng/api';
import {Sensor} from '../../api/api/sensor';
import {Injectable, OnDestroy, OnInit} from '@angular/core';


@Injectable()
export abstract class ClimateChart implements OnInit, OnDestroy {
  public accessPointUuid: string = '';
  public temperaStationId: string = '';
  public rangeDates: Date[] = [];
  public numberOfDisplayedEntries: number = 10;

  protected sensorTypes: Sensor.SensorTypeEnum[] = [];
  protected color1: string = '';
  protected color2: string = '';
  protected label1: string = '';
  protected label2: string = '';
  protected data1: ClimateMeasurementDto[] | undefined = [];
  protected data2: ClimateMeasurementDto[] | undefined = [];

  private intervalId: any;
  public chartData: any;
  public chartOptions: any;

  constructor(public climateDataControllerService: ClimateDataControllerService, private messageService: MessageService) {
  }

  ngOnInit(): void {
    let startDateTime: Date = this.rangeDates[0];
    let endDateTime: Date = this.rangeDates[1];
    this.getMeasurements(
      this.accessPointUuid,
      this.temperaStationId,
      this.sensorTypes,
      startDateTime,
      endDateTime,
      this.numberOfDisplayedEntries
    );
    this.intervalId = setInterval(() => this.getMeasurements(
      this.accessPointUuid,
      this.temperaStationId,
      this.sensorTypes,
      startDateTime,
      endDateTime,
      this.numberOfDisplayedEntries
    ), (endDateTime.getTime() - startDateTime.getTime()) / this.numberOfDisplayedEntries + 1_000);
    // divide the selected datetime range by the number of measurements to be
    // displayed and add 1 second to make sure the latest measurement is always
    // displayed at every reload.
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  public getMeasurements(
    accessPointUuid: string,
    temperaStationId: string,
    sensorTypes: Sensor.SensorTypeEnum[],
    startDateTime: Date,
    endDateTime: Date,
    numberOfDisplayedEntries: number
  ): void {
    for (let sensorType of sensorTypes) {
      this.climateDataControllerService.getMeasurementsBySensorType(
        accessPointUuid, temperaStationId, sensorType, startDateTime.toISOString(), endDateTime.toISOString(), numberOfDisplayedEntries
      ).subscribe({
        next: climateDataDto => {
          if (sensorType == 'TEMPERATURE') {
            this.data1 = climateDataDto.measurementDtos;
          } else if (sensorType == 'NMVOC') {
            this.data2 = climateDataDto.measurementDtos;
          } else if (sensorType == 'HUMIDITY') {
            this.data1 = climateDataDto.measurementDtos;
          } else if (sensorType == 'IRRADIANCE') {
            this.data2 = climateDataDto.measurementDtos;
          }
          this.updateChart();
        },
        error: err => {
          console.error('Failed to fetch data from back end:', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to fetch data from back end:' + err,
          });
        },
      });
    }
  }

  public updateChart(): void {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.chartData = {
      labels: this.data1?.map(measurement => measurement.timestamp?.replace('T', '  ')),
      datasets: [
        {
          label: this.label1,
          data: this.data1?.map(measurement => measurement.value),
          fill: false,
          yAxisID: 'y',
          borderColor: this.color1,
          tension: 0.4,
        },
        {
          label: this.label2,
          data: this.data2?.map(measurement => measurement.value),
          fill: false,
          yAxisID: 'y1',
          borderColor: this.color2,
          tension: 0.4,
        },
      ],
    };

    this.chartOptions = {
      stacked: false,
      maintainAspectRatio: false,
      aspectRatio: 0.6,
      plugins: {
        legend: {
          labels: {
            color: textColor,
          },
        },
      },
      annotation: {
        annotations: {
          line1: {
            type: 'line',
            yMin: 20,
            yMax: 20,
            borderColor: 'rgb(255, 99, 132)',
            borderWidth: 2,
            borderDash: [10, 10],
          },
        },
      },
      responsive: true,
      scales: {
        x: {
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
          },
        },
        xAxes: [{
          type: 'linear',
          time: {
            unit: 'day',
            tooltipFormat: 'll HH:mm',
          },
          scaleLabel: {
            display: true,
            labelString: 'Date',
          },
        }],
        y: {
          type: 'linear',
          display: true,
          position: 'left',
          suggestedMin: 20,
          suggestedMax: 40,
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
          },
        },
        y1: {
          type: 'linear',
          display: true,
          position: 'right',
          suggestedMin: 60,
          suggestedMax: 100,
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            drawOnChartArea: false,
            color: surfaceBorder,
          },
        },
      },
    };
  }
}
