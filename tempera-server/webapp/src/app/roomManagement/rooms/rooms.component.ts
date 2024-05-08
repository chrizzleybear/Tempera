import { Component } from '@angular/core';
import {RoomService} from "../../_services/room.service";
import {Room} from "../../models/room.model";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [
    TableModule,
    ButtonModule
  ],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.css'
})
export class RoomsComponent {

  rooms: Room[] = [];
  newRoomId: string = '';

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
      },
      error: (error) => console.error('Error deleting room:', error)
    });
    this.loadRooms();
  }

  createRoom(): void {
    this.roomService.createRoom(this.newRoomId).subscribe({
      next: (response) => {
        console.log('Room created successfully:', response);
        this.loadRooms();
      },
      error: (error) => console.error('Error creating room:', error)
    });
  }
}
