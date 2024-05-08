import { Component, OnInit } from '@angular/core';
import L from 'leaflet';
import { MapService } from './map.service';
import 'leaflet.vectorgrid';
import {FeatureCollection, Geometry } from 'geojson';

@Component({
  selector: 'app-floor-map',
  standalone: true,
  imports: [
  ],
  templateUrl: './floor-map.component.html',
  styleUrls: ['./floor-map.component.css']
})
export class FloorMapComponent implements OnInit {
  private map!: L.Map;
  roomsData: FeatureCollection;
  buildingData: FeatureCollection;

  constructor(private service: MapService) {
    this.roomsData = this.service.getRoomsData();
    this.buildingData = this.service.getBuildingData();
  }

  ngOnInit(): void {
    this.initMap();
  }

  initMap(): void {
    this.map = L.map('map', {
      center: [40.7128, -74.0060],  // Update this to your building's location
      zoom: 18
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(this.map);

    const vectorData: FeatureCollection<Geometry> = {
      type: "FeatureCollection",  // Explicitly set the type as "FeatureCollection"
      features: [
        {
          type: "Feature",
          geometry: {
            type: "Polygon",
            coordinates: [[[0, 0], [1, 0], [1, 1], [0, 1], [0, 1]]]
          },
          properties: {
            name: "Example Room"
          }
        }
      ]
    };

    L.vectorGrid.slicer(vectorData).addTo(this.map);
  }
}
