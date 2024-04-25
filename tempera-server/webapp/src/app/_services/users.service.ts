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

  deleteUser(userId: string): void {
    console.log("Delete user with ID: ", userId);
    this.http.delete(`${this.API_URL}delete/${userId}`).subscribe({
      next: (response) => {
        console.log("User deleted successfully:", response);
      },
      error: (error) => {
        console.error("Error deleting user:", error);
      }
    });
  }

  updateUser(userData: User): Observable<any> {
    console.log("Update user with ID: ");
    return this.http.put(`${this.API_URL}update`, userData);
  }
  getUserById(userId: string): Observable<User> {
    return this.http.get<User>(`${this.API_URL}load/${userId}`);
  }

  saveUser(userData: User): Observable<any> {
    console.log("Create user with ID: ");
    return this.http.post(`${this.API_URL}create`, userData);
  }

  validateUser(username: string, password: string) {
    console.log("Validate user with username: ", username);
    return this.http.post(`${this.API_URL}validate`, {username, password});
  }
}
