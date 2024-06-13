import { Component, Input } from '@angular/core';
import { FrontendMeasurementDto } from '../../../api';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { NgForOf, NgIf, NgSwitch, NgSwitchCase } from '@angular/common';
import { SensorType } from '../../models/threshold.model';
import { WrapFnPipe } from '../../_pipes/wrap-fn.pipe';
import { AirQualityPipe } from '../../_pipes/air-quality.pipe';
import { DialogModule } from 'primeng/dialog';
import { MeasurementColorPipe } from './measurement-color.pipe';

@Component({
  selector: 'app-measurement',
  standalone: true,
  imports: [
    ButtonModule,
    TooltipModule,
    NgSwitchCase,
    NgSwitch,
    WrapFnPipe,
    NgIf,
    AirQualityPipe,
    DialogModule,
    NgForOf,
    MeasurementColorPipe,
  ],
  templateUrl: './measurement.component.html',
  styleUrl: './measurement.component.css',
})
export class MeasurementComponent {

  @Input({ required: true }) measurement!: FrontendMeasurementDto;

  @Input({ required: true }) sensorType!: SensorType;

  @Input() highHints?: string[];

  @Input() lowHints?: string[];

  @Input() qualityHints?: string[];

  public dialogVisible = false;

  protected readonly SensorType = SensorType;

  getUnit(sensorType: SensorType): string {
    switch (sensorType) {
      case SensorType.TEMPERATURE:
        return '°C';
      case SensorType.HUMIDITY:
        return '%';
      case SensorType.IRRADIANCE:
        return 'lx';
      default:
        return '';
    }
  }
}
