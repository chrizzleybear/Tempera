import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";
import {AccessPoint} from "../models/accessPoint.model";
import {AccessPointCreateDto, AccessPointEditDto} from "../models/AccessPointDtos";
import {Room} from "../models/room.model";
import {MessageResponse} from "../../api";
import {TemperaStation} from "../models/temperaStation.model";

@Injectable({
  providedIn: 'root'
})
export class AccessPointService {
  private API_URL = 'http://localhost:8080/api/accesspoint/';

  constructor(private http: HttpClient) { }

  getAllAccesspoints(): Observable<AccessPoint[]> {
    return this.http.get<AccessPoint[]>(this.API_URL + 'all');
  }

  getAccesspointById(accesspointId: string): Observable<AccessPoint> {
    return this.http.get<AccessPoint>(this.API_URL + 'load/' + accesspointId);
  }

  createAccesspoint(dto: AccessPointCreateDto): Observable<AccessPoint>{
    return this.http.put<AccessPoint>(this.API_URL + 'create', dto);
  }

  updateAccesspoint(dto: AccessPointEditDto): Observable<AccessPoint> {
    return this.http.put<AccessPoint>(this.API_URL + 'update', dto);
  }

  deleteAccesspoint(accesspointId: string) {
    return this.http.delete<MessageResponse>(this.API_URL + 'delete/' + accesspointId);
  }

  getAccesspointsByRoomId(roomId: string): Observable<AccessPoint> {
    return this.http.get<AccessPoint>('http://localhost:8080/api/rooms/accesspoint/' + roomId);
  }

  getAvailableRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.API_URL + 'availableRooms');
  }
  getTemperaStations(accessPointId: string): Observable<TemperaStation[]> {
    return this.http.get<TemperaStation[]>(this.API_URL + 'tempera/' + accessPointId);
  }

  getAvailableTemperaStations(): Observable<TemperaStation[]> {
    return this.http.get<TemperaStation[]>(this.API_URL + 'tempera/available');
  }

  addTemperaStation(accessPointId: string, temperaId: string): Observable<MessageResponse> {
    return this.http.put(this.API_URL + 'tempera/add/' + accessPointId + '/' + temperaId, {});
  }

  removeTemperaStation(accessPointId: string, temperaId: string): Observable<MessageResponse> {
    return this.http.delete(this.API_URL + 'tempera/remove/' + accessPointId + '/' + temperaId);
  }
}
