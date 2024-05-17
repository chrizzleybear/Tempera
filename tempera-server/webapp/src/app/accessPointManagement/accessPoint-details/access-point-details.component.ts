import {Component, OnInit} from '@angular/core';
import {AccessPoint} from "../../models/accesspoint.model";
import {ActivatedRoute} from "@angular/router";
import {AccesspointService} from "../../_services/accesspoint.service";
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
    private accessPointService: AccesspointService,
  ) {}

  ngOnInit() {
    this.accessPointId = this.route.snapshot.paramMap.get('id');
    if (this.accessPointId) {
      this.fetchAccessPointDetails(Number(this.accessPointId));
    }
  }

  fetchAccessPointDetails(id: number) {
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
