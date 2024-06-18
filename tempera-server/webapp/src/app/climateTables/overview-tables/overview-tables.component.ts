import { Component } from '@angular/core';
import {TabViewModule} from "primeng/tabview";
import {TemperatureTableComponent} from "../temperature-table/temperature-table.component";
import {HumidityTableComponent} from "../humidity-table/humidity-table.component";

@Component({
  selector: 'app-overview-tables',
  standalone: true,
  imports: [
    TabViewModule,
    TemperatureTableComponent,
    HumidityTableComponent
  ],
  templateUrl: './overview-tables.component.html',
  styleUrl: './overview-tables.component.css'
})
export class OverviewTablesComponent {

}
