import {Component, OnInit} from '@angular/core';
import {NgForOf} from "@angular/common";
import {Router} from "@angular/router";
import {TemperaStationService} from "../../_services/tempera-station.service";
import {RoomService} from "../../_services/room.service";
import {Subscription} from "rxjs";
import {AccessPoint} from "../../models/accessPoint.model";

interface Room {
  x: number;
  y: number;
  roomId: string;
  fillColor: string;
  shape: 'rectangle';
  accessPoint?: boolean | null;
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
export class FloorPlanComponent implements OnInit{

  private roomChangedSubscription: Subscription;

  temperaStation: any;
  roomIds: string[] = [];
  accesspoints: (boolean | null)[] = [];
  rooms: Room[] = [];
  doors: { x: number, y: number }[] = [
    { x: 90, y: 50 },
    { x: 90, y: 150 },
    { x: 90, y: 250 },
    { x: 130, y: 50 },
    { x: 130, y: 250 },
    { x: 130, y: 150 },
    { x: 65, y: 350 },
  ];

  constructor(private router: Router, private temperaStationService: TemperaStationService, private roomService: RoomService) {
    this.roomChangedSubscription = this.roomService.roomChanged$.subscribe(() => {
      this.fetchData();
    }
    );
  }

  ngOnInit() {
    this.fetchData();

  }
  viewDetails(index: number): void {
    console.log('View details for room:', this.rooms[index]);
    this.router.navigate(['/room', this.rooms[index].roomId]);
  }

  fetchData() {
    this.fetchrooms();
    this.fetchtempera();
  }

  fetchrooms() {
    this.roomService.getAllRooms().subscribe({
      next: (data) => {
        this.roomIds = data.map((room) => room.id);
        this.createFloorPlan();
        this.accesspoints = data.map((room) => room.accessPoint ? room.accessPoint.active : null);
      },
      error: (error) => {
        console.error('Failed to load rooms:', error);
      },
    });
  }

  fetchtempera() {
    this.temperaStationService.getAllTemperaStations().subscribe({
      next: (data) => {
        console.log('TemperaStations: ', data);
        this.temperaStation = data;
      },
      error: (error) => {
        console.error('Failed to load temperaStations:', error);
      },
    });
  }

  createFloorPlan() {
    this.rooms = [
      { x: 0, y: 0, roomId: this.roomIds[0] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[0]},
      { x: 130, y: 0, roomId: this.roomIds[1] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[1]},
      { x: 0, y: 100, roomId: this.roomIds[2] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[2]},
      { x: 130, y: 100, roomId:this.roomIds[3] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[3]},
      { x: 0, y: 200, roomId: this.roomIds[4] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[4]},
      { x: 130, y: 200, roomId: this.roomIds[5] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[5]},
      { x: 65, y: 300, roomId: this.roomIds[6] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[6]},

    ];
  }


}
