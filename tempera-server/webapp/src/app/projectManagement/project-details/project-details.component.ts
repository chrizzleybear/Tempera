import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CardModule} from "primeng/card";
import { ProjectControllerService } from '../../../api';

@Component({
  selector: 'app-project-details',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    TableModule,
    CardModule
  ],
  templateUrl: './project-details.component.html',
  styleUrl: './project-details.component.css'
})
export class ProjectDetailsComponent implements OnInit{

  project: any | undefined;

  constructor(private projectService: ProjectControllerService, private route: ActivatedRoute) {

  }

  ngOnInit(): void {
    const projectId = this.route.snapshot.paramMap.get('id');
    if (projectId) {
      this.fetchProjectDetails(projectId);
    }
  }

  private fetchProjectDetails(projectId: string) {
    this.projectService.getProjectDetailedById(projectId).subscribe({
      next: (data) => {
        this.project = data;
        console.log("Project details: ", this.project);
      },
      error: (error) => {
        console.error('Failed to load project details:', error);
      }
    });
  }
}
