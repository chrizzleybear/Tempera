import {Component, OnInit} from '@angular/core';
import {AccessPoint} from "../../models/accessPoint.model";
import {ActivatedRoute} from "@angular/router";
import {AccessPointService} from "../../_services/access-point.service";
import {NgIf} from "@angular/common";
import {CardModule} from "primeng/card";
import {TableModule} from "primeng/table";

@Component({
  selector: 'app-access-point-details',
  standalone: true,
  imports: [
    NgIf,
    CardModule,
    TableModule
  ],
  templateUrl: './access-point-details.component.html',
  styleUrl: './access-point-details.component.css'
})
export class AccessPointDetailsComponent implements OnInit{
  accessPoint: AccessPoint | undefined;
  accessPointId: string | null | undefined;

  constructor(
    private route: ActivatedRoute,
    private accessPointService: AccessPointService,
  ) {}

  ngOnInit() {
    this.accessPointId = this.route.snapshot.paramMap.get('id');
    if (this.accessPointId) {
      this.fetchAccessPointDetails(this.accessPointId);
    }
  }

  fetchAccessPointDetails(id: string) {
    this.accessPointService.getAccesspointById(id).subscribe({
      next: (data) => {
        this.accessPoint = data;
        console.log('Access Point details: ', this.accessPoint);
      },
      error: (error) => {
        console.error('Failed to load access point details:', error);
      }
    });
  }

}
