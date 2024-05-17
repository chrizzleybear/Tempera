import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../models/room.model";

@Injectable({
  providedIn: 'root',
})

export class RoomService {
  private API_URL = 'http://localhost:8080/api/rooms/';

  constructor(private http: HttpClient) {
  }

  getAllRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.API_URL + 'all');
  }

  deleteRoom(roomId: string): Observable<string> {
    return this.http.delete<string>(`${this.API_URL}delete/${roomId}`,{ responseType: 'text' as 'json' });
  }

  getRoomById(roomId: string): Observable<Room> {
    return this.http.get<Room>(`${this.API_URL}load/${roomId}`);
  }

  createRoom(roomId: string): Observable<string> {
    return this.http.post<string>(`${this.API_URL}create`, roomId, { responseType: 'text' as 'json' });
  }

  getAvailableRooms(): Observable<Room[]>{
    return this.http.get<Room[]>(this.API_URL + 'available');
  }

  addThreadhold(roomId: string, threshold: any): Observable<Room> {
    return this.http.post<Room>(`${this.API_URL}addThreshold/${roomId}`, threshold);
  }

  deleteThreshold(roomId: string): Observable<Room> {
    return this.http.delete<Room>(`${this.API_URL}deleteThreshold/${roomId}`);
  }
}
