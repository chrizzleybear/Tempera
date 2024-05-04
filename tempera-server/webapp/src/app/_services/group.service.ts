import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable, throwError} from 'rxjs';
import {Group} from "../models/group.model";

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private API_URL = 'http://localhost:8080/api/groups/';

  constructor(private http: HttpClient) { }

  getAllGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.API_URL + 'all');
  }

  createGroup(group: Group): Observable<Group> {
    return this.http.post<Group>(this.API_URL + 'create', group);
  }

  getGroupById(id: string): Observable<Group> {
    return this.http.get<Group>(this.API_URL + id);
  }

  updateGroup(group: Group): Observable<Group> {
    return this.http.put<Group>(this.API_URL + 'update', group);
  }


  deleteGroup(groupId: string) {
    return this.http.delete(this.API_URL + 'delete/' + groupId);

  }
}
