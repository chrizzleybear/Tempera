import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ProjectService} from "../../_services/project.service";
import {Project} from "../../models/project.model";

@Component({
  selector: 'app-project-edit',
  standalone: true,
  imports: [],
  templateUrl: './project-edit.component.html',
  styleUrl: './project-edit.component.css'
})
export class ProjectEditComponent {

  projectForm: FormGroup;
  @Input({required: true}) project!: Project;
  @Output() editComplete = new EventEmitter<boolean>();

  constructor(private fb: FormBuilder, private projectService: ProjectService) {
    this.projectForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      manager: [null, [Validators.required]]
    });
  }

  ngOnInit() {
    this.projectService.getProjectById(this.project?.id).subscribe({
      next: (data) => {
        this.project = data;
        this.populateForm();
      },
      error: (error) => {
        console.error('Failed to load project details:', error);
      }
    });
  }


  private populateForm() {
    if (this.project) {
      this.projectForm.patchValue({
        name: this.project.name,
        description: this.project.description,
        manager: this.project.manager
      });
    }
  }

  onSubmit() {
    if (this.projectForm.valid) {
      this.project.name = this.projectForm.value.name;
      this.project.description = this.projectForm.value.description;
      this.project.manager = this.projectForm.value.manager;
      this.projectService.updateProject(this.project).subscribe({
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

}
