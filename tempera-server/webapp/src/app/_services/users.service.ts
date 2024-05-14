import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  private API_URL = 'http://localhost:8080/api/users/';

  constructor(private http: HttpClient) {
  }

  getAllUsers(): Observable<any> {
    return this.http.get<User[]>(this.API_URL + 'all');
  }

  deleteUser(userId: string): Observable<any> {
    console.log('Delete user with ID: ', userId);
    return this.http.delete(`${this.API_URL}delete/${userId}`);
  }

  updateUser(userData: User): Observable<User> {
    console.log('Update user with ID: ');
    return this.http.put<User>(`${this.API_URL}update`, userData);
  }

  getUserById(userId: string): Observable<User> {
    return this.http.get<User>(`${this.API_URL}load/${userId}`);
  }

  saveUser(userData: User): Observable<User> {
    console.log('Create user with ID: ');
    return this.http.post<User>(`${this.API_URL}create`, userData);
  }

  validateUser(username: string, password: string): Observable<User> {
    console.log('Validate user with ID: ', username);
    return this.http.post<User>(`http://localhost:8080/api/users/validate`, { username, password });
  }

  enableUser(username: string, password: string): Observable<Object> {
    console.log('Enable user with ID: ', username);
    return this.http.post(`http://localhost:8080/api/users/enable`, { username, password });
  }

  getAllManagers(): Observable<User[]> {
    return this.http.get<User[]>(this.API_URL + 'managers');
  }
}
