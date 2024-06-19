import { Component } from '@angular/core';
import {TabViewModule} from "primeng/tabview";
import {TemperatureTableComponent} from "../temperature-table/temperature-table.component";
import {HumidityTableComponent} from "../humidity-table/humidity-table.component";
import {Co2TableComponent} from "../co2-table/co2-table.component";
import {IrrTableComponent} from "../irr-table/irr-table.component";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-overview-tables',
  standalone: true,
  imports: [
    TabViewModule,
    TemperatureTableComponent,
    HumidityTableComponent,
    Co2TableComponent,
    IrrTableComponent,
    ToastModule
  ],
  templateUrl: './overview-tables.component.html',
  styleUrl: './overview-tables.component.css'
})
export class OverviewTablesComponent {

}
