import {Component} from '@angular/core';
import {FormGroup, FormBuilder} from '@angular/forms';

@Component({
  selector: 'app-room-thresholds',
  templateUrl: './room-thresholds.component.html',
  standalone: true,
  styleUrls: ['./room-thresholds.component.css']
})
/**
 * @class RoomThresholdsComponent
 * This component is responsible for managing and displaying room thresholds.
 */
export class RoomThresholdsComponent {
  thresholdForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.thresholdForm = this.formBuilder.group({
      temperature: '',
      humidity: '',
      irradiance: '',
      nmvoc: ''
    });
  }
}
