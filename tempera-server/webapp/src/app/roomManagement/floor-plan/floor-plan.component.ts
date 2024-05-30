import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {TemperaStationService} from "../../_services/tempera-station.service";
import {RoomService} from "../../_services/room.service";
import {Subscription} from "rxjs";
import {Room as RoomModel} from "../../models/room.model";
import {DropdownModule} from "primeng/dropdown";

interface Room {
  x: number;
  y: number;
  roomId: string;
  fillColor: string;
  shape: 'rectangle';
  accessPoint?: string;
}
@Component({
  selector: 'app-floor-plan',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DropdownModule
  ],
  templateUrl: './floor-plan.component.html',
  styleUrl: './floor-plan.component.css'
})
/**
 * @class FloorPlanComponent
 * This component represents the floor plan of building.
 * It fetches the room infos and transform them into a svd diagram data.
 */
export class FloorPlanComponent implements OnInit{

  private roomChangedSubscription: Subscription;

  roomIds: string[] = [];
  accesspoints: (boolean | null)[] = [];
  rooms: Room[][] = [];
  roomObjects: RoomModel[] = [];
  currentFloor = 0;
  floors: any[] = [];
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
    this.floors = [
      {label: 'Floor 1', value: 1},
      {label: 'Floor 2', value: 2},
      {label: 'Floor 3', value: 3}
    ];
  }
  viewDetails(index: number): void {
    if (this.rooms[this.currentFloor][index].roomId !== '') {
      this.router.navigate(['/room', this.rooms[this.currentFloor][index].roomId]);
    }
  }

  fetchData() {
    this.fetchrooms();
  }

  fetchrooms() {
    this.roomService.getAllRooms().subscribe({
      next: (data) => {
        console.log(data);
        this.roomObjects = data;
        this.roomIds = data.map((room) => room.id);
        this.createFloorPlan();
        this.accesspoints = data.map((room) => room.accessPoint ? room.accessPoint.isHealthy : null);
        },
      error: (error) => {
        console.error('Failed to load rooms:', error);
      },
    });
  }

  createFloorPlan() {
    this.rooms[0] = [
      { x: 0, y: 0, roomId: this.roomIds[0] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[0] ? 'green' : 'green'},
      { x: 130, y: 0, roomId: this.roomIds[1] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[1] ? 'green' : 'red'},
      { x: 0, y: 100, roomId: this.roomIds[2] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[2] ? 'green' : 'red'},
      { x: 130, y: 100, roomId:this.roomIds[3] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[3] ? 'green' : 'red'},
      { x: 0, y: 200, roomId: this.roomIds[4] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[4] ? 'green' : 'red'},
      { x: 130, y: 200, roomId: this.roomIds[5] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[5] ? 'green' : 'red'},
      { x: 65, y: 300, roomId: this.roomIds[6] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[6] ? 'green' : 'red'},

    ];
    this.rooms[1] = [
      { x: 0, y: 0, roomId: this.roomIds[7] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[7] ? 'green' : 'red'},
      { x: 130, y: 0, roomId: this.roomIds[8] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[8] ? 'green' : 'red'},
      { x: 0, y: 100, roomId: this.roomIds[9] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[9] ? 'green' : 'red'},
      { x: 130, y: 100, roomId: this.roomIds[10] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[10] ? 'green' : 'red'},
      { x: 0, y: 200, roomId: this.roomIds[11] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[11] ? 'green' : 'red'},
      { x: 130, y: 200, roomId: this.roomIds[12] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[12] ? 'green' : 'red'},
      { x: 65, y: 300, roomId: this.roomIds[13] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[13] ? 'green' : 'red'},
    ];
    this.rooms[2] = [
      { x: 0, y: 0, roomId: this.roomIds[14] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[14] ? 'green' : 'red'},
      { x: 130, y: 0, roomId: this.roomIds[15] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[15] ? 'green' : 'red'},
      { x: 0, y: 100, roomId: this.roomIds[16] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[16] ? 'green' : 'red'},
      { x: 130, y: 100, roomId: this.roomIds[17] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[17] ? 'green' : 'red'},
      { x: 0, y: 200, roomId: this.roomIds[18] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[18] ? 'green' : 'red'},
      { x: 130, y: 200, roomId: this.roomIds[19] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[19] ? 'green' : 'red'},
      { x: 65, y: 300, roomId: this.roomIds[20] || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.accesspoints[20] ? 'green' : 'red'},
    ];
  }


  viewDetailsAccesspoint(index: number) {
    if (this.roomObjects[index]) {
      this.roomService.getAccessPoints(this.roomObjects[index + this.currentFloor*7].id).subscribe({
        next: (data) => {
          this.router.navigate(['/accessPoint', data.id]);
        },
        error: (error) => {
          console.error('Failed to load accessPoints:', error);
        },
      });
    }
  }
  selectFloor(event: {value: {label: string, value: number}}): void {
    this.currentFloor = event.value.value - 1;
  }
}
