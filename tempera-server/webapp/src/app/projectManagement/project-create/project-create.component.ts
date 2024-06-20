import { Component, EventEmitter, Output, OnInit, Input } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { UsersService } from '../../_services/users.service';
import { User } from '../../models/user.model';
import { MessageModule } from 'primeng/message';
import { NgIf } from '@angular/common';
import { ProjectControllerService, SimpleProjectDto } from '../../../api';
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-project-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    MessageModule,
    NgIf,
  ],
  templateUrl: './project-create.component.html',
  styleUrls: ['./project-create.component.css'],
})
export class ProjectCreateComponent implements OnInit{
  projectForm: FormGroup;
  reactivateForm: FormGroup;
  managers: { label: string, value: User }[] | undefined;
  deactivatedProjects: SimpleProjectDto[] = [];
  deactivatedProjectOptions: { label: string, value : string} [] | undefined;
  deactiveProjectsExist: boolean = false;
  @Output() createComplete = new EventEmitter<boolean>();

  constructor(
    private fb: FormBuilder,
    private projectService: ProjectControllerService,
    private usersService: UsersService,
    private messageService: MessageService) {
    this.projectForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      manager: [null, [Validators.required]],
    });
    this.reactivateForm = this.fb.group({
      project: ['', [Validators.required]],
    });
    }


  ngOnInit() {
    if (this.projectCreationEvent) {
      this.projectCreationEvent.subscribe(() => {
        this.fetchDeactivatedProjects();
        this.fetchManagers();
      });
    }
  }

  fetchDeactivatedProjects() {
    this.projectService.getAllSimpleProjects().subscribe({
      next: (projects) => {
        this.deactivatedProjects = projects.filter(project => !project.isActive);
        this.deactivatedProjectOptions = this.deactivatedProjects
          .map(project => ({
            label: `${project.name}`, value: project.projectId,
          }));
        if(this.deactivatedProjects.length > 0) {
          console.log('Deactivated projects:', this.deactivatedProjects);
          this.deactiveProjectsExist = true;
        }
      },
      error: (error) => {
        console.error('Error fetching deactivated projects:', error);
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Failed to load deactivated projects'});
      }
    });
  }

  @Input() projectCreationEvent?: EventEmitter<void>;

  fetchManagers() {
    this.usersService.getAllManagers().subscribe({
      next: (users: User[]) => {
        this.managers = users.map(user => ({
          label: `${user.firstName} ${user.lastName}`, value: user,
        }));
      },
      error: (error) => console.error('Error loading managers:', error),
    });
  }

  reactivateProject() {
    if(this.reactivateForm.valid) {
      const projectId = this.reactivateForm.value.project.value;

      this.projectService.reactivateProject(projectId).subscribe({
        next: () => {
          console.log('Project reactivated successfully');
          this.reactivateForm.reset();
          this.createComplete.emit(true);
        },
        error: (error) => {
          console.error('Error reactivating project:', error);
          this.createComplete.emit(false);
        },
      });
    } else {
      console.error('Invalid reactivation form');
    }
  }
  onSubmit() {
    if (this.projectForm.valid) {
      const dto: SimpleProjectDto = {
        name: this.projectForm.value.name,
        projectId: this.projectForm.value.name,
        isActive: true,
        description: this.projectForm.value.description,
        manager: this.projectForm.value.manager.value.username,
      };
      this.projectService.createProject(dto).subscribe({
        next: () => {
          this.projectForm.reset();
          this.messageService.add({severity: 'success', summary: 'Success', detail: 'Project created successfully'});
          console.log('Project created successfully');
          this.createComplete.emit(true);
        },
        error: (error) => {
          this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error creating project'});
          console.error('Error adding project:', error);
          this.createComplete.emit(false);
        },
      });
    } else {
      this.messageService.add({severity: 'error', summary: 'Error', detail: 'Invalid form'});
      console.error('Invalid form');
    }
  }
}
