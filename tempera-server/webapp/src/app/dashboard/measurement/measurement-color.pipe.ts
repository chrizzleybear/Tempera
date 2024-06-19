import { Pipe, PipeTransform } from '@angular/core';
import { FrontendMeasurementDto } from '../../../api';
import QualityEnum = FrontendMeasurementDto.QualityEnum;

@Pipe({
  name: 'measurementColor',
  standalone: true
})
export class MeasurementColorPipe implements PipeTransform {

  transform(value?: QualityEnum): string {
    switch (value) {
      case QualityEnum.Good:
        return 'success';
      case QualityEnum.Mediocre:
        return 'warning';
      case QualityEnum.Poor:
        return 'danger';
      default:
        return 'secondary';
    }
  }

}
