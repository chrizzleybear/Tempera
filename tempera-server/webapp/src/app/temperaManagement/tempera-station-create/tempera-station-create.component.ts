import {Component, EventEmitter, OnChanges, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TemperaStationService} from "../../_services/tempera-station.service";
import {DropdownModule} from "primeng/dropdown";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {User} from "../../models/user.model";
import {InputTextModule} from "primeng/inputtext";
import {TemperaStation} from "../../models/temperaStation.model";
import {AccessPoint} from "../../models/accessPoint.model";
import {AccessPointService} from "../../_services/access-point.service";

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
  accessPoints:{ label: string; value: AccessPoint }[] = [];

  constructor(
    private temperaStationService: TemperaStationService,
    private accessPointService: AccessPointService,
    private formBuilder: FormBuilder,
  ) {
    this.temperaForm = this.formBuilder.group({
      id: [null, [Validators.required]],
      user: [null, []],
      accessPoint: [null, Validators.required]
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
        accessPointId: this.temperaForm.value.accessPoint.value.id
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
        this.fetchAccessPoints()
      },
      error: (error) => console.error('Error loading managers:', error)
    });
  }

  private fetchAccessPoints() {
    this.accessPointService.getAllAccesspoints().subscribe({
      next: (accesspoints: AccessPoint[]) => {
        this.accessPoints = accesspoints.map(accessPoint => ({
          label: accessPoint.id, // Change this to any property you want to display
          value: accessPoint
        }));
        console.log(this.accessPoints);
      }
    })
  }
}
