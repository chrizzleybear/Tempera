import {Component, OnInit} from '@angular/core';
import {ChartModule} from "primeng/chart";

@Component({
  selector: 'app-humidity-li-chart',
  standalone: true,
  imports: [
    ChartModule
  ],
  templateUrl: './humidity-li-chart.component.html',
  styleUrl: './humidity-li-chart.component.css'
})
export class HumidityLiChartComponent implements OnInit{

  chartData: any;
  chartOptions: any;


  measurements: { timestamp: string, value: number }[] = [
    { timestamp: '2022-01-01 08:30:00', value: 45 },
    { timestamp: '2022-01-02 08:34:00', value: 47 },
    { timestamp: '2022-01-03 08:35:00', value: 50 },
    { timestamp: '2022-01-04 08:36:00', value: 48 },
    { timestamp: '2022-01-05 08:37:00', value: 46 },
    { timestamp: '2022-01-06 08:38:00', value: 49 },
    { timestamp: '2022-01-07 08:39:00', value: 50 },
    { timestamp: '2022-01-08 08:40:00', value: 52 },
    { timestamp: '2022-01-09 08:41:00', value: 51 },
    { timestamp: '2022-01-10 08:42:00', value: 50 },
  ];

  measurements1: { timestamp: string, value: number }[] = [
    { timestamp: '2022-01-01 08:30:00', value: 300 },
    { timestamp: '2022-01-02 08:34:00', value: 320 },
    { timestamp: '2022-01-03 08:35:00', value: 310 },
    { timestamp: '2022-01-04 08:36:00', value: 315 },
    { timestamp: '2022-01-05 08:37:00', value: 290 },
    { timestamp: '2022-01-06 08:38:00', value: 295 },
    { timestamp: '2022-01-07 08:39:00', value: 310 },
    { timestamp: '2022-01-08 08:40:00', value: 320 },
    { timestamp: '2022-01-09 08:41:00', value: 315 },
    { timestamp: '2022-01-10 08:42:00', value: 310 },
  ];
  ngOnInit() {

    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.chartData = {
      labels: this.measurements.map(m => m.timestamp),
      datasets: [
        {
          label: 'Humidity (%)',
          data: this.measurements.map(m => m.value),
          fill: false,
          yAxisID: 'y',
          borderColor: '#42A5F5',
          tension: 0.4,
        },
        {
          label: 'Light Intensity (lux)',
          data: this.measurements1.map(m => m.value),
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
          min: 0,
          max: 100,
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
          suggestedMin: 200,
          suggestedMax: 400,
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

