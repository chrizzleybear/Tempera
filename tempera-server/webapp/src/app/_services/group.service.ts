import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Group} from "../models/group.model";
import {User} from "../models/user.model";
import {GroupCreateDTO, GroupMemberDTO, GroupUpdateDTO} from "../models/groupDtos";

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private API_URL = 'http://localhost:8080/api/groups/';

  constructor(private http: HttpClient) { }

  getAllGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.API_URL + 'all');
  }

  createGroup(dto: GroupCreateDTO): Observable<Group> {
    return this.http.post<Group>(this.API_URL + 'create', dto);
  }

  getGroupById(groupId: number): Observable<Group> {
    return this.http.get<Group>(this.API_URL + 'load/' + groupId);
  }

  updateGroup(dto: GroupUpdateDTO): Observable<Group> {
    return this.http.put<Group>(this.API_URL + 'update', dto);
  }

  deleteGroup(groupId: number): Observable<string> {
    return this.http.delete(`${this.API_URL}delete/${groupId}`, { responseType: 'text' })
      .pipe(
        catchError(error => throwError(() => new Error('Error deleting group: ' + error.message)))
      );
  }

  getGroupMembers(groupId: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}members/${groupId}`);
  }

  addGroupMember(dto: GroupMemberDTO): Observable<User> {
    return this.http.post<User>(`${this.API_URL}addMember`, dto);
  }

  deleteGroupMember(groupId: number, memberId: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}removeMember/${groupId}/${memberId}`);
  }

  getGroupByLead(groupLeadId: string): Observable<Group[]> {
    return this.http.get<Group[]>(`${this.API_URL}groupLead/${groupLeadId}`);
  }
}
