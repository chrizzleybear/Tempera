import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";
import {AccessPoint} from "../models/accesspoint.model";

@Injectable({
  providedIn: 'root'
})
export class AccesspointService {
  private API_URL = 'http://localhost:8080/api/accesspoint/';

  constructor(private http: HttpClient) { }

  getAllAccesspoints(): Observable<AccessPoint[]> {
    return this.http.get<AccessPoint[]>(this.API_URL + 'all');
  }

  getAccesspointById(accesspointId: number): Observable<AccessPoint> {
    return this.http.get<AccessPoint>(this.API_URL + 'load/' + accesspointId);
  }

  createAccesspoint(dto: AccessPoint) {
    return this.http.post(this.API_URL + 'create', dto);
  }

  updateAccesspoint(dto: AccessPoint) {
    return this.http.put(this.API_URL + 'update', dto);
  }

  deleteAccesspoint(accesspointId: number) {
    return this.http.delete(this.API_URL + 'delete/' + accesspointId);
  }
}
