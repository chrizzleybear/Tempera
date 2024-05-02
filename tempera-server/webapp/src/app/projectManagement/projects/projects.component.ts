import {Component, OnInit} from '@angular/core';
import {Project} from "../../models/project.model";
import {ProjectService} from '../../_services/project.service';
import {MessagesModule} from "primeng/messages";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {NgIf} from "@angular/common";
import {UserCreateComponent} from "../../userManagement/user-create/user-create.component";
import {ProjectCreateComponent} from "../project-create/project-create.component";
import {DialogModule} from "primeng/dialog";
@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [
    MessagesModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    NgIf,
    UserCreateComponent,
    ProjectCreateComponent,
    DialogModule
  ],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.css'
})
export class ProjectsComponent implements OnInit{

  projects: Project[] = [];
  filteredProjects: Project[] = [];
  messages: any;
  displayCreateDialog: boolean = false;

  constructor(private projectService: ProjectService) {

  }
  ngOnInit(): void {
    this.loadProjects();
    this.filteredProjects = this.projects;
  }

  private loadProjects() {
    this.projectService.getAllProject().subscribe({
      next: (projects) => {
        console.log("Loaded projects:", projects);
        this.projects = projects;
        this.filteredProjects = projects;
      },
      error: (error) => {
        console.error("Error loading projects:", error);
      }
    });
  }
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredProjects = this.projects.filter(project =>
        project.name.toLowerCase().includes(filterValue.toLowerCase())
      );
    }
    else {
        this.filteredProjects = this.projects;
      }
  }
  createProject() {
    this.displayCreateDialog = true;
  }
    deleteProject(projectName: string) {
      this.projectService.deleteProject(projectName).subscribe({
        next: (response) => {
          this.loadProjects();
          this.messages = [{severity:'success', summary:'Success', detail:'Project deleted successfully'}];
        },
        error: (error) => {
          this.messages = [{severity:'error', summary:'Error', detail:'Error deleting project'}];
        }
      })
  };

  onCreateCompleted(success: boolean) {
    if (success) {
      this.messages = [{severity:'success', summary:'Success', detail:'Project created successfully'}];
      this.returnToProjects();
    }
  }
  returnToProjects() {
    this.loadProjects();
    this.displayCreateDialog = false;
  }
}
