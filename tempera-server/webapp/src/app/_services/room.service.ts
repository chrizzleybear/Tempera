import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, Subject} from "rxjs";
import {FloorComponent, Room} from "../models/room.model";
import {Threshold, ThresholdTipUpdateDto, ThresholdUpdateDto} from "../models/threshold.model";
import {AccessPoint} from "../models/accessPoint.model";



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
//two way binding ->delete
  getAccessPoint(roomId: string): Observable<AccessPoint> {
    return this.http.get<AccessPoint>(`${this.API_URL}accesspoint/${roomId}`);
  }

  updateThreshold(dto: ThresholdUpdateDto): Observable<Threshold>{
    console.log('Update Threshold: ', dto);
    return this.http.put<Threshold>(`${this.API_URL}threshold/update`, dto);
  }

  updateThresholdTip(dto: ThresholdTipUpdateDto) {
    return this.http.put<Threshold>(`${this.API_URL}threshold/tip/update`, dto);
  }

  getFloorPlan(): Observable<FloorComponent[]> {
    return this.http.get<FloorComponent[]>(this.API_URL + 'floor');
  }

  private roomChangedSource = new Subject<void>();
  roomChanged$ = this.roomChangedSource.asObservable();

  roomChanged() {
    this.roomChangedSource.next();
  }

}
