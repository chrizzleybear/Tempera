import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TemperaStationService } from '../../_services/tempera-station.service';
import { TemperaStation } from '../../models/temperaStation.model';
import {NgIf} from "@angular/common";
import {Sensor} from "../../models/sensor";

@Component({
  selector: 'app-tempera-station-details',
  templateUrl: './tempera-station-details.component.html',
  standalone: true,
  imports: [
    NgIf
  ],
  styleUrls: ['./tempera-station-details.component.css']
})
export class TemperaStationDetailsComponent implements OnInit {

  temperaStationId: string | undefined;
  temperaStation: TemperaStation | undefined;
  private sensors: Sensor[] = [];

  constructor(
    private route: ActivatedRoute,
    private temperaStationService: TemperaStationService
  ) { }

  ngOnInit() {
    this.temperaStationId = this.route.snapshot.paramMap.get('id')!;
    if (this.temperaStationId) {
      this.fetchTemperaStationDetails(this.temperaStationId);
    }
  }

  private fetchTemperaStationDetails(temperaStationId: string) {
    this.temperaStationService.getTemperaStationById(temperaStationId).subscribe({
      next: (data) => {
        console.log('TemperaStation details: ', data);
        this.temperaStation = data;
        this.fetchSensors();
      },
      error: (error) => {
        console.error('Failed to load temperaStation details:', error);
      },
    });
  }

  fetchSensors() {
    this.temperaStationService.getTemperaStationSensors(this.temperaStationId!).subscribe({
      next: (data) => {
        this.sensors = data;
      },
      error: (error) => {
        console.error('Failed to load temperaStation sensors:', error);
      },
    });
  }
}
