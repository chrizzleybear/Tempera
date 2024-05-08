import { Component } from '@angular/core';
import {RoomService} from "../../_services/room.service";
import {Room} from "../../models/room.model";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {MessagesModule} from "primeng/messages";
import {NgIf} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {UserCreateComponent} from "../../userManagement/user-create/user-create.component";
import {FormsModule} from "@angular/forms";
import {FloorPlanComponent} from "../../rooms/floor-plan/floor-plan.component";

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [
    TableModule,
    ButtonModule,
    InputTextModule,
    MessagesModule,
    NgIf,
    DialogModule,
    UserCreateComponent,
    FormsModule,
    FloorPlanComponent
  ],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.css'
})
export class RoomsComponent {

  rooms: Room[] = [];
  newRoomId: string = '';
  displayCreateDialog: boolean = false;
  messages: any;

  constructor(private roomService: RoomService) { }

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms(): void {
    this.roomService.getAllRooms().subscribe({
      next: (rooms) => {
        this.rooms = rooms;
      },
      error: (error) => console.error('Error fetching rooms:', error)
    });
  }

  deleteRoom(roomId: string): void {
    console.log('Delete room with ID: ', roomId);
    this.roomService.deleteRoom(roomId).subscribe({
      next: (response) => {
        console.log('Room deleted successfully:', response);
        this.loadRooms();
        this.messages = [{ severity: 'success', summary: 'Success', detail: 'Room deleted successfully' }];
      },
      error: (error) => console.error('Error deleting room:', error)
    });
    this.loadRooms();
  }

  createRoomDialog():void {
    this.displayCreateDialog = true;
  }

  createRoom(): void {
    console.log(this.newRoomId);
    this.roomService.createRoom(this.newRoomId).subscribe({
      next: (response) => {
        console.log('Room created successfully:', response);
        this.loadRooms();
        this.displayCreateDialog = false;
        this.messages = [{ severity: 'success', summary: 'Success', detail: 'Room created successfully' }];
      },
      error: (error) => console.error('Error creating room:', error)
    });
  }

  applyFilter($event: Event) {


  }
}
