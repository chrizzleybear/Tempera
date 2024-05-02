import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {HomeData} from "../models/home-data.model";

const API_URL = 'http://localhost:8080/api/user/';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  getHomeData(): Observable<HomeData> {
    return this.http.get<HomeData>(API_URL + 'homeData');
  }

}
