import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable, throwError} from 'rxjs';
import {Project} from "../models/project.model";

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


  deleteProject(projectName: string): Observable<string> {
    console.log("Delete project with Name: ", projectName);
    return this.http.delete(`${this.API_URL}delete/${projectName}`, { responseType: 'text' })
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

  updateProject(projectData: Project): Observable<any> {
    console.log("Update project with ID: ");
    return this.http.put(`${this.API_URL}update`, projectData);
  }

  getProjectById(projectId: string): Observable<Project> {
    return this.http.get<Project>(`${this.API_URL}load/${projectId}`);
  }

}
