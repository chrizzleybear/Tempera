import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import { TemperaStationService } from "../../_services/tempera-station.service";
import {TemperaStation} from "../../models/temperaStation.model";
import {TableModule} from "primeng/table";
import {NgIf} from "@angular/common";


@Component({
  selector: 'app-tempera-stations',
  templateUrl: './tempera-stations.component.html',
  standalone: true,
  imports: [
    TableModule,
    NgIf
  ],
  styleUrls: ['./tempera-stations.component.css']
})
export class TemperaStationsComponent implements OnInit {

  temperaStations: TemperaStation[] | undefined;

  constructor(
    private router: Router,
    private temperaStationService: TemperaStationService,
  ) { }

  ngOnInit() {
    this.fetchTemperaStations();
  }


  private fetchTemperaStations() {
    this.temperaStationService.getAllTemperaStations().subscribe({
      next: (data) => {
        console.log('TemperaStations: ', data);
        this.temperaStations = data;
      },
      error: (error) => {
        console.error('Failed to load temperaStations:', error);
      },
    });
  }

  viewDetails(temperaStation: TemperaStation) {
    this.router.navigate(['/temperaStation', temperaStation.id]);
  }
}
