import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TemperaStation} from "../../models/temperaStation.model";
import {AccessPoint} from "../../models/accessPoint.model";
import {AccessPointService} from "../../_services/access-point.service";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {DialogModule} from "primeng/dialog";
import {TemperaStationService} from "../../_services/tempera-station.service";

@Component({
  selector: 'app-access-point-tempera',
  standalone: true,
  imports: [
    TableModule,
    ButtonModule,
    InputTextModule,
    DialogModule
  ],
  templateUrl: './access-point-tempera.component.html',
  styleUrl: './access-point-tempera.component.css'
})
export class AccessPointTemperaComponent implements OnInit{

  tempera: TemperaStation[] = [];

  @Input() accessPoint!: AccessPoint;
  @Output() accessPointChange: EventEmitter<AccessPoint> = new EventEmitter<AccessPoint>();
  filteredTemperaStations: TemperaStation[] = [];

  constructor(private accessPointService: AccessPointService, private temperaStationService: TemperaStationService) {
  }

  ngOnInit(): void {
    this.fetchTempera();
    }

    fetchTempera() {
      this.accessPointService.getTemperaStations(this.accessPoint.id).subscribe({
        next: (tempera) => {
          this.tempera = tempera;
          this.filteredTemperaStations = tempera;
          console.log("Loaded tempera:", tempera);
        },
        error: (error) => {
          console.error("Error loading tempera:", error);
        }
      });
  }

  applyFilter($event: Event) {
    const filterValue = ($event.target as HTMLInputElement).value;
    this.filteredTemperaStations= this.tempera!.filter(tempStation => {
      return tempStation.id.toLowerCase().includes(filterValue.toLowerCase());
    });
  }

  removeTempera(temperaStation: TemperaStation) {
    this.temperaStationService.deleteTemperaStation(temperaStation.id).subscribe({
      next: () => {
        this.fetchTempera();
      },
      error: (error) => {
        console.error("Error removing tempera:", error);
      }
    });
  }
}
