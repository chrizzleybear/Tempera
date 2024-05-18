import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";
import {AccessPoint} from "../models/accessPoint.model";
import {AccessPointCreateDto, AccessPointEditDto} from "../models/AccessPointDtos";

@Injectable({
  providedIn: 'root'
})
export class AccessPointService {
  private API_URL = 'http://localhost:8080/api/accesspoint/';

  constructor(private http: HttpClient) { }

  getAllAccesspoints(): Observable<AccessPoint[]> {
    return this.http.get<AccessPoint[]>(this.API_URL + 'all');
  }

  getAccesspointById(accesspointId: number): Observable<AccessPoint> {
    return this.http.get<AccessPoint>(this.API_URL + 'load/' + accesspointId);
  }

  createAccesspoint(dto: AccessPointCreateDto): Observable<AccessPoint>{
    return this.http.post<AccessPoint>(this.API_URL + 'create', dto);
  }

  updateAccesspoint(dto: AccessPointEditDto): Observable<AccessPoint> {
    return this.http.put<AccessPoint>(this.API_URL + 'update', dto);
  }

  deleteAccesspoint(accesspointId: number) {
    return this.http.delete(this.API_URL + 'delete/' + accesspointId);
  }

  getAccesspointsByRoomId(roomId: string): Observable<AccessPoint> {
    return this.http.get<AccessPoint>('http://localhost:8080/api/rooms/accesspoint/' + roomId);
  }
}
