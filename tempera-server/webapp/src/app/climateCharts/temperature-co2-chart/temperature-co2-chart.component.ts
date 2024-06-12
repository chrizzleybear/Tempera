import {Component, OnInit} from "@angular/core";
import {ChartModule} from "primeng/chart";
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";
import {MessageModule} from "primeng/message";


const sensorTypes: Sensor.SensorTypeEnum[] = ["TEMPERATURE", "NMVOC"];

@Component({
  selector: 'app-temperature-co2-chart',
  standalone: true,
  imports: [
    ChartModule,
    MessageModule,
  ],
  templateUrl: './temperature-co2-chart.component.html',
  styleUrl: './temperature-co2-chart.component.css'
})
export class TemperatureCo2ChartComponent implements OnInit {

  private intervalId: any;

  public timeout: number = 0.5;  // chart refresh rate in minutes

  public chartData: any;

  public chartOptions: any;

  public accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";

  public temperaStationId: string = "tempera_station_1";

  public temperatureData: ClimateMeasurementDto[] | undefined = [];

  public co2Data: ClimateMeasurementDto[] | undefined = [];

  constructor(public climateDataControllerService: ClimateDataControllerService, public messageService: MessageService) {
  }


  ngOnInit(): void {
    this.getMeasurements();
    // timeout is converted form minutes (for nicer display on the web app) to milliseconds (parameter taken by setInterval)
    this.intervalId = setInterval(() => this.getMeasurements(), this.timeout * 60 * 1_000)
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  private getMeasurements(): void {
    for (let sensorType of sensorTypes) {
      this.climateDataControllerService.getMeasurementsBySensorType(
        this.accessPointUuid, this.temperaStationId, sensorType
      ).subscribe({
        next: climateDataDto => {
          if (sensorType == "TEMPERATURE") {
            this.temperatureData = climateDataDto.measurementDtos;
          } else if (sensorType == "NMVOC") {
            this.co2Data = climateDataDto.measurementDtos;
          }
          console.log("Climate data's measurement DTOs: " + climateDataDto.measurementDtos?.map(item => `${item.timestamp}, ${item.value}`));
          this.updateChart();
          this.printSettings();
        },
        error: err => {
          console.error('Failed to fetch data from back end:', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to fetch data from back end:' + err
          });
        },
      });
    }
  }

  private updateChart(): void {
    if (this.temperatureData == undefined || this.temperatureData.length == 0) {
      this.messageService.add({severity: 'warning', summary: 'Warning', detail: "No data available."});
    } else {
      this.temperatureData?.map(item => {
        console.log("Available data for chart display: " + `${item.timestamp}, ${item.value}`);
        if (item.timestamp == null || item.value == null) {
          this.messageService.add({severity: 'warning', summary: 'Warning', detail: "Can't display void measurement."});
        }
      })
    }

    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.chartData = {
      labels: this.temperatureData?.map(measurement => measurement.timestamp),
      datasets: [
        {
          label: 'Temperature (°C)',
          data: this.temperatureData?.map(measurement => measurement.value),
          fill: false,
          yAxisID: 'y',
          borderColor: '#42A5F5',
          tension: 0.4,
        },
        {
          label: 'CO2 (ppm)',
          data: this.co2Data?.map(measurement => measurement.value),
          fill: false,
          yAxisID: 'y1',
          borderColor: '#66BB6A',
          tension: 0.4,
        }
      ]
    };

    this.chartOptions = {
      stacked: false,
      maintainAspectRatio: false,
      aspectRatio: 0.6,
      plugins: {
        legend: {
          labels: {
            color: textColor
          }
        }
      },
      annotation: {
        annotations: {
          line1: {
            type: 'line',
            yMin: 20,
            yMax: 20,
            borderColor: 'rgb(255, 99, 132)',
            borderWidth: 2,
            borderDash: [10, 10]
          }
        }
      },
      responsive: true,
      scales: {
        x: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder
          }
        },
        xAxes: [{
          type: 'linear',
          time: {
            unit: 'day',
            tooltipFormat: 'll HH:mm'
          },
          scaleLabel: {
            display: true,
            labelString: 'Date'
          }
        }],
        y: {
          type: 'linear',
          display: true,
          position: 'left',
          suggestedMin: 20,
          suggestedMax: 40,
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder
          }
        },
        y1: {
          type: 'linear',
          display: true,
          position: 'right',
          suggestedMin: 60,
          suggestedMax: 100,
          ticks: {
            color: textColorSecondary
          },
          grid: {
            drawOnChartArea: false,
            color: surfaceBorder
          }
        }
      }
    };
  }

  printSettings(): void {
    console.log("Timeout: " + this.timeout);
  }
}

