import {Component, OnInit} from '@angular/core';
import {Project} from "../../models/project.model";
import {MessagesModule} from "primeng/messages";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {NgIf} from "@angular/common";
import {UserCreateComponent} from "../../userManagement/user-create/user-create.component";
import {ProjectCreateComponent} from "../project-create/project-create.component";
import {DialogModule} from "primeng/dialog";
import {Router} from "@angular/router";
import {ProjectEditComponent} from "../project-edit/project-edit.component";
import { ProjectControllerService, SimpleProjectDto } from '../../../api';
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
    DialogModule,
    ProjectEditComponent
  ],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.css'
})
/**
 * @class ProjectsComponent
 * This component is for managing and displaying all projects.
 */
export class ProjectsComponent implements OnInit{

  projects: SimpleProjectDto[] = [];
  filteredProjects: SimpleProjectDto[] = [];
  messages: any;
  displayCreateDialog: boolean = false;
  displayEditDialog: boolean = false;
  selectedProject: SimpleProjectDto | undefined;

  constructor(private projectService: ProjectControllerService, private router: Router) {

  }
  ngOnInit(): void {
    this.loadProjects();
    this.filteredProjects = this.projects;
  }

  private loadProjects() {
    this.projectService.getAllSimpleProjects().subscribe({
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
    deleteProject(projectId: string) {
      this.projectService.deleteProject(projectId).subscribe({
        next: (response) => {
          this.loadProjects();
          this.messages = [{severity:'success', summary:'Success', detail:'Project deleted successfully'}];
        },
        error: (err) => {
          this.messages = [{severity:'error', summary:'Error', detail:`Error ${err} deleting project`}];
        }
      })
  };

  onCreateCompleted(success: boolean) {
    if (success) {
      this.messages = [{severity:'success', summary:'Success', detail:'Project created successfully'}];
      this.returnToProjects();
    }
  }
  editProject(project: SimpleProjectDto) {
    console.log("Edit project:", project);
    this.selectedProject = project;
    this.displayEditDialog = true;
  }
  onEditCompleted(success: boolean) {
    if (success) {
      this.messages = [{severity:'success', summary:'Success', detail:'Project updated successfully'}];
      this.returnToProjects();
    }
  }
  returnToProjects() {
    this.loadProjects();
    this.displayCreateDialog = false;
    this.displayEditDialog = false;
  }

  viewProjectDetails(project: Project) {
    console.log("View project details:", project);
    console.log("Project ID:", project.projectId);
    this.router.navigate(['/project', project.projectId]);
  }
  addGroupToProject(project: Project) {
    this.router.navigate(['/project/groups', project.projectId]);
  }

}
