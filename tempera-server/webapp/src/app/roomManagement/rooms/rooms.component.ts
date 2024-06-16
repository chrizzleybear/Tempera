import {Component, OnInit} from '@angular/core';
import {RoomService} from "../../_services/room.service";
import {Room} from "../../models/room.model";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {MessagesModule} from "primeng/messages";
import {NgForOf, NgIf} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {UserCreateComponent} from "../../userManagement/user-create/user-create.component";
import {FormsModule} from "@angular/forms";
import {FloorPlanComponent} from "../floor-plan/floor-plan.component";
import {RippleModule} from "primeng/ripple";
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";

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
    FloorPlanComponent,
    NgForOf,
    RippleModule,
    ToastModule
  ],
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css']
})
/**
 * @class RoomsComponent
 * This component is responsible for managing and displaying all rooms.
 */
export class RoomsComponent implements OnInit {

  rooms: Room[] = [];
  newRoomId: string = '';
  displayCreateDialog: boolean = false;
  expandedRows: { [key: string]: boolean } = {};
  protected filteredRooms: Room[] = [];

  constructor(private roomService: RoomService, private router: Router, private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms(): void {
    this.roomService.getAllRooms().subscribe({
      next: (rooms) => {
        console.log('Loaded rooms:', rooms);
        this.filteredRooms = rooms;
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
        this.roomService.roomChanged();
        this.messageService.add({severity: 'success', summary: 'Success', detail: 'Room deleted successfully'});
      },
      error: (error) => {
        console.error('Error deleting room:', error)
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error deleting room'});
      }
    });
    this.loadRooms();
  }

  createRoomDialog(): void {
    this.displayCreateDialog = true;
  }

  createRoom(): void {
    console.log(this.newRoomId);
    this.roomService.createRoom(this.newRoomId).subscribe({
      next: (response) => {
        console.log('Room created successfully:', response);
        this.messageService.add({severity: 'success', summary: 'Success', detail: 'Room created successfully'});
        this.loadRooms();
        this.newRoomId = '';
        this.displayCreateDialog = false;
        this.roomService.roomChanged();
      },
      error: (error) => {
        console.error('Error creating room:', error);
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error creating room'});
      }
    });
  }

  applyFilter($event: Event) {
    const filterValue = ($event.target as HTMLInputElement).value;
    console.log('Filtering rooms:', filterValue);
    this.filteredRooms = this.rooms.filter(room => room.id.includes(filterValue));
  }

  detailedView(id: string) {
    this.router.navigate(['/room', id]);
  }
}
