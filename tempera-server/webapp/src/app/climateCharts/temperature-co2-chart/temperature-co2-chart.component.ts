import {Component, OnInit} from '@angular/core';
import {ChartModule} from "primeng/chart";

@Component({
  selector: 'app-temperature-co2-chart',
  standalone: true,
  imports: [
    ChartModule
  ],
  templateUrl: './temperature-co2-chart.component.html',
  styleUrl: './temperature-co2-chart.component.css'
})
export class TemperatureCo2ChartComponent implements OnInit{

  measurements: { timestamp: string, value: number }[] = [
    { timestamp: '2022-01-01 08:30:00', value: 30 },
    { timestamp: '2022-01-02 08:34:00', value: 31 },
    { timestamp: '2022-01-03 08:35:00', value: 33 },
    { timestamp: '2022-01-04 08:36:00', value: 29 },
    { timestamp: '2022-01-05 08:37:00', value: 30 },
    { timestamp: '2022-01-06 08:38:00', value: 31 },
    { timestamp: '2022-01-07 08:39:00', value: 32 },
    { timestamp: '2022-01-08 08:40:00', value: 32 },
    { timestamp: '2022-01-09 08:41:00', value: 31 },
    { timestamp: '2022-01-10 08:42:00', value: 30 },
  ];

  measurements2: { timestamp: string, value: number }[] = [
    { timestamp: '2022-01-01 08:30:00', value: 70 },
    { timestamp: '2022-01-02 08:34:00', value: 72 },
    { timestamp: '2022-01-03 08:35:00', value: 71 },
    { timestamp: '2022-01-04 08:36:00', value: 72 },
    { timestamp: '2022-01-05 08:37:00', value: 70 },
    { timestamp: '2022-01-06 08:38:00', value: 70 },
    { timestamp: '2022-01-07 08:39:00', value: 70 },
    { timestamp: '2022-01-08 08:40:00', value: 65 },
    { timestamp: '2022-01-09 08:41:00', value: 70 },
    { timestamp: '2022-01-10 08:42:00', value: 60 },
  ];


  chartData: any;
  chartOptions: any;

  ngOnInit() {

    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.chartData = {
      labels: this.measurements.map(m => m.timestamp),
      datasets: [
        {
          label: 'Temperature (°C)',
          data: this.measurements.map(m => m.value),
          fill: false,
          yAxisID: 'y',
          borderColor: '#42A5F5',
          tension: 0.4,
        },
        {
          label: 'CO2 (ppm)',
          data: this.measurements2.map(m => m.value),
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
            borderDash: [10,10]
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
}

