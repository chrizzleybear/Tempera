import {Component} from '@angular/core';
import {FormGroup, FormBuilder} from '@angular/forms';

@Component({
  selector: 'app-room-thresholds',
  templateUrl: './room-thresholds.component.html',
  standalone: true,
  styleUrls: ['./room-thresholds.component.css']
})
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

  onSubmit(): void {

    console.log(this.thresholdForm.value);
  }
}
