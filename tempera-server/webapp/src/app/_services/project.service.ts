import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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

  test(): Observable<any> {
    return this.http.post(`${this.API_URL}test`, null)

  }

  deleteProject(projectId: string): void {
    console.log("Delete project with ID: ", projectId);
    this.http.delete(`${this.API_URL}delete/${projectId}`).subscribe({
      next: (response) => {
        console.log("Project deleted successfully:", response);
      },
      error: (error) => {
        console.error("Error deleting project:", error);
      }
    });
  }

  updateProject(projectData: Project): Observable<any> {
    console.log("Update project with ID: ");
    return this.http.put(`${this.API_URL}update`, projectData);
  }

  getProjectById(projectId: string): Observable<Project> {
    return this.http.get<Project>(`${this.API_URL}load/${projectId}`);
  }

  createProject(projectData: Project): Observable<any> {
    console.log("Create project with ID: ");
    return this.http.put(`${this.API_URL}create`, projectData);
  }

}
