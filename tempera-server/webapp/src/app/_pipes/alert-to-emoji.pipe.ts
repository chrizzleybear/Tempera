import { Pipe, PipeTransform } from '@angular/core';
import { AlertDto } from '../../api';

@Pipe({
  name: 'alertToEmoji',
  standalone: true
})
export class AlertToEmojiPipe implements PipeTransform {

  transform(value: AlertDto): string | null {
    switch (value.sensorType) {
      case AlertDto.SensorTypeEnum.Temperature:
        if (value.isUpperBound) return '🥵';
        else return '🥶';
      case AlertDto.SensorTypeEnum.Irradiance:
        if (value.isUpperBound) return '☀️';
        else return '🌙';
      case AlertDto.SensorTypeEnum.Humidity:
        if (value.isUpperBound) return '💦';
        // desert emoji
        else return '🐫';
      case AlertDto.SensorTypeEnum.Nmvoc:
        return '😷';
    }
    return null;
  }

}
