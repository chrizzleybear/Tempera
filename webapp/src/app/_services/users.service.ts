import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private API_URL = 'http://localhost:8080/api/users/';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<any> {
    return this.http.get<User[]>(this.API_URL + 'all');
  }

  deleteUser(id: string) {
    console.log('delete user with id: ' + id);
    return this.http.delete(`${this.API_URL}delete/${id}`);
  }

  deleteUser1(id: string) {
    console.log('delete user with id: ' + id);
    return this.http.delete(this.API_URL + 'delete/user1');
  }
}
