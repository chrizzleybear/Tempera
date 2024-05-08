import { Component } from '@angular/core';
import {NgForOf} from "@angular/common";
import {Router} from "@angular/router";

interface Room {
  x: number;
  y: number;
  roomId: string;
  fillColor: string;
  shape: 'rectangle' | 'triangle';
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
    { x: 0, y: 0, roomId: 'room_1', fillColor: 'grey', shape: 'rectangle'},
    { x: 130, y: 0, roomId: 'room_2', fillColor: 'grey', shape: 'rectangle'},
    { x: 0, y: 100, roomId: 'Room 3', fillColor: 'grey', shape: 'rectangle'},
    { x: 130, y: 100, roomId: 'Room 4', fillColor: 'grey', shape: 'rectangle'},
    { x: 0, y: 200, roomId: 'Room 5', fillColor: 'grey', shape: 'rectangle'},
    { x: 130, y: 200, roomId: 'Room 6', fillColor: 'grey', shape: 'rectangle'},
    { x: 65, y: 300, roomId: 'Room 7', fillColor: 'grey', shape: 'rectangle'},

  ];
  doors: { x: number, y: number }[] = [
    { x: 90, y: 50 },
    { x: 90, y: 150 },
    { x: 90, y: 250 },
    { x: 130, y: 50 },
    { x: 130, y: 250 },
    { x: 130, y: 150 },
    { x: 65, y: 350 },
  ];

  constructor(private router: Router) {
  }
  changeColor(index: number): void {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    this.fillColor = `rgb(${r}, ${g}, ${b})`;
    this.rooms[index].fillColor = this.fillColor;
  }

  viewDetails(index: number): void {
    console.log('View details for room:', this.rooms[index]);
    this.router.navigate(['/room', this.rooms[index].roomId]);
  }


}
