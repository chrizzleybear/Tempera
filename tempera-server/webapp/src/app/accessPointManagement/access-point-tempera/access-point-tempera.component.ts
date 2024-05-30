import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TemperaStation} from "../../models/temperaStation.model";
import {AccessPoint} from "../../models/accessPoint.model";
import {AccessPointService} from "../../_services/access-point.service";

@Component({
  selector: 'app-access-point-tempera',
  standalone: true,
  imports: [],
  templateUrl: './access-point-tempera.component.html',
  styleUrl: './access-point-tempera.component.css'
})
export class AccessPointTemperaComponent implements OnInit{

  tempera: TemperaStation[] = [];
  availableTempera: TemperaStation[] = [];

  @Input() accessPoint!: AccessPoint;
  @Output() accessPointChange: EventEmitter<AccessPoint> = new EventEmitter<AccessPoint>();

  constructor(private accessPointService: AccessPointService) {
  }

  ngOnInit(): void {
    this.fetchTempera();
    }

    fetchTempera() {
      this.accessPointService.getTemperaStations(this.accessPoint.id).subscribe({
        next: (tempera) => {
          this.tempera = tempera;
          console.log("Loaded tempera:", tempera);
        },
        error: (error) => {
          console.error("Error loading tempera:", error);
        }
      });
  }

  fetchAvailableTempera() {
    this.accessPointService.getAvailableTemperaStations().subscribe({
      next: (tempera) => {
        this.availableTempera = tempera;
        console.log("Loaded available tempera:", tempera);
      },
      error: (error) => {
        console.error("Error loading available tempera:", error);
      }
    });
  }
}
