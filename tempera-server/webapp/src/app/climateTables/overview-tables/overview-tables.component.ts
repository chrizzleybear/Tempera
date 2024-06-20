import {Component, OnInit} from '@angular/core';
import {TabViewModule} from "primeng/tabview";
import {TemperatureTableComponent} from "../temperature-table/temperature-table.component";
import {HumidityTableComponent} from "../humidity-table/humidity-table.component";
import {Co2TableComponent} from "../co2-table/co2-table.component";
import {IrrTableComponent} from "../irr-table/irr-table.component";
import {ToastModule} from "primeng/toast";
import {TemperaStationService} from "../../_services/tempera-station.service";
import {TemperaStation} from "../../models/temperaStation.model";
import {AccessPoint} from "../../models/accessPoint.model";
import {User} from "../../models/user.model";
import {StorageService} from "../../_services/storage.service";
import {MessageService} from "primeng/api";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-overview-tables',
  standalone: true,
  imports: [
    TabViewModule,
    TemperatureTableComponent,
    HumidityTableComponent,
    Co2TableComponent,
    IrrTableComponent,
    ToastModule,
    NgIf
  ],
  templateUrl: './overview-tables.component.html',
  styleUrl: './overview-tables.component.css'
})
export class OverviewTablesComponent implements OnInit{

  temperaStationId: string | undefined;
  accessPointId: string | undefined;
  currentUser: User | undefined;
  constructor(
    private temperaStationService: TemperaStationService,
    private storageService: StorageService,
    private messageService: MessageService) {
  }

  ngOnInit(): void {
   this.fetchTemperaAndAccessPoint();
  }

  fetchTemperaAndAccessPoint() {
    this.temperaStationService.getAllTemperaStations().subscribe({
      next: (data) => {
        this.currentUser = this.storageService.getUser();
        this.temperaStationId = data.find((temperaStation) => temperaStation.user === this.currentUser?.id)?.id;
        this.accessPointId = data.find((temperaStation) => temperaStation.user === this.currentUser?.id)?.accessPointId;
        if(this.temperaStationId === undefined || this.accessPointId === undefined) {
          this.messageService.add({severity:'error', summary:'Error', detail:'No tempera station found for the current user'});
          console.error("No tempera station found for the current user");
        }
      },
      error: (error) => {
        console.error(error);
      }
    });
    }
}
