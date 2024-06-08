import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {TemperaStationService} from "../../_services/tempera-station.service";
import {RoomService} from "../../_services/room.service";
import {Subscription} from "rxjs";
import {FloorComponent, Room as RoomModel} from "../../models/room.model";
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

  rooms: Room[][] = [];
  floorComponents: FloorComponent[] = [];
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
    this.roomService.getFloorPlan().subscribe({
      next: (data) => {
        console.log(data);
        this.floorComponents = Array.from({ length: 21 }, (v, i) => data[i] || null);
        this.createFloorPlan();
        },
      error: (error) => {
        console.error('Failed to load rooms:', error);
      },
    });
  }

  createFloorPlan() {
    this.rooms[0] = [
      { x: 0, y: 0, roomId: this.floorComponents[0]?.roomId || '' || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[0]?.isHealthy ? 'green' : 'green'},
      { x: 130, y: 0, roomId: this.floorComponents[1]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[1]?.isHealthy ? 'green' : 'red'},
      { x: 0, y: 100, roomId: this.floorComponents[2]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[2]?.isHealthy ? 'green' : 'red'},
      { x: 130, y: 100, roomId:this.floorComponents[3]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[3]?.isHealthy ? 'green' : 'red'},
      { x: 0, y: 200, roomId: this.floorComponents[4]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[4]?.isHealthy ? 'green' : 'red'},
      { x: 130, y: 200, roomId: this.floorComponents[5]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[5]?.isHealthy ? 'green' : 'red'},
      { x: 65, y: 300, roomId: this.floorComponents[6]?.roomId || '' || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[6]?.isHealthy ? 'green' : 'red'},

    ];
    this.rooms[1] = [
      { x: 0, y: 0, roomId: this.floorComponents[7]?.roomId|| '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[7]?.isHealthy ? 'green' : 'red'},
      { x: 130, y: 0, roomId: this.floorComponents[8]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[8]?.isHealthy ? 'green' : 'red'},
      { x: 0, y: 100, roomId: this.floorComponents[9]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[9]?.isHealthy ? 'green' : 'red'},
      { x: 130, y: 100, roomId: this.floorComponents[10]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[10]?.isHealthy ? 'green' : 'red'},
      { x: 0, y: 200, roomId: this.floorComponents[11]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[11]?.isHealthy ? 'green' : 'red'},
      { x: 130, y: 200, roomId: this.floorComponents[12]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[12]?.isHealthy ? 'green' : 'red'},
      { x: 65, y: 300, roomId: this.floorComponents[13]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[13]?.isHealthy ? 'green' : 'red'},
    ];
    this.rooms[2] = [
      { x: 0, y: 0, roomId: this.floorComponents[14]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[14]?.isHealthy ? 'green' : 'red'},
      { x: 130, y: 0, roomId: this.floorComponents[15]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[15]?.isHealthy ? 'green' : 'red'},
      { x: 0, y: 100, roomId: this.floorComponents[16]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[16]?.isHealthy ? 'green' : 'red'},
      { x: 130, y: 100, roomId: this.floorComponents[17]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[17]?.isHealthy ? 'green' : 'red'},
      { x: 0, y: 200, roomId: this.floorComponents[18]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[18]?.isHealthy ? 'green' : 'red'},
      { x: 130, y: 200, roomId: this.floorComponents[19]?.roomId || '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[19]?.isHealthy ? 'green' : 'red'},
      { x: 65, y: 300, roomId: this.floorComponents[20]?.roomId|| '', fillColor: 'white', shape: 'rectangle', accessPoint: this.floorComponents[20]?.isHealthy ? 'green' : 'red'},
    ];
  }
  viewDetailsAccesspoint(index: number) {
    if (this.floorComponents[index]) {
      this.router.navigate(['/accessPoint', this.floorComponents[index + this.currentFloor * 7].accessPointId]);
    }
    else {
      postMessage('No access point found for this room');
    }
  }
  selectFloor(event: {value: {label: string, value: number}}): void {
    this.currentFloor = event.value.value - 1;
  }
}
