import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TemperaStation} from "../models/temperaStation.model";
import {Sensor} from "../models/sensor.model";
import {User} from "../models/user.model";


@Injectable({
  providedIn: 'root'
})
export class TemperaStationService {
  private apiUrl = 'http://localhost:8080/api/temperastation/';

  constructor(private http: HttpClient) { }

  getAllTemperaStations(): Observable<TemperaStation[]> {
    return this.http.get<TemperaStation[]>(this.apiUrl+'all');
  }

  getTemperaStationById(id: string): Observable<TemperaStation> {
    return this.http.get<TemperaStation>(this.apiUrl + 'load/' + id);
  }

  createTemperaStation(temperaStation: TemperaStation): Observable<TemperaStation> {
    return this.http.put<TemperaStation>(this.apiUrl + 'create', temperaStation);
  }

  updateTemperaStation(temperaStation: TemperaStation): Observable<TemperaStation> {
    return this.http.put<TemperaStation>(this.apiUrl + 'update', temperaStation);
  }

  deleteTemperaStation(id: string): Observable<void> {
    return this.http.delete<void>(this.apiUrl + 'delete/' + id);
  }

  getTemperaStationSensors(id: string) {
    return this.http.get<Sensor[]>(this.apiUrl+'sensors/' + id);

  }

  getAvailableUsers() {
    return this.http.get<User[]>(this.apiUrl+'availableUsers');
  }
}
