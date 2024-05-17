import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AccesspointService} from "../../_services/accesspoint.service";
import {AccessPoint} from "../../models/accesspoint.model";


@Component({
  selector: 'app-accespoints',
  standalone: true,
  imports: [],
  templateUrl: './accespoints.component.html',
  styleUrl: './accespoints.component.css'
})
export class AccesspointsComponent implements OnInit{

  accessPoints : AccessPoint[] | undefined ;

  constructor(private accessPointService: AccesspointService, private router: Router) {}
  ngOnInit(): void {
    this.loadAccesspoints();
  }


  private loadAccesspoints() {
    this.accessPointService.getAllAccesspoints().subscribe({
      next: (accessPoints) => {
        this.accessPoints = accessPoints
        console.log("Loaded accesspoints:", accessPoints);
      },
      error: (error) => {
        console.error("Error loading accesspoints:", error);
      }
    });
  }

}

