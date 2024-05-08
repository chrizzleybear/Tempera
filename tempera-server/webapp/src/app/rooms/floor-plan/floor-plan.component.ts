import { Component } from '@angular/core';
import {NgForOf} from "@angular/common";

interface Room {
  x: number;
  y: number;
  name: string;
  fillColor: string;
}
@Component({
  selector: 'app-floor-plan',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './floor-plan.component.html',
  styleUrl: './floor-plan.component.css'
})
export class FloorPlanComponent {
  fillColor = 'rgb(255, 0, 0)';
  rooms: Room[] = [
    { x: 0, y: 0, name: 'Room 1', fillColor: 'red' },
    { x: 130, y: 0, name: 'Room 2', fillColor: 'green' },
    { x: 0, y: 130, name: 'Room 3', fillColor: 'blue' },
    { x: 130, y: 130, name: 'Room 4', fillColor: 'yellow' },
    { x: 0, y: 260, name: 'Room 5', fillColor: 'purple' },
    { x: 130, y: 260, name: 'Room 6', fillColor: 'orange' }

  ];

  changeColor(index: number): void {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    this.fillColor = `rgb(${r}, ${g}, ${b})`;
    this.rooms[index].fillColor = this.fillColor;
  }

}
