import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TemperaStation} from "../models/temperaStation.model";


@Injectable({
  providedIn: 'root'
})
export class TemperaStationService {
  private apiUrl = 'http://localhost:8080/api/temperastation/'; // replace with your API endpoint

  constructor(private http: HttpClient) { }

  getAllTemperaStations(): Observable<TemperaStation[]> {
    return this.http.get<TemperaStation[]>(this.apiUrl+'all');
  }

  getTemperaStationById(id: string): Observable<TemperaStation> {
    return this.http.get<TemperaStation>(`${this.apiUrl}/${id}`);
  }

  createTemperaStation(temperaStation: TemperaStation): Observable<TemperaStation> {
    return this.http.post<TemperaStation>(this.apiUrl, temperaStation);
  }

  updateTemperaStation(id: string, temperaStation: TemperaStation): Observable<TemperaStation> {
    return this.http.put<TemperaStation>(`${this.apiUrl}/${id}`, temperaStation);
  }

  deleteTemperaStation(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
