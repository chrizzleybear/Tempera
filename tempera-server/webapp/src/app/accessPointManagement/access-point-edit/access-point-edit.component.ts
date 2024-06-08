import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {AccessPoint} from "../../models/accessPoint.model";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AccessPointService} from "../../_services/access-point.service";
import {RoomService} from "../../_services/room.service";
import {AccessPointEditDto} from "../../models/AccessPointDtos";
import {DropdownModule} from "primeng/dropdown";
import {Room} from "../../models/room.model";
import {ButtonModule} from "primeng/button";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-access-point-edit',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    DropdownModule,
    ButtonModule
  ],
  templateUrl: './access-point-edit.component.html',
  styleUrl: './access-point-edit.component.css'
})
export class AccessPointEditComponent implements OnInit, OnChanges{

  accessPointForm: FormGroup;
  rooms: Room[] = [];


  @Input() accessPoint!: AccessPoint;
  @Output() editComplete = new EventEmitter<boolean>();

  constructor(private formBuilder: FormBuilder, private accessPointService: AccessPointService, private messageService: MessageService) {
    this.accessPointForm = this.formBuilder.group({
      room: [null , [Validators.required]],
      enabled: [true, [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.fetchAccessPoint()
  }

  ngOnChanges(SimpleChanges: any) {
    this.fetchRooms();
  }

  private fetchRooms() {
    this.accessPointService.getAvailableRooms().subscribe({
      next: (rooms) => {
        this.rooms = rooms;
        console.log('Loaded rooms:', rooms);
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error loading rooms'});
        console.error('Error loading rooms:', error);
      }
    });
    this.populateForm();
  }
  private fetchAccessPoint() {
    this.accessPointService.getAccesspointById(this.accessPoint.id).subscribe({
      next: (accessPoint) => {
        this.accessPoint = accessPoint;
        this.accessPointForm.setValue({
          room: accessPoint.room,
          enabled: accessPoint.enabled
        });
        this.fetchRooms();
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error loading access point'});
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
      if(this.accessPointForm.value.room.id === undefined) {
        this.accessPointForm.value.room = this.accessPoint.room;
      }
      const dto: AccessPointEditDto = {
        id : this.accessPoint.id,
        room: this.accessPointForm.value.room,
        enabled: this.accessPointForm.value.enabled
      }
      console.log('Editing access point:', dto);
      this.accessPointService.updateAccesspoint(dto
      ).subscribe({
        next: (response) => {
          this.messageService.add({severity: 'success', summary: 'Success', detail: 'Access Point edited successfully'});
          console.log('Access point edited:', response);
          this.accessPointForm.reset();
          this.editComplete.emit(true);
        },
        error: (error) => {
          this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error editing access point'});
          console.error('Error editing access point:', error)
        }
      });
    }
    this.editComplete.emit(true);

  }
}
