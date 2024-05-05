import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'airQuality',
  standalone: true,
})
export class AirQualityPipe implements PipeTransform {

  transform(value: number): string {
    if (value > 70) {
      return 'Good';
    } else if (value > 40) {
      return 'Moderate';
    } else if (value <= 40 && value >= 0) {
      return 'Poor';
    } else {
      return 'Unknown';
    }
  }

}
