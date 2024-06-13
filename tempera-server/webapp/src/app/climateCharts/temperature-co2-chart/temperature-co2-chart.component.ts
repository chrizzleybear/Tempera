import {Component, OnInit} from "@angular/core";
import {ChartModule} from "primeng/chart";
import {ClimateDataControllerService, ClimateMeasurementDto, Sensor} from "../../../api";
import {MessageService} from "primeng/api";
import {MessagesModule} from "primeng/messages";
import {MessageModule} from "primeng/message";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {ToastModule} from "primeng/toast";
import {isNumber} from "chart.js/helpers";
import {InputNumberModule} from "primeng/inputnumber";
import {PanelModule} from "primeng/panel";
import {DropdownModule} from "primeng/dropdown";


type TimeUnit = "SECONDS" | "MINUTES" | "HOURS" | "DAYS" | "WEEKS" | "MONTHS" | "YEARS"
const sensorTypes: Sensor.SensorTypeEnum[] = ["TEMPERATURE", "NMVOC"];

@Component({
  selector: 'app-temperature-co2-chart',
  standalone: true,
  imports: [
    ChartModule,
    MessageModule,
    MessagesModule,
    FormsModule,
    InputTextModule,
    ToastModule,
    InputNumberModule,
    PanelModule,
    DropdownModule,
  ],
  templateUrl: './temperature-co2-chart.component.html',
  styleUrl: './temperature-co2-chart.component.css'
})
export class TemperatureCo2ChartComponent implements OnInit {

  public timeout: number = 0.5;  // chart refresh rate in minutes
  public timeUnit: TimeUnit = "MINUTES";
  public timeAmount: number = 1;
  public accessPointUuid: string = "123e4567-e89b-12d3-a456-426614174001";
  public temperaStationId: string = "tempera_station_1";

  public timeUnits: TimeUnit[] = ["SECONDS", "MINUTES", "HOURS", "DAYS", "WEEKS", "MONTHS", "YEARS"];
  private intervalId: any;
  public chartData: any;
  public chartOptions: any;
  public temperatureData: ClimateMeasurementDto[] | undefined = [];
  public co2Data: ClimateMeasurementDto[] | undefined = [];

  constructor(public climateDataControllerService: ClimateDataControllerService, private messageService: MessageService) {
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
        this.accessPointUuid, this.temperaStationId, sensorType, this.timeUnit, this.timeAmount
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

  private startInterval(): void {
    this.intervalId = setInterval(() => this.getMeasurements(), this.timeout * 60 * 1_000);
  }

  // public updateTimeUnit(unit: TimeUnit): void {
  // this.timeUnit = unit;
  public updateTimeAmount(): void {
    if (!isNumber(this.timeAmount)) {
      console.log("Invalid user input for time unit.");
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid input',
        detail: `Time amount must be a number.`
      });
      this.timeAmount = 1;
    } else {
      console.log("Changed time amount from user input to " + this.timeAmount);
      this.messageService.add({
        severity: 'info',
        summary: 'Time amount Updated',
        detail: `Time amount set to ${this.timeAmount}.`
      });
    }
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
    this.startInterval();
  }

  // public updateTimeout(timeout: number): void {
  //   this.timeout = timeout;
  public updateTimeout(): void {
    if (!isNumber(this.timeout)) {
      console.log("Invalid user input for timeout. Reset timeout to 0.5 minutes.");
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid input',
        detail: `Timeout must be a number. Timeout reset to 0.5 minutes.`
      });
      this.timeout = 0.5;
    } else {
      console.log("Changed timeout from user input to " + this.timeout + " minutes.");
      this.messageService.add({
        severity: 'info',
        summary: 'Timeout Updated',
        detail: `Timeout set to ${this.timeout} minutes.`
      });
    }
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
    this.startInterval();
  }

  printSettings(): void {
    console.log("Timeout: " + this.timeout);
  }
}

