import {Component, Input} from '@angular/core';
import {ChartModule} from 'primeng/chart';
import {Sensor} from '../../../api';
import {MessagesModule} from 'primeng/messages';
import {MessageModule} from 'primeng/message';
import {FormsModule} from '@angular/forms';
import {InputTextModule} from 'primeng/inputtext';
import {ToastModule} from 'primeng/toast';
import {InputNumberModule} from 'primeng/inputnumber';
import {PanelModule} from 'primeng/panel';
import {DropdownModule} from 'primeng/dropdown';
import {ClimateChart} from '../climate-chart';


type TimeUnit = 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' | 'WEEKS' | 'MONTHS' | 'YEARS'
const sensorTypes: Sensor.SensorTypeEnum[] = ['TEMPERATURE', 'NMVOC'];

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
  styleUrl: './temperature-co2-chart.component.css',
})
export class TemperatureCo2ChartComponent extends ClimateChart {
  @Input() public override accessPointUuid: string = '';
  @Input() public override temperaStationId: string = '';
  @Input() public override rangeDates: Date[] = [];
  @Input() public override numberOfDisplayedEntries: number = 10;

  protected override sensorTypes: Sensor.SensorTypeEnum[] = ['TEMPERATURE', 'NMVOC'];
  protected override color1: string = '#DC143C';
  protected override color2: string = '#228B22';
  protected override label1: string = 'Temperature (°C)';
  protected override label2: string = 'CO2 (ppm)';

  override ngOnInit(): void {
    super.ngOnInit();
  }
}
