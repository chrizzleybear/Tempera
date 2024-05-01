import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectService } from '../../_services/project.service';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { UsersService } from '../../_services/users.service';
import { User } from '../../models/user.model';
import {Project} from "../../models/project.model";

@Component({
  selector: 'app-project-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    DropdownModule
  ],
  templateUrl: './project-create.component.html',
  styleUrls: ['./project-create.component.css']
})
export class ProjectCreateComponent {
  projectForm: FormGroup;
  userDropdownOptions: any[] | undefined;

  @Output() creatComplete = new EventEmitter<boolean>();

  constructor(private fb: FormBuilder, private projectService: ProjectService, private usersService: UsersService) {
    this.projectForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      manager: [null, [Validators.required]]
    });
  }

  ngOnInit() {
    this.usersService.getAllUsers().subscribe({
      next: (users: User[]) => {
        console.log('Loaded users:', users);
        this.userDropdownOptions = users.map(user => ({ label: `${user.firstName} ${user.lastName}`, value: user }));
        console.log('User dropdown options:', this.userDropdownOptions);
      },
      error: (error) => console.error('Error loading users:', error)
    });
  }

  onSubmit() {
    if (this.projectForm.valid) {
      console.log('Project Data:', this.projectForm.value);
      this.projectService.createProject(this.projectForm.value).subscribe({
        next: (response) => {
          console.log('Project created successfully:', response);
          this.creatComplete.emit(true);
        },
        error: (error) => {
          console.error('Error creating project:', error);
          this.creatComplete.emit(false);
        }
      });
    } else {
      console.error('Invalid form');
    }
  }

  addProject1() {
    console.log(this.projectForm.value.manager.value.username);
    this.projectService.test1(this.projectForm.value.name, this.projectForm.value.description, this.projectForm.value.manager.value.username ).subscribe({
      next: (response) => {
        console.log("Test response:", this.projectForm.value);
      },
      error: (error) => {
        console.error("Error adding project:", error);
      }
    });
  }
}
