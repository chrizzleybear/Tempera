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
    { x: 0, y: 0, roomId: 'room_1', fillColor: 'white', shape: 'rectangle'},
    { x: 130, y: 0, roomId: 'room_2', fillColor: 'white', shape: 'rectangle'},
    { x: 0, y: 100, roomId: 'room_3', fillColor: 'white', shape: 'rectangle'},
    { x: 130, y: 100, roomId: 'room_4', fillColor: 'white', shape: 'rectangle'},
    { x: 0, y: 200, roomId: 'room_5', fillColor: 'white', shape: 'rectangle'},
    { x: 130, y: 200, roomId: 'room_6', fillColor: 'white', shape: 'rectangle'},
    { x: 65, y: 300, roomId: 'room_7', fillColor: 'white', shape: 'rectangle'},

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

  viewDetails(index: number): void {
    console.log('View details for room:', this.rooms[index]);
    this.router.navigate(['/room', this.rooms[index].roomId]);
  }


}
