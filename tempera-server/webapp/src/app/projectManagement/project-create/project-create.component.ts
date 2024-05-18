import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectService } from '../../_services/project.service';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { UsersService } from '../../_services/users.service';
import { User } from '../../models/user.model';
import {ProjectCreateDTO} from "../../models/projectDtos";
import {MessageModule} from "primeng/message";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-project-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    MessageModule,
    NgIf
  ],
  templateUrl: './project-create.component.html',
  styleUrls: ['./project-create.component.css']
})
export class ProjectCreateComponent {
  projectForm: FormGroup;
  managers: { label: string, value: User }[] | undefined;
  @Output() createComplete = new EventEmitter<boolean>();

  constructor(private fb: FormBuilder, private projectService: ProjectService, private usersService: UsersService) {
    this.projectForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      manager: [null, [Validators.required]]
    });
  }

  ngOnInit() {
    this.fetchManagers();
  }

  fetchManagers() {
    this.usersService.getAllManagers().subscribe({
      next: (users: User[]) => {
        this.managers = users.map(user => ({
          label: `${user.firstName} ${user.lastName}`, value: user
        }));
      },
      error: (error) => console.error('Error loading managers:', error)
    });
  }

  onSubmit() {
    if (this.projectForm.valid) {
      const dto: ProjectCreateDTO = {
        name: this.projectForm.value.name,
        description: this.projectForm.value.description,
        manager: this.projectForm.value.manager.value.username
      };
      this.projectService.createProject(dto).subscribe({
        next: () => {
          this.projectForm.reset();
          console.log('Project created successfully');
          this.createComplete.emit(true);
        },
        error: (error) => {
          console.error("Error adding project:", error);
          this.createComplete.emit(false);
        }
      });
    } else {
      console.error('Invalid form');
    }
  }
}
