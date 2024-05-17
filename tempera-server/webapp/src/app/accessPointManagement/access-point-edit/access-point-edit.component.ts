import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AccessPoint} from "../../models/accesspoint.model";

@Component({
  selector: 'app-access-point-edit',
  standalone: true,
  imports: [],
  templateUrl: './access-point-edit.component.html',
  styleUrl: './access-point-edit.component.css'
})
export class AccessPointEditComponent {
  @Input() accessPoint!: AccessPoint;
  @Output() editComplete = new EventEmitter<unknown>();

}
