import { Component, Input } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { Sensor } from '../../../api';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
import { PanelModule } from 'primeng/panel';
import { DropdownModule } from 'primeng/dropdown';
import { ClimateChart } from '../climate-chart';


@Component({
  selector: 'app-humidity-irradiance-chart',
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
  templateUrl: './humidity-irradiance-chart.component.html',
  styleUrl: './humidity-irradiance-chart.component.css',
})
export class HumidityIrradianceChartComponent extends ClimateChart {
  @Input() public override accessPointUuid: string = '';
  @Input() public override temperaStationId: string = '';
  @Input() public override rangeDates: Date[] = [];

  protected override sensorTypes: Sensor.SensorTypeEnum[] = ['TEMPERATURE', 'NMVOC'];
  protected override color1: string = '#0047AB';
  protected override color2: string = '#F08000';
  protected override label1: string = 'Humidity (%)';
  protected override label2: string = 'Irradiance (lux)';

  override ngOnInit(): void {
    super.ngOnInit();
  }
}
