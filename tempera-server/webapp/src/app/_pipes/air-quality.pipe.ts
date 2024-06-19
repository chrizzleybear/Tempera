import { Pipe, PipeTransform } from '@angular/core';
import { FrontendMeasurementDto } from '../../api';
import QualityEnum = FrontendMeasurementDto.QualityEnum;

@Pipe({
  name: 'airQuality',
  standalone: true,
})
export class AirQualityPipe implements PipeTransform {

  transform(value: QualityEnum | undefined): string {
    switch (value) {
      case QualityEnum.Good:
        return 'Good';
      case QualityEnum.Mediocre:
        return 'Medium';
      case QualityEnum.Poor:
        return 'Poor';
      default:
        return 'Unknown';
    }
  }

}
