import { Component } from '@angular/core';
import {FeatureCollection, MapService} from "./map.service";

@Component({
  selector: 'app-floor-map',
  standalone: true,
  imports: [],
  templateUrl: './floor-map.component.html',
  styleUrl: './floor-map.component.css'
})
export class FloorMapComponent {

  projection = {
    to(coordinates: number[]) {
      return [coordinates[0] / 100, coordinates[1] / 100];
    },
    from(coordinates: number[]) {
      return [coordinates[0] * 100, coordinates[1] * 100];
    },
  };

  roomsData: FeatureCollection;

  buildingData: FeatureCollection;

  constructor(service: MapService) {
    this.roomsData = service.getRoomsData();
    this.buildingData = service.getBuildingData();
  }

}
