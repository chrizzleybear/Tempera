import { Component } from '@angular/core';
import {ThresholdTipUpdateDto} from "../models/threshold.model";

@Component({
  selector: 'app-tips',
  standalone: true,
  imports: [],
  templateUrl: './tips.component.html',
  styleUrl: './tips.component.css'
})
export class TipsComponent {

  displayEditThresholdDialog = false;
  tips: string[] = [];

  constructor() {
  }
  ngOnInit() {
    const dto : ThresholdTipUpdateDto = {
      id: 2,
      tip: 'Tip 1',
    };
  }
}
