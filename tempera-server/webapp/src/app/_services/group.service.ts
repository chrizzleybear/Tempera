import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable, throwError} from 'rxjs';
import {Group} from "../models/group.model";
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private API_URL = 'http://localhost:8080/api/groups/';

  constructor(private http: HttpClient) { }

  getAllGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.API_URL + 'all');
  }

  createGroup(name: string, description: string, groupLead: string ): Observable<Group> {
    return this.http.post<Group>(this.API_URL + 'create', {name, description, groupLead});
  }

  getGroupById(groupId: string): Observable<Group> {
    return this.http.get<Group>(this.API_URL + 'load/'+ groupId);
  }

  updateGroup(groupId: string, name: string, description: string, groupLead: string ): Observable<Group> {
    return this.http.put<Group>(this.API_URL + 'update', {groupId, name, description, groupLead});
  }

  deleteGroup(groupId: string) {
    return this.http.delete(`${this.API_URL}delete/${groupId}`, { responseType: 'text' })
      .pipe(
        map(response => {
          console.log("Group deleted successfully:", response);
          return response;
        }),
        catchError(error => {
          console.error("Error deleting group:", error);
          return throwError(() => new Error('Error deleting project: ' + error.message));  // properly handle and throw an error
        })
      );
  }

  getGroupMembers(groupId: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}members/${groupId}`);
  }

  addGroupMember(groupId: string, memberId: string): Observable<User> {
    return this.http.post<User>(`${this.API_URL}addMember`, {groupId, memberId});
  }

  deleteGroupMember(groupId: string, memberId: string): Observable<any> {
    return this.http.delete(`${this.API_URL}removeMember/${groupId}/${memberId}`);
  }

  getGroupByLead(groupLeadId: string): Observable<Group[]> {
    return this.http.get<Group[]>(`${this.API_URL}/groupLead/${groupLeadId}`);
  }
}
