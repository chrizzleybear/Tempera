import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TemperaStationService } from '../../_services/tempera-station.service';
import { TemperaStation } from '../../models/temperaStation.model';
import {TableModule} from "primeng/table";
import {NgIf} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {TemperaStationCreateComponent} from "../tempera-station-create/tempera-station-create.component";
import {TemperaStationEditComponent} from "../tempera-station-edit/tempera-station-edit.component";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {MessagesModule} from "primeng/messages";
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-tempera-stations',
  templateUrl: './tempera-stations.component.html',
  standalone: true,
  imports: [
    TableModule,
    NgIf,
    DialogModule,
    TemperaStationCreateComponent,
    TemperaStationEditComponent,
    ButtonModule,
    InputTextModule,
    MessagesModule,
    ToastModule
  ],
  styleUrls: ['./tempera-stations.component.css']
})
/**
 * @class TemperaStationsComponent
 * This component is responsible for managing and displaying a list of tempera stations.
 */
export class TemperaStationsComponent implements OnInit {

  temperaStations: TemperaStation[] | undefined;
  filteredTemperaStations: TemperaStation[] = [];
  selectedTemperaStation: TemperaStation | undefined;
  displayEditDialog: boolean = false;
  displayCreateDialog: boolean = false;

  constructor(
    private router: Router,
    private temperaStationService: TemperaStationService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.fetchTemperaStations();
  }

  private fetchTemperaStations() {
    this.temperaStationService.getAllTemperaStations().subscribe({
      next: (data) => {
        this.temperaStations = data;
        this.filteredTemperaStations = data;
      },
      error: (error) => {
        console.error('Failed to load temperaStations:', error);
        this.messageService.add({severity:'error', summary:'Error', detail:'Failed to load tempera stations'});
      },
    });
  }

  viewDetails(temperaStation: TemperaStation) {
    this.router.navigate(['/temperaStation', temperaStation.id]);
  }

  editTemperaStationDialog(tempStation: TemperaStation) {
    this.selectedTemperaStation = tempStation;
    this.displayEditDialog = true;
  }

  deleteTemperaStation(tempStation: TemperaStation) {
    this.temperaStationService.deleteTemperaStation(tempStation.id).subscribe({
      next: () => {
        this.messageService.add({severity:'success', summary:'Success', detail:'Tempera station deleted successfully'});
        this.fetchTemperaStations();
      },
      error: (error) => {
        this.messageService.add({severity:'error', summary:'Error', detail:'Failed to delete tempera station'});
      },
    });
  }

  createTemperaStation() {
    this.displayCreateDialog = true;
  }

  onCreateCompleted(success: boolean) {
    if (success) {
      this.messageService.add({severity:'success', summary:'Success', detail:'Tempera station created successfully'});
      this.fetchTemperaStations();
      this.displayCreateDialog = false;
    }
  }

  onEditCompleted(success: boolean) {
    if (success) {
      this.messageService.add({severity:'success', summary:'Success', detail:'Tempera station updated successfully'});
      this.fetchTemperaStations();
      this.displayEditDialog = false;
    }
  }

  applyFilter($event: Event) {
    const filterValue = ($event.target as HTMLInputElement).value;
    this.filteredTemperaStations= this.temperaStations!.filter(tempStation => {
      return tempStation.id.toLowerCase().includes(filterValue.toLowerCase());
    });
  }
}
