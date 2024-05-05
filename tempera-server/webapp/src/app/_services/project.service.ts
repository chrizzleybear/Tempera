import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable, throwError} from 'rxjs';
import {Project} from "../models/project.model";
import {Group} from "../models/group.model";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private API_URL = 'http://localhost:8080/api/project/';

  constructor(private http: HttpClient) { }

  getAllProject(): Observable<any> {
    return this.http.get<Project[]>(this.API_URL + 'all');
  }

  createProject(name: string, description: string, manager: string): Observable<any> {
    const projectData = {
      name: name,
      description: description,
      manager: manager
    };
    return this.http.post(`${this.API_URL}create`, projectData);
  }


  deleteProject(projectId: string): Observable<string> {
    console.log("Delete project with Name: ", projectId);
    return this.http.delete(`${this.API_URL}delete/${projectId}`, { responseType: 'text' })
      .pipe(
        map(response => {
          console.log("Project deleted successfully:", response);
          return response;
        }),
        catchError(error => {
          console.error("Error deleting project:", error);
          return throwError(() => new Error('Error deleting project: ' + error.message));  // properly handle and throw an error
        })
      );
  }

  updateProject(projectId: number, name: string, description: string, manager: string): Observable<any> {
    return this.http.put(`${this.API_URL}update`, {projectId, name, description, manager});
  }

  getProjectById(projectId: number): Observable<Project> {
    return this.http.get<Project>(`${this.API_URL}load/${projectId}`);
  }

  addGroupToProject(projectId: string, groupId: string): Observable<any> {
    console.log("Get groups for project with ID: ", projectId, " and group ID: ", groupId);
    return this.http.post(`${this.API_URL}addGroup`, {projectId, groupId});
  }

  getGroups(projectId: string | null): Observable<Group[]> {
    return this.http.get<Group[]>(`${this.API_URL}getGroups/${projectId}`);

  }

  deleteGroupFromProject(projectId: string, groupId: string) {
    return this.http.delete(`${this.API_URL}deleteGroup/${projectId}/${groupId}`);

  }

  getProjectsOfGroup(groupId: string): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.API_URL}allOfGroup/${groupId}`);
  }

  addMemberToProject(projectId: string, contributorId: string): Observable<any> {
    return this.http.post(`${this.API_URL}addContributor`, {projectId, contributorId});
  }

  removeMemberFromProject(projectId: string, contributorId: string) {
    return this.http.delete(`${this.API_URL}deleteContributor/${projectId}/${contributorId}`);
  }
}
