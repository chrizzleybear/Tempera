import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {TemperaStation} from "../../models/temperaStation.model";
import {AccessPoint} from "../../models/accessPoint.model";
import {AccessPointService} from "../../_services/access-point.service";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {DialogModule} from "primeng/dialog";
import {TemperaStationService} from "../../_services/tempera-station.service";
import {MessageService} from "primeng/api";

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
export class AccessPointTemperaComponent implements OnInit, OnChanges{

  tempera: TemperaStation[] = [];

  @Input() accessPoint!: AccessPoint;
  @Output() accessPointChange: EventEmitter<AccessPoint> = new EventEmitter<AccessPoint>();
  filteredTemperaStations: TemperaStation[] = [];

  constructor(
    private accessPointService: AccessPointService,
    private temperaStationService: TemperaStationService,
    private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.fetchTempera();
    }

    ngOnChanges() {
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
          this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error loading tempera stations'});
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
        this.messageService.add({severity: 'success', summary: 'Success', detail: 'Tempera station removed'});
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error removing tempera station'});
        console.error("Error removing tempera:", error);
      }
    });
  }
}
