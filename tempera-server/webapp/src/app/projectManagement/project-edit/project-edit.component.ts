import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProjectService} from "../../_services/project.service";
import {Project} from "../../models/project.model";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {MessageModule} from "primeng/message";
import {NgIf} from "@angular/common";
import {PaginatorModule} from "primeng/paginator";
import {User} from "../../models/user.model";
import {UsersService} from "../../_services/users.service";
import {ProjectUpdateDTO} from "../../models/projectDtos";

@Component({
  selector: 'app-project-edit',
  standalone: true,
  imports: [
    ButtonModule,
    InputTextModule,
    MessageModule,
    NgIf,
    PaginatorModule,
    ReactiveFormsModule
  ],
  templateUrl: './project-edit.component.html',
  styleUrl: './project-edit.component.css'
})
export class ProjectEditComponent implements OnChanges, OnInit {

  projectForm: FormGroup;
  managers: any[] = [];
  @Input({required: true}) project!: Project;
  @Output() editComplete = new EventEmitter<boolean>();

  constructor(private fb: FormBuilder, private projectService: ProjectService, private usersService: UsersService) {
    this.projectForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      manager: [null, [Validators.required]]
    });
  }

  ngOnInit() {
    this.projectService.getProjectById(this.project?.projectId).subscribe({
      next: (data) => {
        this.project = data;
        this.populateForm();
      },
      error: (error) => {
        console.error('Failed to load project details:', error);
      }
    });
    this.fetchManagers();
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('ProjectEditComponent: ngOnChanges:', this.project);
    this.fetchManagers();
    this.populateForm();
  }

  private populateForm() {
    if (this.project) {
      this.projectForm.patchValue({
        name: this.project.name,
        description: this.project.description,
        manager: this.managers.find(manager => manager.value.username === this.project.manager.id)
      });
    }
  }

  onSubmit() {
    if (this.projectForm.valid) {
      const dto: ProjectUpdateDTO = {
        projectId: this.project.projectId,
        name: this.projectForm.value.name,
        description: this.projectForm.value.description,
        manager: this.projectForm.value.manager.value.username
      }
      this.projectService.updateProject(dto).subscribe({
        next: (response) => {
          console.log('Project updated successfully:', response);
          this.editComplete.emit(true);
        },
        error: (error) => {
          console.error("Error updating project:", error);
          this.editComplete.emit(false);
        }
      });
    } else {
      console.error('Invalid form');
    }
  }
  fetchManagers() {
    this.usersService.getAllManagers().subscribe({
      next: (users: User[]) => {
        console.log('Loaded users:', users);
        this.managers = users.map(user => ({ label: `${user.firstName} ${user.lastName}`, value: user }));
        console.log('User dropdown options:', this.managers);
      },
      error: (error) => console.error('Error loading users:', error)
    });
  }
}
