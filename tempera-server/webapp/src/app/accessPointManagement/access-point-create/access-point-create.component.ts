import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormGroup, FormBuilder, Validators, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Room} from "../../models/room.model";
import {AccessPointService} from "../../_services/access-point.service";
import {NgForOf, NgIf} from "@angular/common";
import {AccessPointCreateDto} from "../../models/AccessPointDtos";
import {InputTextModule} from "primeng/inputtext";
import {MessageModule} from "primeng/message";
import {DropdownModule} from "primeng/dropdown";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {MessageService} from "primeng/api";
@Component({
  selector: 'app-access-point-create',
  templateUrl: './access-point-create.component.html',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    MessageModule,
    DropdownModule,
    CheckboxModule,
    NgIf,
    ButtonModule
  ],
  styleUrls: ['./access-point-create.component.css']
})
export class AccessPointCreateComponent implements OnInit {

  accessPointForm: FormGroup;
  rooms: Room[] = [];

  @Output() createComplete = new EventEmitter<boolean>();

  constructor(
    private formBuilder: FormBuilder,
    private accessPointService: AccessPointService,
    private messageService: MessageService
    ) {
    this.accessPointForm = this.formBuilder.group({
      id: [null, [Validators.required]],
      room: [null , [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.fetchRooms();
  }

  private fetchRooms() {
    this.accessPointService.getAvailableRooms().subscribe({
      next: (rooms) => {
        this.rooms = rooms;
      },
      error: (error) => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error loading rooms'});
        console.error('Error loading rooms:', error);
      }
    });
  }
  onSubmit() {
    if (this.accessPointForm.valid) {
      const dto: AccessPointCreateDto = {
        id: this.accessPointForm.value.id,
        room: this.accessPointForm.value.room.id
      }
      this.accessPointService.createAccessPointViaDto(dto
      ).subscribe({
        next: (response) => {
          this.messageService.add({severity: 'success', summary: 'Success', detail: 'Access point created successfully'});
          console.log('Access point created:', response);
          this.accessPointForm.reset();
          this.createComplete.emit(true);
        },
        error: (error) => {
          this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error creating access point'});
          console.error('Error creating access point:', error)
          this.createComplete.emit(false);
        }
      });
    }
  }
}
