import { Component } from '@angular/core';
import {HumidityLiChartComponent} from "../humidity-li-chart/humidity-li-chart.component";
import {TemperatureCo2ChartComponent} from "../temperature-co2-chart/temperature-co2-chart.component";
import {PanelModule} from "primeng/panel";

@Component({
  selector: 'app-overview-charts',
  standalone: true,
  imports: [
    HumidityLiChartComponent,
    TemperatureCo2ChartComponent,
    PanelModule
  ],
  templateUrl: './overview-charts.component.html',
  styleUrl: './overview-charts.component.css'
})
export class OverviewChartsComponent {

}
