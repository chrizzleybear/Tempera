import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { TemperaStationService } from "../../_services/tempera-station.service";
import { DropdownModule } from "primeng/dropdown";
import { CheckboxModule } from "primeng/checkbox";
import { ButtonModule } from "primeng/button";
import { User } from "../../models/user.model";
import { InputTextModule } from "primeng/inputtext";
import { TemperaStationCreateDto } from "../../models/TemperaStationDtos";
import { AccessPoint } from "../../models/accessPoint.model";
import { AccessPointService } from "../../_services/access-point.service";
import { MessageService } from "primeng/api";
import { ToastModule } from "primeng/toast";

@Component({
  selector: 'app-tempera-station-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    DropdownModule,
    CheckboxModule,
    ButtonModule,
    InputTextModule,
    ToastModule
  ],
  templateUrl: './tempera-station-create.component.html',
  styleUrls: ['./tempera-station-create.component.css']
})
export class TemperaStationCreateComponent implements OnInit {

  temperaForm: FormGroup;
  @Output() onCreateCompleted = new EventEmitter<boolean>();
  users: { label: string; value: User; }[] = [];
  accessPoints: { label: string; value: AccessPoint }[] = [];

  constructor(
    private temperaStationService: TemperaStationService,
    private accessPointService: AccessPointService,
    private formBuilder: FormBuilder,
    private messageService: MessageService
  ) {
    this.temperaForm = this.formBuilder.group({
      user: [null, [Validators.required]],
      accessPoint: [null, Validators.required]
    });
  }

  ngOnInit() {
    this.fetchUsers();
  }

  onSubmit() {
    if (this.temperaForm.valid) {
      const dto: TemperaStationCreateDto = {
        user: this.temperaForm.value.user.value.username,
        accessPointId: this.temperaForm.value.accessPoint.value.id
      };

      this.temperaStationService.createTemperaStation(dto).subscribe({
        next: () => {
          this.temperaForm.reset();
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Tempera station created successfully' });
          this.onCreateCompleted.emit(true);
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to create tempera station' });
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
        this.fetchAccessPoints();
      },
      error: (error) => console.error('Error loading users:', error)
    });
  }

  fetchAccessPoints() {
    this.accessPointService.getAllAccesspoints().subscribe({
      next: (accesspoints: AccessPoint[]) => {
        this.accessPoints = accesspoints.map(accessPoint => ({
          label: accessPoint.id,
          value: accessPoint
        }));
      },
      error: (error) => console.error('Error loading access points:', error)
    });
  }
}
