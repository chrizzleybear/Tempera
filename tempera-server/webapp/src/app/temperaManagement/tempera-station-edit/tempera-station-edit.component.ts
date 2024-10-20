import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import { TemperaStationService } from '../../_services/tempera-station.service';
import { TemperaStation } from '../../models/temperaStation.model';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {User} from "../../models/user.model";
import {DropdownModule} from "primeng/dropdown";
import {MessageService} from "primeng/api";

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
    private messageService: MessageService
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
        this.messageService.add({severity:'error', summary:'Error', detail:'Failed to load temperaStation details'});
        console.error('Failed to load temperaStation details:', error);
      },
    });
  }

  onSubmit() {
    if (this.temperaForm?.valid) {
      if(this.temperaForm.value.user.value == undefined) {
        this.temperaStation.enabled = this.temperaForm.value.enabled;
      }
      else {
        this.temperaStation.user = this.temperaForm.value.user.value.username;
        this.temperaStation.enabled = this.temperaForm.value.enabled;
      }
      this.temperaStationService.updateTemperaStation(this.temperaStation).subscribe({
        next: () => {
          this.messageService.add({severity:'success', summary:'Success', detail:'Tempera station updated successfully'});
          this.temperaForm?.reset();
          this.onEditComplete.emit(true);

        },
        error: (error) => {
          this.messageService.add({severity:'error', summary:'Error', detail:'Failed to update tempera station'});
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
    this.temperaForm = this.formBuilder.group({
      user: this.temperaStation.user,
      enabled: this.temperaStation.enabled
    });
  }
}

