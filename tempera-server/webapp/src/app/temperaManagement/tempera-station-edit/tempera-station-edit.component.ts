import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import { TemperaStationService } from '../../_services/tempera-station.service';
import { TemperaStation } from '../../models/temperaStation.model';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {User} from "../../models/user.model";
import {DropdownModule} from "primeng/dropdown";
import {UsersService} from "../../_services/users.service";

@Component({
  selector: 'app-tempera-station-edit',
  templateUrl: './tempera-station-edit.component.html',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule,
    FormsModule,
    CheckboxModule,
    ButtonModule,
    DropdownModule
  ],
  styleUrls: ['./tempera-station-edit.component.css']
})
export class TemperaStationEditComponent implements OnInit, OnChanges {

  temperaForm: FormGroup;

  @Input() temperaStation!: TemperaStation;
  @Output() onEditComplete = new EventEmitter<boolean>();
  users!: { label: string, value: User }[];

  constructor(
    private temperaStationService: TemperaStationService,
    private formBuilder: FormBuilder,
    private userService: UsersService
  ) {
    this.temperaForm = this.formBuilder.group({
      user: [null, [Validators.required]],
      enabled: [false, [Validators.required]],
    });
  }

  ngOnInit() {
    this.fetchTemperaStationDetails(this.temperaStation.id);
  }

  ngOnChanges() {
    this.fetchTemperaStationDetails(this.temperaStation.id);
  }

  private fetchTemperaStationDetails(temperaStationId: string) {
    this.temperaStationService.getTemperaStationById(temperaStationId).subscribe({
      next: (data) => {
       this.temperaStation = data;
        this.fetchUsers();
      },
      error: (error) => {
        console.error('Failed to load temperaStation details:', error);
      },
    });
  }

  onSubmit() {
    if (this.temperaForm?.valid) {
      console.log(this.temperaForm.value);
      this.temperaStation.user = this.temperaForm.value.user.value.username;
      this.temperaStation.enabled = this.temperaForm.value.enabled;
      this.temperaStationService.updateTemperaStation(this.temperaStation).subscribe({
        next: () => {
          this.temperaForm?.reset();
          this.onEditComplete.emit(true);

        },
        error: (error) => {
          console.error('Failed to update temperaStation:', error);
          this.onEditComplete.emit(false);
        },
      });
    }
  }

  private fetchUsers() {
    this.temperaStationService.getAvailableUsers().subscribe({
      next: (users: User[]) => {
        this.users = users.map(user => ({
          label: `${user.firstName} ${user.lastName}`,
          value: user
        }));
        this.populateForm();
      },
      error: (error) => console.error('Error loading managers:', error)
    });
  }

  populateForm() {
    console.log(this.temperaStation.user);
    console.log(this.users);
    this.temperaForm = this.formBuilder.group({
      user: this.users?.find(user => user.value.username === this.temperaStation.user),
      enabled: this.temperaStation.enabled
    });
  }
}

