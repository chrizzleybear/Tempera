import {Component, EventEmitter, OnChanges, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TemperaStationService} from "../../_services/tempera-station.service";
import {DropdownModule} from "primeng/dropdown";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {User} from "../../models/user.model";
import {UsersService} from "../../_services/users.service";
import {InputTextModule} from "primeng/inputtext";
import {TemperaStation} from "../../models/temperaStation.model";

@Component({
  selector: 'app-tempera-station-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    DropdownModule,
    CheckboxModule,
    ButtonModule,
    InputTextModule
  ],
  templateUrl: './tempera-station-create.component.html',
  styleUrl: './tempera-station-create.component.css'
})
export class TemperaStationCreateComponent implements OnInit, OnChanges{

  temperaForm: FormGroup;
  newTemperaStation: TemperaStation | undefined;
  @Output() onCreateCompleted = new EventEmitter<boolean>();
  users: { label: string; value: User; }[] | undefined;

  constructor(
    private temperaStationService: TemperaStationService,
    private formBuilder: FormBuilder,
  ) {
    this.temperaForm = this.formBuilder.group({
      id: [null, [Validators.required]],
      user: [null, []],
    });
  }

  ngOnInit() {
    this.fetchUsers();
  }

  ngOnChanges() {
    this.fetchUsers();
  }

  onSubmit() {
    if (this.temperaForm.valid) {
      this.newTemperaStation = {
        id: this.temperaForm.value.id,
        user: this.temperaForm.value.user.value.username,
        enabled: false,
        isHealthy: false,
      }
      console.log('Creating temperaStation:', this.newTemperaStation);
      this.temperaStationService.createTemperaStation(this.newTemperaStation).subscribe({
        next: () => {
          this.temperaForm.reset();
          this.onCreateCompleted.emit(true);
        },
        error: (error) => {
          console.error('Failed to create temperaStation:', error);
          this.onCreateCompleted.emit(false);
        },
      });
    }
  }

  fetchUsers() {
    this.temperaStationService.getAvailableUsers().subscribe({
      next: (users: User[]) => {
        this.users = users.map(user => ({
          label: `${user.firstName} ${user.lastName}`, value: user
        }));
      },
      error: (error) => console.error('Error loading managers:', error)
    });
  }

}
