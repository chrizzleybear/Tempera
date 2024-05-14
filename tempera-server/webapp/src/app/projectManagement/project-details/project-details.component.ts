import { Component } from '@angular/core';
import {Project} from "../../models/project.model";
import {ProjectService} from "../../_services/project.service";
import {ActivatedRoute} from "@angular/router";
import {NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {CardModule} from "primeng/card";

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
export class ProjectDetailsComponent {

  project: Project | undefined;

  constructor(private projectService: ProjectService, private route: ActivatedRoute) {

  }

  ngOnInit(): void {
    const projectId = this.route.snapshot.paramMap.get('id');
    if (projectId) {
      this.fetchProjectDetails(parseInt(projectId));
    }
  }

  private fetchProjectDetails(projectId: number) {
    this.projectService.getProjectById(projectId).subscribe({
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
