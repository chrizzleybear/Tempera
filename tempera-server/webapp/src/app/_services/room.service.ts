import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../models/room.model";

class List<T> {
}

@Injectable({
  providedIn: 'root',
})

export class RoomService {
  private API_URL = 'http://localhost:8080/api/rooms/';

  constructor(private http: HttpClient) {
  }

  getAllRooms(): Observable<List<Room>> {
    return this.http.get<Room[]>(this.API_URL + 'all');
  }

  deleteRoom(roomId: string): void {
    console.log('Delete room with ID: ', roomId);
    this.http.delete(`${this.API_URL}delete/${roomId}`).subscribe({
      next: (response) => {
        console.log('Room deleted successfully:', response);
      },
      error: (error) => {
        console.error('Error deleting room:', error);
      },
    });
  }

  getRoomById(roomId: string): Observable<Room> {
    return this.http.get<Room>(`${this.API_URL}load/${roomId}`);
  }

  createRoom(roomId: String): Observable<any> {
    console.log('Create room with ID: ');
    return this.http.post(`${this.API_URL}create`, { roomId });
  }

}
