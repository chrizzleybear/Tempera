import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TemperaStationService} from "../../_services/tempera-station.service";
import {DropdownModule} from "primeng/dropdown";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {User} from "../../models/user.model";
import {UsersService} from "../../_services/users.service";

@Component({
  selector: 'app-tempera-station-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    DropdownModule,
    CheckboxModule,
    ButtonModule
  ],
  templateUrl: './tempera-station-create.component.html',
  styleUrl: './tempera-station-create.component.css'
})
export class TemperaStationCreateComponent implements OnInit{

  temperaForm: FormGroup;
  @Output() onCreateCompleted = new EventEmitter<boolean>();
  users: { label: string; value: User; }[] | undefined;

  constructor(
    private temperaStationService: TemperaStationService,
    private formBuilder: FormBuilder,
    private usersService: UsersService
  ) {
    this.temperaForm = this.formBuilder.group({
      id: [null, [Validators.required]],
      user: [null, []],
    });
  }

  ngOnInit() {
    this.fetchUsers();
  }

  onSubmit() {
    if (this.temperaForm.valid) {
      this.temperaStationService.createTemperaStation(this.temperaForm.value).subscribe({
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
    this.usersService.getAllUsers().subscribe({
      next: (users: User[]) => {
        this.users = users.map(user => ({
          label: `${user.firstName} ${user.lastName}`, value: user
        }));
      },
      error: (error) => console.error('Error loading managers:', error)
    });
  }

}
