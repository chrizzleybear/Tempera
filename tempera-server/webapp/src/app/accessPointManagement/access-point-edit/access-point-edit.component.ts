import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AccessPoint} from "../../models/accesspoint.model";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Room} from "../../models/room.model";
import {AccessPointService} from "../../_services/access-point.service";
import {RoomService} from "../../_services/room.service";
import {AccessPointEditDto} from "../../models/AccessPointDtos";

@Component({
  selector: 'app-access-point-edit',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './access-point-edit.component.html',
  styleUrl: './access-point-edit.component.css'
})
export class AccessPointEditComponent implements OnInit{

  accessPointForm: FormGroup;
  rooms: Room[] = [];


  @Input() accessPoint!: AccessPoint;
  @Output() editComplete = new EventEmitter<boolean>();

  constructor(private formBuilder: FormBuilder, private accessPointService: AccessPointService, private roomService: RoomService) {
    this.accessPointForm = this.formBuilder.group({
      room: [null , [Validators.required]],
      enabled: [true, [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.fetchAccessPoint()
    this.fetchRooms();
    this.populateForm();
  }

  private fetchRooms() {
    this.roomService.getAllRooms().subscribe({
      next: (rooms) => {
        this.rooms = rooms;
        console.log('Loaded rooms:', rooms);
      },
      error: (error) => {
        console.error('Error loading rooms:', error);
      }
    });
  }
  private fetchAccessPoint() {
    this.accessPointService.getAccesspointById(this.accessPoint.id).subscribe({
      next: (accessPoint) => {
        this.accessPoint = accessPoint;
        this.accessPointForm.setValue({
          room: accessPoint.room,
          enabled: accessPoint.enabled
        });
      },
      error: (error) => {
        console.error('Error loading access point:', error);
      }
    });
  }
  private populateForm() {
    this.accessPointForm.setValue({
      room: this.accessPoint.room,
      enabled: this.accessPoint.enabled
    });
  }

  onSubmit() {
    if (this.accessPointForm.valid) {
      const dto: AccessPointEditDto = {
        id : this.accessPoint.id,
        room: this.accessPointForm.value.room,
        enabled: this.accessPointForm.value.enabled
      }
      this.accessPointService.updateAccesspoint(dto
      ).subscribe({
        next: (response) => {
          console.log('Access point edited:', response);
          this.editComplete.emit(true);
        },
        error: (error) => {
          console.error('Error editing access point:', error)
        }
      });
    }
    this.editComplete.emit(true);

  }
}
