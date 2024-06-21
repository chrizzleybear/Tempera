import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Project } from '../../models/project.model';
import { MessagesModule } from 'primeng/messages';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { NgIf } from '@angular/common';
import { UserCreateComponent } from '../../userManagement/user-create/user-create.component';
import { ProjectCreateComponent } from '../project-create/project-create.component';
import { DialogModule } from 'primeng/dialog';
import { Router } from '@angular/router';
import { ProjectEditComponent } from '../project-edit/project-edit.component';
import { ProjectControllerService, SimpleProjectDto } from '../../../api';
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";
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
    ProjectEditComponent,
    ToastModule
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
  displayCreateDialog: boolean = false;
  displayEditDialog: boolean = false;
  selectedProject: SimpleProjectDto | undefined;

  constructor(private projectService: ProjectControllerService, private router: Router, private messageService: MessageService) {

  }
  ngOnInit(): void {
    this.loadProjects();
    this.filteredProjects = this.projects;
  }

  private loadProjects() {
    this.projectService.getAllSimpleProjects().subscribe({
      next: (projects) => {
        console.log("Loaded projects:", projects);
        this.projects = projects.filter(project => project.isActive);
        this.filteredProjects = this.projects;
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

  // Event emitter for creating a project - loading latest deactivatedProjects
  @Output() projectCreationEvent: EventEmitter<void> = new EventEmitter<void>();


  createProject() {
    this.displayCreateDialog = true;
    this.projectCreationEvent.emit();
  }
    deleteProject(projectId: string) {
      this.projectService.deleteProject(projectId).subscribe({
        next: (response) => {
          this.loadProjects();
          this.messageService.add({severity: 'success', summary: 'Success', detail: 'Project deleted successfully'});
        },
        error: (err) => {
          console.error('Error deleting project:', err);
          this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error deleting project'});
        }
      })
  };

  onCreateCompleted(success: boolean) {
    if (success) {
      this.returnToProjects();
    }
  }
  editProject(project: SimpleProjectDto) {
    this.selectedProject = project;
    this.displayEditDialog = true;
  }
  onEditCompleted(success: boolean) {
    if (success) {
      this.returnToProjects();
    }
  }
  returnToProjects() {
    this.loadProjects();
    this.displayCreateDialog = false;
    this.displayEditDialog = false;
  }

  addGroupToProject(project: Project) {
    this.router.navigate(['/project/groups', project.projectId]);
  }

}
