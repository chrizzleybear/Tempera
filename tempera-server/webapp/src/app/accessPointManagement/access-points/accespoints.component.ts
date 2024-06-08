import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AccessPointService} from "../../_services/access-point.service";
import {AccessPoint} from "../../models/accessPoint.model";
import {NgIf} from "@angular/common";
import {CardModule} from "primeng/card";
import {Message, MessageService} from "primeng/api";
import {MessagesModule} from "primeng/messages";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {AccessPointCreateComponent} from "../access-point-create/access-point-create.component";
import {AccessPointEditComponent} from "../access-point-edit/access-point-edit.component";
import {InputTextModule} from "primeng/inputtext";
import {AccessPointTemperaComponent} from "../access-point-tempera/access-point-tempera.component";
import {ToastModule} from "primeng/toast";


@Component({
  selector: 'app-accespoints',
  standalone: true,
  imports: [
    NgIf,
    CardModule,
    MessagesModule,
    TableModule,
    ButtonModule,
    DialogModule,
    AccessPointCreateComponent,
    AccessPointEditComponent,
    InputTextModule,
    AccessPointTemperaComponent,
    ToastModule
  ],
  templateUrl: './accespoints.component.html',
  styleUrl: './accespoints.component.css'
})
export class AccesspointsComponent implements OnInit{

  accessPoints : AccessPoint[] = [] ;
  filteredAccessPoints: AccessPoint[] = [];
  selectedAccessPoint: AccessPoint | null = null;
  displayCreateDialog: boolean = false;
  displayEditDialog: boolean = false;
  temperaDialogDisplay: boolean = false;

  constructor(
    private accessPointService: AccessPointService,
    private router: Router,
    private messageService: MessageService) {}
  ngOnInit(): void {
    this.loadAccessPoints();
  }


  private loadAccessPoints() {
    this.accessPointService.getAllAccesspoints().subscribe({
      next: (accessPoints) => {
        this.accessPoints = accessPoints;
        this.filteredAccessPoints = this.accessPoints;
        console.log("Loaded accesspoints:", accessPoints);
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error loading Access Points'});
        console.error("Error loading accesspoints:", error);
      }
    });
  }
  deleteAccesspoint(accessPoint: AccessPoint) {
    if (accessPoint) {
        this.accessPointService.deleteAccesspoint(accessPoint.id).subscribe({
          next: () => {
            this.messageService.add({severity: 'success', summary: 'Success', detail: 'Access Point deleted successfully'});
            console.log(`Deleted access point with id: ${accessPoint.id}`);
            this.loadAccessPoints();

          },
          error: (error) => {
            console.error(`Error deleting access point with id: ${accessPoint.id}`, error);
            this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error deleting Access Point'});
          }
        });
    }
  }

  applyFilter(event: any): void {
    const filterValue = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredAccessPoints = this.accessPoints!.filter(accessPoint =>
      accessPoint.room.id.toLowerCase().includes(filterValue)
    );
  }

  createAccessPoint(): void {
    this.displayCreateDialog = true;
  }

  editAccessPoint(accessPoint: AccessPoint): void {
    this.selectedAccessPoint = accessPoint;
    this.displayEditDialog = true;
  }

  onCreateComplete(event: any): void {
    this.displayCreateDialog = false;
    this.loadAccessPoints();
    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Access Point created successfully' });
  }

  onEditComplete(event: any): void {
    this.displayEditDialog = false;
    this.loadAccessPoints();
  }

  viewAccessPointDetails(accessPoint: AccessPoint) {
    this.router.navigate(['/accessPoint', accessPoint.id])
  }

  temperaDialog(accessPoint: AccessPoint) {
    this.selectedAccessPoint = accessPoint;
    this.temperaDialogDisplay = true;
  }
}

